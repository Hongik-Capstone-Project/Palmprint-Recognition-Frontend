package com.example.palmprint_recognition.ui.user.features.palmprint_camera.screens

import android.Manifest
import android.os.Handler
import android.os.Looper
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
import androidx.compose.runtime.DisposableEffect
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
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.HandLandmarkDetector
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.HandLandmarkOverlay
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.HandLandmarkPoint
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.calculateHandHeightRatio
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.calculateHandTiltDegrees
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.evaluateHandSizeCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.evaluateHandTiltCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCapturedResult
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraMode
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraRealtimeState
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.CameraRealtimeFrameAnalyzer
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.resolveCameraCaptureCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.toCameraConditionMessage
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture.bindCameraUseCases
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture.buildPalmCropLogMessage
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture.captureToFileThenBitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture.createCameraCapturedResult
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture.saveCapturedBitmapsForTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.debug.CameraLogger
import androidx.compose.foundation.layout.BoxScope
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.AutoCaptureState
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark.canAutoCaptureByLandmark
import kotlinx.coroutines.delay
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.toAutoCaptureConditionMessage

private const val CAMERA_STATUS_TOP_PADDING = 120
private const val CAMERA_ERROR_TOP_PADDING = 100
private const val CAMERA_GUIDE_TOP_PADDING = 44
private const val CAMERA_BUTTON_BOTTOM_PADDING = 20
private const val DEBUG_LANDMARK_TOP_PADDING = 160
private const val DEBUG_HAND_RATIO_TOP_PADDING = 200
private const val DEBUG_TILT_TOP_PADDING = 240

/**
 * 손바닥 촬영용 커스텀 카메라 화면
 *
 * 기능
 * - CameraX 프리뷰 표시
 * - MediaPipe 손 랜드마크 분석
 * - realtime blur 분석
 * - landmark 기반 거리 / 기울기 조건 표시
 * - 수동촬영 또는 자동촬영 모드 준비
 *
 * @param onCaptured 촬영 완료 결과 콜백
 * @param onCancel 카메라 종료 콜백
 * @param cameraMode 카메라 사용 목적
 * @param isAutoCaptureEnabled 자동촬영 사용 여부
 */
@Composable
fun CameraScreen(
    onCaptured: (CameraCapturedResult) -> Unit,
    onCancel: () -> Unit,
    cameraMode: CameraMode = CameraMode.REGISTER,
    isAutoCaptureEnabled: Boolean = false
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mainHandler = remember {
        Handler(Looper.getMainLooper())
    }

    val conditionConfig = remember {
        CameraAnalysisConfig.conditionConfig
    }

    val previewView = remember {
        PreviewView(context)
    }

    var hasPermission by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var isCapturing by remember {
        mutableStateOf(false)
    }

    var previewWidth by remember {
        mutableStateOf(0f)
    }

    var previewHeight by remember {
        mutableStateOf(0f)
    }

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    var realtimeState by remember {
        mutableStateOf(CameraRealtimeState())
    }

    var handLandmarks by remember {
        mutableStateOf<List<HandLandmarkPoint>>(emptyList())
    }

    var handHeightRatio by remember {
        mutableStateOf<Float?>(null)
    }

    var handSizeCondition by remember {
        mutableStateOf(CameraCaptureCondition.HAND_NOT_DETECTED)
    }

    var handTiltDegrees by remember {
        mutableStateOf<Float?>(null)
    }

    var handTiltCondition by remember {
        mutableStateOf(CameraCaptureCondition.HAND_NOT_DETECTED)
    }

    var autoCaptureState by remember {
        mutableStateOf(
            if (isAutoCaptureEnabled) {
                AutoCaptureState.WAITING
            } else {
                AutoCaptureState.DISABLED
            }
        )
    }


    var lastAutoCapturedAtMs by remember {
        mutableStateOf(0L)
    }

    val finalCondition = resolveCameraCaptureCondition(
        handSizeCondition = handSizeCondition,
        blurCondition = realtimeState.blurCondition,
        tiltCondition = handTiltCondition,
        config = conditionConfig
    )

    val canAutoCapture = canAutoCaptureByLandmark(
        handHeightRatio = handHeightRatio,
        handTiltDegrees = handTiltDegrees,
        finalCondition = finalCondition
    )

    val guideTitleMessage = if (isAutoCaptureEnabled) {
        "손바닥을 가이드라인에 맞추면 자동으로 촬영됩니다."
    } else {
        "손바닥을 가이드라인에 맞춰주세요"
    }

    val finalMessage = if (isAutoCaptureEnabled) {
        toAutoCaptureConditionMessage(
            condition = finalCondition,
            canAutoCapture = canAutoCapture,
            handHeightRatio = handHeightRatio,
            handTiltDegrees = handTiltDegrees,
            minAutoRatio = CameraAnalysisConfig.AUTO_CAPTURE_MIN_HAND_RATIO,
            maxAutoRatio = CameraAnalysisConfig.AUTO_CAPTURE_MAX_HAND_RATIO,
            maxAutoTiltDegrees = CameraAnalysisConfig.AUTO_CAPTURE_MAX_TILT_DEGREES
        )
    } else {
        toCameraConditionMessage(
            condition = finalCondition
        )
    }

    val autoCaptureMessage = when (autoCaptureState) {
        AutoCaptureState.READY_HOLDING,
        AutoCaptureState.CAPTURING -> {
            "자동 촬영 중입니다. 손을 움직이지 마세요."
        }

        else -> null
    }

    val isManualCaptureVisible =
        cameraMode == CameraMode.REGISTER && !isAutoCaptureEnabled

    val handLandmarkDetector = remember {
        HandLandmarkDetector(
            context = context,
            onResult = { result ->
                mainHandler.post {
                    handLandmarks = result.landmarks

                    CameraLogger.logHandLandmarkResult(
                        landmarkCount = result.landmarks.size,
                        handedness = result.handedness
                    )
                }
            },
            onError = { exception ->
                CameraLogger.logHandLandmarkError(
                    throwable = exception
                )
            }
        )
    }

    val realtimeAnalyzer = remember(
        conditionConfig,
        handLandmarkDetector
    ) {
        CameraRealtimeFrameAnalyzer(
            analysisInterval = CameraAnalysisConfig.REALTIME_ANALYSIS_INTERVAL,
            conditionConfig = conditionConfig,
            onBitmapFrameAvailable = { bitmap ->
                handLandmarkDetector.detect(bitmap)
            },
            onFrameAvailable = { frameMetadata ->
                realtimeState = frameMetadata.realtimeState

                CameraLogger.logRealtimeFrame(
                    frameIndex = frameMetadata.frameIndex,
                    width = frameMetadata.width,
                    height = frameMetadata.height,
                    rotationDegrees = frameMetadata.rotationDegrees,
                    blurScore = frameMetadata.blurScore,
                    blurCondition = frameMetadata.realtimeState.blurCondition
                )
            }
        )
    }

    /**
     * 현재 카메라 프레임을 촬영하고 crop 결과를 상위 화면으로 전달한다.
     */
    fun startCapture() {
        val capture = imageCapture

        if (isCapturing) {
            return
        }

        if (capture == null) {
            errorMessage = "카메라가 아직 준비되지 않았습니다."
            return
        }

        if (previewWidth <= 0f || previewHeight <= 0f) {
            errorMessage = "프리뷰 크기를 알 수 없어 촬영할 수 없습니다."
            return
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

                val capturedResult = createCameraCapturedResult(
                    originalBitmap = originalBitmap,
                    previewWidth = previewWidth,
                    previewHeight = previewHeight
                )

                if (CameraAnalysisConfig.SAVE_CAPTURED_IMAGES_FOR_TEST) {
                    saveCapturedBitmapsForTest(
                        context = context,
                        result = capturedResult
                    )
                }

                CameraLogger.logPalmCrop(
                    message = buildPalmCropLogMessage(
                        previewWidth = previewWidth,
                        previewHeight = previewHeight,
                        result = capturedResult
                    )
                )

                onCaptured(capturedResult)
            },
            onFailure = { message ->
                isCapturing = false
                errorMessage = message
            }
        )
    }

    LaunchedEffect(
        isAutoCaptureEnabled,
        canAutoCapture,
        isCapturing,
        imageCapture,
        previewWidth,
        previewHeight
    ) {
        if (!isAutoCaptureEnabled) {
            autoCaptureState = AutoCaptureState.DISABLED
            return@LaunchedEffect
        }

        if (isCapturing) {
            autoCaptureState = AutoCaptureState.CAPTURING
            return@LaunchedEffect
        }

        val now = System.currentTimeMillis()

        val isCooldown =
            now - lastAutoCapturedAtMs < CameraAnalysisConfig.AUTO_CAPTURE_COOLDOWN_MS

        if (isCooldown) {
            autoCaptureState = AutoCaptureState.COOLDOWN
            return@LaunchedEffect
        }

        val isCameraReady =
            imageCapture != null && previewWidth > 0f && previewHeight > 0f

        if (!canAutoCapture || !isCameraReady) {
            autoCaptureState = AutoCaptureState.WAITING
            return@LaunchedEffect
        }

        autoCaptureState = AutoCaptureState.READY_HOLDING

        delay(CameraAnalysisConfig.AUTO_CAPTURE_READY_HOLD_MS)

        lastAutoCapturedAtMs = System.currentTimeMillis()
        autoCaptureState = AutoCaptureState.CAPTURING

        startCapture()
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
            CameraLogger.logCameraBindError(
                throwable = exception
            )
            errorMessage = "카메라 초기화에 실패했습니다."
        }
    }

    LaunchedEffect(handLandmarks) {
        handTiltDegrees = calculateHandTiltDegrees(
            landmarks = handLandmarks
        )

        handTiltCondition = evaluateHandTiltCondition(
            landmarks = handLandmarks
        )
    }

    LaunchedEffect(
        handLandmarks,
        handHeightRatio,
        handSizeCondition,
        handTiltDegrees,
        handTiltCondition
    ) {
        CameraLogger.logCameraDebugStatus(
            landmarkCount = handLandmarks.size,
            handHeightRatio = handHeightRatio,
            handSizeCondition = handSizeCondition,
            handTiltDegrees = handTiltDegrees,
            handTiltCondition = handTiltCondition
        )
    }

    LaunchedEffect(
        autoCaptureState,
        canAutoCapture,
        handHeightRatio,
        handTiltDegrees,
        finalCondition
    ) {
        CameraLogger.logAutoCaptureState(
            state = autoCaptureState,
            canAutoCapture = canAutoCapture,
            handHeightRatio = handHeightRatio,
            handTiltDegrees = handTiltDegrees,
            finalCondition = finalCondition
        )
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
            isDebugVisible = CameraAnalysisConfig.SHOW_LANDMARK_DEBUG_OVERLAY,
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
            text = guideTitleMessage,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = CAMERA_GUIDE_TOP_PADDING.dp)
        )

        errorMessage?.let { message ->
            CameraStatusText(
                text = message,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = CAMERA_ERROR_TOP_PADDING.dp)
            )
        }

        if (errorMessage == null) {
            CameraStatusText(
                text = finalMessage,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = CAMERA_STATUS_TOP_PADDING.dp)
            )
        }

        if (isAutoCaptureEnabled && autoCaptureMessage != null && errorMessage == null) {
            CameraStatusText(
                text = autoCaptureMessage,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 170.dp)
            )
        }


        if (CameraAnalysisConfig.SHOW_LANDMARK_DEBUG_OVERLAY) {
            CameraDebugStatusTextGroup(
                handLandmarks = handLandmarks,
                handHeightRatio = handHeightRatio,
                handSizeCondition = handSizeCondition,
                handTiltDegrees = handTiltDegrees,
                handTiltCondition = handTiltCondition
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = CAMERA_BUTTON_BOTTOM_PADDING.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (isManualCaptureVisible) {
                CameraCaptureButton(
                    enabled = !isCapturing,
                    onClick = {
                        startCapture()
                    }
                )
            }
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


/**
 * 카메라 디버그 상태 텍스트 그룹
 *
 * @param handLandmarks 현재 손 랜드마크 목록
 * @param handHeightRatio 손 높이 비율
 * @param handSizeCondition 손 크기 조건
 * @param handTiltDegrees 손 기울기 각도
 * @param handTiltCondition 손 기울기 조건
 */
@Composable
private fun BoxScope.CameraDebugStatusTextGroup(
    handLandmarks: List<HandLandmarkPoint>,
    handHeightRatio: Float?,
    handSizeCondition: CameraCaptureCondition,
    handTiltDegrees: Float?,
    handTiltCondition: CameraCaptureCondition
) {
    CameraStatusText(
        text = "landmarks=${handLandmarks.size}",
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = DEBUG_LANDMARK_TOP_PADDING.dp)
    )

    CameraStatusText(
        text = "handRatio=${handHeightRatio?.let { "%.2f".format(it) } ?: "-"} " +
                "sizeCond=$handSizeCondition",
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = DEBUG_HAND_RATIO_TOP_PADDING.dp)
    )

    CameraStatusText(
        text = "tilt=${handTiltDegrees?.let { "%.1f".format(it) } ?: "-"} " +
                "tiltCond=$handTiltCondition",
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = DEBUG_TILT_TOP_PADDING.dp)
    )
}