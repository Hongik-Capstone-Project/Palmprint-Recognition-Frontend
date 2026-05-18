package com.example.palmprint_recognition.ui.user.features.palmprint_camera.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.CameraConditionConfig

/**
 * 카메라 분석 관련 중앙 설정
 *
 * 역할
 * - realtime 조건 사용 여부를 관리한다.
 * - blur / hand size / tilt / auto capture 기준값을 한 곳에서 관리한다.
 * - 디버그 표시와 로그 출력을 제어한다.
 */
object CameraAnalysisConfig {

    val conditionConfig = CameraConditionConfig(
        useHandSizeCondition = true,
        useBlurCondition = true,
        useTiltCondition = true
    )

    /**
     * 자동촬영 기본값
     *
     * 1단계:
     * - 여기 값을 false / true로 바꾸면 앱 전체 기본 자동촬영 여부가 바뀐다.
     *
     * 2단계:
     * - DemoGuideScreen의 스위치에서 이 값을 직접 변경한다.
     */
    var isAutoCaptureEnabled by mutableStateOf(false)

    /**
     * 유사도 정보 출력 여부
     *
     * true:
     * - 인증 결과 화면에 유사도 점수를 표시한다.
     *
     * false:
     * - 인증 결과 화면에 유사도 점수를 표시하지 않는다.
     */
    var isSimilarityScoreVisible by mutableStateOf(false)

    const val REALTIME_BLUR_THRESHOLD = 60f

    const val HAND_TOO_FAR_HEIGHT_RATIO = 0.70f
    const val HAND_TOO_CLOSE_HEIGHT_RATIO = 0.92f

    const val HAND_TILT_THRESHOLD_DEGREES = 17f

    const val AUTO_CAPTURE_MIN_HAND_RATIO = 0.75f
    const val AUTO_CAPTURE_MAX_HAND_RATIO = 0.85f
    const val AUTO_CAPTURE_MAX_TILT_DEGREES = 16f
    const val AUTO_CAPTURE_READY_HOLD_MS = 1000L
    const val AUTO_CAPTURE_COOLDOWN_MS = 2000L

    const val REALTIME_ANALYSIS_INTERVAL = 5

    const val SAVE_CAPTURED_IMAGES_FOR_TEST = false

    /**
     * 랜드마크 디버그 오버레이 표시 여부
     *
     * false여도 landmark 계산은 계속 수행한다.
     */
    const val SHOW_LANDMARK_DEBUG_OVERLAY = false

    /**
     * 카메라 디버그 로그 출력 여부
     */
    const val ENABLE_CAMERA_DEBUG_LOG = true
}