package com.example.palmprint_recognition.ui.user.features.palmprint_camera.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.camera_preview.CameraPreview
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.capture.CameraCaptureButton
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.guide.CameraGuideOverlay
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.guide.CameraGuideText
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.guide.CameraStatusText
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.permission.CameraPermissionContent
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.config.CameraAnalysisConfig
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCapturedResult
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraGuideDebugState
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraRealtimeState
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.CameraRealtimeFrameAnalyzer
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture.bindCameraUseCases
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture.captureToFileThenBitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture.handleCapturedBitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture.saveCapturedBitmapsForTest
import timber.log.Timber
import androidx.compose.runtime.DisposableEffect
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.HandLandmarkDetector
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.HandLandmarkOverlay
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.HandLandmarkPoint
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.calculateHandHeightRatio
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.evaluateHandSizeCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.resolveCameraCaptureCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.toCameraConditionMessage

private const val SAVE_CAPTURED_IMAGES_FOR_TEST = true
private const val REALTIME_ANALYSIS_INTERVAL = 5

/**
 * 손바닥 촬영용 커스텀 카메라 화면
 *
 * 기능
 * - CameraX 프리뷰 표시
 * - 손바닥 가이드라인 표시
 * - 실시간 프레임 분석 결과 표시
 * - 촬영 버튼 처리
 * - 촬영 결과를 상위 화면으로 전달
 *
 * @param onCaptured 촬영 완료 결과 콜백
 * @param onCancel 카메라 종료 콜백
 */
@Composable
fun CameraScreen(
    onCaptured: (CameraCapturedResult) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mainHandler = remember {
        Handler(Looper.getMainLooper())
    }

    var hasPermission by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isCapturing by remember { mutableStateOf(false) }

    val previewView = remember { PreviewView(context) }

    var previewWidth by remember { mutableStateOf(0f) }
    var previewHeight by remember { mutableStateOf(0f) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    var debugState by remember { mutableStateOf(CameraGuideDebugState()) }
    var realtimeState by remember { mutableStateOf(CameraRealtimeState()) }

    val conditionConfig = remember {
        CameraAnalysisConfig.conditionConfig
    }

    var handLandmarks by remember {
        mutableStateOf<List<HandLandmarkPoint>>(emptyList())
    }

    var handHeightRatio by remember {
        mutableStateOf<Float?>(null)
    }

    var handSizeCondition by remember {
        mutableStateOf(CameraCaptureCondition.READY)
    }

    val landmarkBasedCondition = resolveCameraCaptureCondition(
        ratioCondition = handSizeCondition,
        blurCondition = realtimeState.blurCondition,
        tiltCondition = realtimeState.tiltCondition,
        config = conditionConfig
    )

    val landmarkBasedMessage = toCameraConditionMessage(
        condition = landmarkBasedCondition
    )


    val handLandmarkDetector = remember {
        HandLandmarkDetector(
            context = context,
            onResult = { result ->
                mainHandler.post {
                    handLandmarks = result.landmarks

                    Timber.tag("HandLandmark").d(
                        "landmarkCount=%d handedness=%s",
                        result.landmarks.size,
                        result.handedness
                    )
                }
            },
            onError = { exception ->
                Timber.tag("HandLandmark").e(
                    exception,
                    "Hand landmark detection failed"
                )
            }
        )
    }

    val realtimeAnalyzer = remember(
        conditionConfig,
        handLandmarkDetector
    ) {
        CameraRealtimeFrameAnalyzer(
            analysisInterval = REALTIME_ANALYSIS_INTERVAL,
            conditionConfig = conditionConfig,
            onBitmapFrameAvailable = { bitmap ->
                handLandmarkDetector.detect(bitmap)
            },
            onFrameAvailable = { frameMetadata ->
                realtimeState = frameMetadata.realtimeState

                Timber.tag("CameraRealtime").d(
                    "frameIndex=%d frame=%dx%d rotation=%d blur=%.1f ratio=%.1f tilt=%.3f condition=%s",
                    frameMetadata.frameIndex,
                    frameMetadata.width,
                    frameMetadata.height,
                    frameMetadata.rotationDegrees,
                    frameMetadata.blurScore,
                    frameMetadata.ratioEstimate,
                    frameMetadata.tiltScore,
                    frameMetadata.realtimeState.condition
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
        if (!hasPermission) {
            return@LaunchedEffect
        }

        runCatching {
            withContext(Dispatchers.Default) {
                handLandmarkDetector.setup()
            }

            bindCameraUseCases(
                context = context,
                lifecycleOwner = lifecycleOwner,
                previewView = previewView,
                analyzer = realtimeAnalyzer
            ) { capture ->
                imageCapture = capture
            }
        }.onFailure { exception ->
            Timber.tag("CameraScreen").e(exception, "Camera bind failed")
            errorMessage = "카메라 초기화에 실패했습니다."
        }
    }

    DisposableEffect(handLandmarkDetector) {
        onDispose {
            handLandmarkDetector.close()
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

        HandLandmarkOverlay(
            landmarks = handLandmarks,
            modifier = Modifier.fillMaxSize(),
            onHandRectChanged = { handRect, guideRect ->
                handHeightRatio = calculateHandHeightRatio(
                    handRect = handRect,
                    guideRect = guideRect
                )

                handSizeCondition = evaluateHandSizeCondition(
                    handRect = handRect,
                    guideRect = guideRect
                )
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

        CameraStatusText(
            text = "landmarks=${handLandmarks.size}",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 160.dp)
        )

        CameraStatusText(
            text = "handRatio=${handHeightRatio?.let { "%.2f".format(it) } ?: "-"} " +
                    "sizeCond=$handSizeCondition",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 200.dp)
        )

        errorMessage?.let { message ->
            CameraStatusText(
                text = message,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp)
            )
        }

        if (errorMessage == null) {
            CameraStatusText(
                text = landmarkBasedMessage,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 120.dp)
            )
        }





//        debugState.lastRatio?.let { lastRatio ->
//            val averageRatio = debugState.averageRatio ?: lastRatio
//
//            CameraStatusText(
//                text = "frame ratio: ${"%.1f".format(lastRatio)}% / avg: " +
//                        "${"%.1f".format(averageRatio)}% (n=${debugState.ratioCount})",
//                modifier = Modifier
//                    .align(Alignment.TopCenter)
//                    .padding(top = 140.dp)
//            )
//        }
//
//        realtimeState.ratioEstimate?.let { ratioEstimate ->
//            CameraStatusText(
//                text = "realtime ratio: ${"%.1f".format(ratioEstimate)}%",
//                modifier = Modifier
//                    .align(Alignment.TopCenter)
//                    .padding(top = 184.dp)
//            )
//        }
//
//        realtimeState.blurScore?.let { blurScore ->
//            CameraStatusText(


//                text = "realtime blur: ${"%.1f".format(blurScore)}",
//                modifier = Modifier
//                    .align(Alignment.TopCenter)
//                    .padding(top = 228.dp)
//            )
//        }
//
//        realtimeState.tiltScore?.let { tiltScore ->
//            CameraStatusText(
//                text = "realtime tilt: ${"%.3f".format(tiltScore)}",
//                modifier = Modifier
//                    .align(Alignment.TopCenter)
//                    .padding(top = 272.dp)
//            )
//        }

//        CameraStatusText(
//            text = "ratioCond=${realtimeState.ratioCondition}, " +
//                    "blurCond=${realtimeState.blurCondition}, " +
//                    "tiltCond=${realtimeState.tiltCondition}",
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 316.dp)
//        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp),
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
                                return@captureToFileThenBitmap
                            }

                            val captureResult = handleCapturedBitmap(
                                originalBitmap = originalBitmap,
                                previewWidth = previewWidth,
                                previewHeight = previewHeight,
                                debugState = debugState,
                                conditionConfig = conditionConfig
                            )

                            debugState = captureResult.updatedDebugState

                            val analysisState = captureResult.capturedResult.analysisState

                            if (analysisState.condition !=
                                com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition.READY
                            ) {
                                errorMessage = analysisState.message
                            } else {
                                errorMessage = analysisState.message
                            }

                            if (SAVE_CAPTURED_IMAGES_FOR_TEST) {
                                saveCapturedBitmapsForTest(
                                    context = context,
                                    result = captureResult.capturedResult
                                )
                            }

                            onCaptured(captureResult.capturedResult)

                            Timber.tag("PalmCrop").d(captureResult.logMessage)
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