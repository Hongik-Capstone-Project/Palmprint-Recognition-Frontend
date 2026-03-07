package com.example.palmprint_recognition.ui.user.features.palmprint_management.screens

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.palmprint_recognition.ui.user.features.palmprint_management.components.CameraShutterIcon
import java.io.File
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.sp
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import kotlin.math.roundToInt
import androidx.compose.ui.geometry.Rect as ComposeRect
import androidx.compose.ui.layout.onSizeChanged
import androidx.camera.core.AspectRatio

@Composable
fun CameraScreen(
    onCaptured: (Bitmap) -> Unit,
    onCancel: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var isCapturing by remember { mutableStateOf(false) }

    // CameraX 객체들
    val previewView = remember { PreviewView(context) }
    var previewW by remember { mutableStateOf(0f) }
    var previewH by remember { mutableStateOf(0f) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    // 간단 평균용 누적(화면 실행 중에만 유지)
    var ratioSum by remember { mutableStateOf(0f) }
    var ratioCount by remember { mutableStateOf(0) }
    var lastRatio by remember { mutableStateOf<Float?>(null) }

    // 권한 요청
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) error = "카메라 권한이 필요합니다."
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // 권한 승인되면 카메라 바인딩
    LaunchedEffect(hasPermission) {
        if (!hasPermission) return@LaunchedEffect

        runCatching {
            bindCameraUseCases(
                context = context,
                lifecycleOwner = lifecycleOwner,
                previewView = previewView
            ) { cap ->
                imageCapture = cap
            }
        }.onFailure { e ->
            Log.e("PalmCamera", "bindCameraUseCases failed", e)
            error = "카메라 초기화에 실패했습니다."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (!hasPermission) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = error ?: "카메라 권한 확인 중...", color = Color.White)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "돌아가기",
                    color = Color.White,
                    modifier = Modifier.clickable { onCancel() }
                )
            }
            return@Box
        }

        // 1) 카메라 미리보기 (가장 아래 레이어)
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { sz ->
                    previewW = sz.width.toFloat()
                    previewH = sz.height.toFloat()
                }
        )

        // 2) 가이드라인 오버레이
        TempPalmGuideOverlay(
            modifier = Modifier.fillMaxSize()
        )

        // 3) 상단 안내 텍스트 (미리보기 위에 나오도록 여기 위치)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 44.dp)
                .padding(horizontal = 16.dp)
                .background(Color(0x66000000))
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "손바닥을 타원 안에 맞춰주세요",
                color = Color.White
            )
            Text(
                text = "손목을 아래 선에 맞춰주세요",
                color = Color.White
            )
        }

        // 4) 에러 표시 (텍스트 아래쪽에 표시되게 조금 더 아래로)
        error?.let {
            Text(
                text = it,
                color = Color.Yellow,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 96.dp)  // 안내 문구 아래로 내림
                    .padding(horizontal = 16.dp)
            )
        }

        lastRatio?.let { r ->
            val avg = if (ratioCount > 0) ratioSum / ratioCount else r
            Text(
                text = "frame ratio: ${"%.1f".format(r)}%  / avg: ${"%.1f".format(avg)}% (n=$ratioCount)",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 140.dp)
                    .background(Color(0x66000000))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }

        // 5) 촬영 버튼
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clickable(enabled = !isCapturing) {
                        val cap = imageCapture
                        if (cap == null) {
                            error = "카메라가 아직 준비되지 않았습니다."
                            return@clickable
                        }
                        isCapturing = true
                        error = null


                        captureToFileThenBitmap(
                            context = context,
                            imageCapture = cap,
                            onSuccess = { bmp ->
                                isCapturing = false

                                if (previewW <= 0f || previewH <= 0f) {
                                    error = "프리뷰 크기를 알 수 없어 crop을 건너뜁니다."
                                    onCaptured(bmp)
                                    return@captureToFileThenBitmap
                                }

                                // ✅ 1) 타원 영역(정확히는 타원 bounding rect)로 crop
                                val cropped = cropBitmapByGuideRect(
                                    bitmap = bmp,
                                    viewW = previewW.toFloat(),
                                    viewH = previewH.toFloat()
                                )

                                // ✅ 2) crop 면적 비율(%)
                                val ratio = calcCropAreaRatioPercent(bmp, cropped)
                                lastRatio = ratio

                                // ✅ 3) 평균값 누적
                                ratioSum += ratio
                                ratioCount += 1
                                val avg = ratioSum / ratioCount.toFloat()

                                Log.d("PalmGuide", "Crop ratio = $ratio%, avg = $avg% (n=$ratioCount)")

                                // ✅ STEP3 맛보기: 너무 작거나/너무 크면 막기(임시 기준)
                                // - 기준은 나중에 당신이 실제 데이터 보고 조정
                                val tooSmall = ratio < 12f
                                val tooLarge = ratio > 45f

                                if (tooSmall) {
                                    error = "너무 멀리서 찍혔습니다. 손바닥이 타원에 더 꽉 차게 맞춰주세요."
                                    return@captureToFileThenBitmap
                                }
                                if (tooLarge) {
                                    error = "너무 가까이 찍혔습니다. 손바닥을 조금만 멀리 해주세요."
                                    return@captureToFileThenBitmap
                                }

                                // ✅ 최종: crop된 이미지로 다음 화면(등록 화면)에 보여주고/전송하도록
                                onCaptured(cropped)
                            },
                            onFailure = { msg ->
                                isCapturing = false
                                error = msg
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                CameraShutterIcon(iconSize = 96.dp)
            }
        }

        // 6) 로딩 오버레이
        if (isCapturing) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

/**
 * CameraX 바인딩 (Preview + ImageCapture)
 * - recomposition에 흔들리지 않도록 함수로 분리
 */
private fun bindCameraUseCases(
    context: Context,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    previewView: PreviewView,
    onReadyCapture: (ImageCapture) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val executor = ContextCompat.getMainExecutor(context)

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build()
            .also { it.setSurfaceProvider(previewView.surfaceProvider) }

        val rotation = previewView.display.rotation

        val imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(rotation)
            .build()

        val selector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            selector,
            preview,
            imageCapture
        )

        onReadyCapture(imageCapture)
    }, executor)
}

/**
 * 안전 촬영
 * - OnImageCapturedCallback(YUV) 쓰지 말고
 * - 파일로 저장 → BitmapFactory.decodeFile 로 읽기
 */
private fun captureToFileThenBitmap(
    context: Context,
    imageCapture: ImageCapture,
    onSuccess: (Bitmap) -> Unit,
    onFailure: (String) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(context)

    // 캐시에 임시 파일 생성
    val photoFile = File.createTempFile("palm_capture_", ".jpg", context.cacheDir)

    val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        output,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val bmp = decodeBitmapWithExifRotation(photoFile.absolutePath)
                if (bmp == null) {
                    onFailure("촬영 이미지 디코딩에 실패했습니다.")
                } else {
                    onSuccess(bmp)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                onFailure("촬영에 실패했습니다: ${exception.message}")
            }
        }
    )
}

/**
 * STEP 1 가이드라인 오버레이 (RGB 손바닥용)
 * - 중앙 세로 타원
 * - 손목 가이드 선
 * - 안내 텍스트f
 *
 * 목표: 왼손/오른손 상관 없이 "손바닥 중심부"가 일정한 위치/크기로 들어오게 유도
 */
@Composable
private fun TempPalmGuideOverlay(
    modifier: Modifier = Modifier,
    guideText: String = "손바닥을 타원 안에 맞춰주세요\n손목을 아래 선에 맞춰주세요"
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // -----------------------------
        // 1) 타원 프레임 영역 (세로 타원)
        // -----------------------------
        val spec = calculateGuideSpec(w, h)
        val ovalRect = spec.ovalRect
        val wristY = spec.wristY
        val ovalW = ovalRect.width
        val wristLineHalf = ovalW * 0.35f

        // 손목 가이드선 위치 (타원 하단 근처)
//        val wristY = ovalRect.bottom + (h * 0.03f)  // 타원 아래 약간 내려서
//        val wristLineHalf = ovalW * 0.35f           // 선 길이 (타원 너비 기준)

        // -----------------------------
        // 2) 배경 마스크 (전체 반투명)
        // -----------------------------
        drawRect(color = Color(0x88000000))

        // -----------------------------
        // 3) "뚫린 느낌" (완전한 구멍은 아니지만, 타원 내부를 더 밝게)
        //    - 초보자용: 블렌드 모드/clipPath 없이도 충분히 '여기 보세요' 효과가 남
        // -----------------------------
        drawOval(
            color = Color(0x22000000),
            topLeft = Offset(ovalRect.left, ovalRect.top),
            size = androidx.compose.ui.geometry.Size(ovalRect.width, ovalRect.height)
        )

        // -----------------------------
        // 4) 타원 테두리 (가장 중요한 가이드)
        // -----------------------------
        drawOval(
            color = Color.White,
            topLeft = Offset(ovalRect.left, ovalRect.top),
            size = androidx.compose.ui.geometry.Size(ovalRect.width, ovalRect.height),
            style = Stroke(width = 6f)
        )

        // -----------------------------
        // 5) 손목 가이드 선
        // -----------------------------
        drawLine(
            color = Color.White,
            start = Offset(x = w / 2f - wristLineHalf, y = wristY),
            end = Offset(x = w / 2f + wristLineHalf, y = wristY),
            strokeWidth = 6f
        )

//        // 손목선 아래에 작은 보조 텍스트(선택)
//        drawContext.canvas.nativeCanvas.apply {
//            val paint = android.graphics.Paint().apply {
//                isAntiAlias = true
//                color = android.graphics.Color.WHITE
//                textAlign = android.graphics.Paint.Align.CENTER
//                textSize = 14.sp.toPx()
//            }
//            drawText("손목을 이 선에 맞춰주세요", w / 2f, wristY + 40f, paint)
//        }
//
//        // -----------------------------
//        // 6) 안내 텍스트 (상단)
//        // -----------------------------
//        drawContext.canvas.nativeCanvas.apply {
//            val paint = android.graphics.Paint().apply {
//                isAntiAlias = true
//                color = android.graphics.Color.WHITE
//                textAlign = android.graphics.Paint.Align.CENTER
//                textSize = 18.sp.toPx()
//            }
//
//            // 여러 줄 텍스트 간단 처리 (초보자용)
//            val lines = guideText.split("\n")
//            val startY = ovalRect.top - 40f  // 타원 위쪽에 위치
//            val lineGap = 28f
//
//            lines.forEachIndexed { idx, line ->
//                drawText(line, w / 2f, startY - (lines.size - 1 - idx) * lineGap, paint)
//            }
//        }

        // -----------------------------
        // (선택) 중앙 표시점(너무 과하면 제거)
        // -----------------------------
        // val cx = w / 2f
        // val cy = ovalRect.top + ovalRect.height / 2f
        // drawCircle(Color.White, radius = 6f, center = Offset(cx, cy))
    }
}



private fun decodeBitmapWithExifRotation(filePath: String): Bitmap? {
    val bmp = BitmapFactory.decodeFile(filePath) ?: return null
    val exif = runCatching { ExifInterface(filePath) }.getOrNull() ?: return bmp

    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )

    val rotateDegrees = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }

    if (rotateDegrees == 0) return bmp

    val matrix = Matrix().apply { postRotate(rotateDegrees.toFloat()) }
    return Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
}

private data class GuideSpec(
    val ovalRect: Rect,
    val wristY: Float
)

/**
 * ✅ STEP2에서 "오버레이와 동일한 위치"를 Crop에도 재사용하기 위한 계산 함수
 * - Overlay에서 쓰던 비율(ovalW, ovalH)을 그대로 사용
 */
private fun calculateGuideSpec(viewW: Float, viewH: Float): GuideSpec {
    val ovalW = viewW * 0.75f
    val ovalH = viewH * 0.50f
    val ovalLeft = (viewW - ovalW) / 2f
    val ovalTop = (viewH - ovalH) / 2f
    val ovalRect = Rect(ovalLeft, ovalTop, ovalLeft + ovalW, ovalTop + ovalH)

    val wristY = ovalRect.bottom + (viewH * 0.03f)
    return GuideSpec(ovalRect = ovalRect, wristY = wristY)
}



/**
 *    Preview 좌표(화면 좌표)의 타원 Rect를,
 *    실제 Bitmap 좌표로 변환해서 crop
 */
private fun cropBitmapByGuideRect(
    bitmap: Bitmap,
    viewW: Float,
    viewH: Float
): Bitmap {
    val spec = calculateGuideSpec(viewW, viewH)
    val guideRect = spec.ovalRect

    // 1) 화면 좌표 -> 0~1 정규화
    val leftN = (guideRect.left / viewW).coerceIn(0f, 1f)
    val topN = (guideRect.top / viewH).coerceIn(0f, 1f)
    val rightN = (guideRect.right / viewW).coerceIn(0f, 1f)
    val bottomN = (guideRect.bottom / viewH).coerceIn(0f, 1f)

    // 2) 정규화 -> 비트맵 좌표
    val x = (leftN * bitmap.width).roundToInt().coerceIn(0, bitmap.width - 1)
    val y = (topN * bitmap.height).roundToInt().coerceIn(0, bitmap.height - 1)
    val w = ((rightN - leftN) * bitmap.width).roundToInt().coerceAtLeast(1)
    val h = ((bottomN - topN) * bitmap.height).roundToInt().coerceAtLeast(1)

    // 3) 범위 보정
    val safeW = (x + w).coerceAtMost(bitmap.width) - x
    val safeH = (y + h).coerceAtMost(bitmap.height) - y

    return Bitmap.createBitmap(bitmap, x, y, safeW, safeH)
}

/**
 *   crop 면적이 원본 대비 몇 %인지 계산
 * - STEP2의 "손바닥 프레임 점유율"의 아주 기초 버전(프레임 자체 점유율)
 */
private fun calcCropAreaRatioPercent(original: Bitmap, cropped: Bitmap): Float {
    val total = original.width.toFloat() * original.height.toFloat()
    val part = cropped.width.toFloat() * cropped.height.toFloat()
    if (total <= 0f) return 0f
    return (part / total) * 100f
}