package com.example.palmprint_recognition.ui.user.features.palmprint_camera.screens

import android.Manifest
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.CameraCaptureButton
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.CameraGuideOverlay
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.CameraGuideText
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.CameraPermissionContent
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.CameraPreview
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.CameraStatusText
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraGuideDebugState
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraRealtimeState
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.CameraRealtimeFrameAnalyzer
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analyzeCapturedBitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.bindCameraUseCases
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.captureToFileThenBitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.cropBitmapByGuideRect
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.toCameraConditionMessage


/**
 * 손바닥 촬영용 커스텀 카메라 화면
 *
 * 기능
 * - CameraX 프리뷰 표시
 * - 손바닥 가이드라인 표시
 * - 촬영 버튼 처리
 * - crop 및 촬영 조건 분석
 * - 실시간 프레임 분석 구조 연결
 * - 촬영 결과를 상위 화면으로 전달
 *
 * @param onCaptured 촬영 완료된 Bitmap 콜백
 * @param onCancel 카메라 종료 콜백
 */
@Composable
fun CameraScreen(
    onCaptured: (Bitmap) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isCapturing by remember { mutableStateOf(false) }

    val previewView = remember { PreviewView(context) }

    var previewWidth by remember { mutableStateOf(0f) }
    var previewHeight by remember { mutableStateOf(0f) }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    var debugState by remember { mutableStateOf(CameraGuideDebugState()) }
    var realtimeState by remember { mutableStateOf(CameraRealtimeState()) }

    /**
     * 실시간 프레임 분석기
     *
     * - 프레임이 들어오는 구조 연결한다
     * - 프레임 스킵: 5프레임 중 1개만 분석한다
     */
    val realtimeAnalyzer = remember {
        CameraRealtimeFrameAnalyzer(
            analysisInterval = 5,
            onFrameAvailable = { frameMetadata ->
                realtimeState = frameMetadata.realtimeState.copy(
                    isAnalyzing = false
                )

                Log.d(
                    "CameraRealtime",
                    "frameIndex=${frameMetadata.frameIndex}, " +
                            "frame=${frameMetadata.width}x${frameMetadata.height}, " +
                            "rotation=${frameMetadata.rotationDegrees}, " +
                            "timestamp=${frameMetadata.timestamp}, " +
                            "blur=${frameMetadata.blurScore}, " +
                            "condition=${frameMetadata.realtimeState.condition}"
                )
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) {
            errorMessage = "카메라 권한이 필요합니다."
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(hasPermission) {
        if (!hasPermission) return@LaunchedEffect

        runCatching {
            bindCameraUseCases(
                context = context,
                lifecycleOwner = lifecycleOwner,
                previewView = previewView,
                analyzer = realtimeAnalyzer
            ) { capture ->
                imageCapture = capture
            }
        }.onFailure { exception ->
            Log.e("CameraScreen", "Camera bind failed", exception)
            errorMessage = "카메라 초기화에 실패했습니다."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (!hasPermission) {
            CameraPermissionContent(
                message = errorMessage ?: "카메라 권한 확인 중...",
                onCancel = onCancel
            )
            return@Box
        }

        CameraPreview(
            previewView = previewView,
            modifier = Modifier.fillMaxSize(),
            onPreviewSizeChanged = { width, height ->
                previewWidth = width
                previewHeight = height
            }
        )

        CameraGuideOverlay(
            modifier = Modifier.fillMaxSize()
        )

        CameraGuideText(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 44.dp)
        )

        errorMessage?.let { message ->
            CameraStatusText(
                text = message,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 96.dp)
            )
        }

        if (errorMessage == null) {
            CameraStatusText(
                text = realtimeState.message,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 96.dp)
            )
        }

        debugState.lastRatio?.let { last ->
            val average = debugState.averageRatio ?: last

            CameraStatusText(
                text = "frame ratio: ${"%.1f".format(last)}% / avg: ${"%.1f".format(average)}% (n=${debugState.ratioCount})",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 140.dp)
            )
        }

        realtimeState.blurScore?.let { blur ->
            CameraStatusText(
                text = "realtime blur: ${"%.1f".format(blur)}",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 184.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            CameraCaptureButton(
                enabled = !isCapturing,
                onClick = {
                    val capture = imageCapture

                    if (capture == null) {
                        errorMessage = "카메라가 아직 준비되지 않았습니다."
                        return@CameraCaptureButton
                    }

                    isCapturing = true
                    errorMessage = null

                    captureToFileThenBitmap(
                        context = context,
                        imageCapture = capture,
                        previewWidth = previewWidth,
                        previewHeight = previewHeight,
                        onSuccess = { originalBitmap ->
                            isCapturing = false

                            if (previewWidth <= 0f || previewHeight <= 0f) {
                                errorMessage = "프리뷰 크기를 알 수 없어 crop을 건너뜁니다."
                                onCaptured(originalBitmap)
                                return@captureToFileThenBitmap
                            }

                            val croppedBitmap = cropBitmapByGuideRect(
                                bitmap = originalBitmap,
                                viewW = previewWidth,
                                viewH = previewHeight
                            )

                            val analysisState = analyzeCapturedBitmap(
                                originalBitmap = originalBitmap,
                                croppedBitmap = croppedBitmap
                            )

                            debugState = debugState.addRatio(analysisState.ratio)

                            if (analysisState.condition != CameraCaptureCondition.READY) {
                                errorMessage = toCameraConditionMessage(
                                    analysisState.condition
                                )
                                return@captureToFileThenBitmap
                            }

                            errorMessage = toCameraConditionMessage(
                                analysisState.condition
                            )

                            onCaptured(croppedBitmap)

                            Log.d(
                                "PalmCrop",
                                "preview=${previewWidth}x${previewHeight}, " +
                                        "original=${originalBitmap.width}x${originalBitmap.height}, " +
                                        "cropped=${croppedBitmap.width}x${croppedBitmap.height}, " +
                                        "ratio=${analysisState.ratio}, " +
                                        "blur=${analysisState.blurScore}, " +
                                        "condition=${analysisState.condition}"
                            )
                        },
                        onFailure = { message ->
                            isCapturing = false
                            errorMessage = message
                        }
                    )
                }
            )
        }

        if (isCapturing) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}