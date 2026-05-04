package com.example.palmprint_recognition.ui.user.features.palmprint_camera.config

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

    const val SAVE_CAPTURED_IMAGES_FOR_TEST = true

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