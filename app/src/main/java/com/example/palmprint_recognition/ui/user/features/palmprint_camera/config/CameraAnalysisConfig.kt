package com.example.palmprint_recognition.ui.user.features.palmprint_camera.config

import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.CameraConditionConfig

/**
 * 카메라 분석 관련 중앙 설정
 *
 * 역할
 * - realtime 조건 사용 여부를 관리한다
 * - blur / hand size / tilt / auto capture 기준값을 한 곳에서 관리한다
 */
object CameraAnalysisConfig {

    /**
     * 조건 활성화 설정
     */
    val conditionConfig = CameraConditionConfig(
        useHandSizeCondition = true,
        useBlurCondition = true,
        useTiltCondition = true
    )

    /**
     * 실시간 blur 기준값
     *
     * 값이 낮을수록 blur 판정이 덜 예민해진다.
     */
    const val REALTIME_BLUR_THRESHOLD = 60f

    /**
     * 손 크기 기준값
     *
     * handHeightRatio = hand bounding box 높이 / guide cropRect 높이
     */
    const val HAND_TOO_FAR_HEIGHT_RATIO = 0.70f
    const val HAND_TOO_CLOSE_HEIGHT_RATIO = 0.92f

    /**
     * 손 기울기 기준값
     *
     * index MCP(5)와 pinky MCP(17)를 잇는 선의 각도 기준이다.
     */
    const val HAND_TILT_THRESHOLD_DEGREES = 19f

    /**
     * 자동촬영용 더 엄격한 조건
     *
     * 일반 READY보다 좁은 범위에서만 자동촬영한다.
     */
    const val AUTO_CAPTURE_MIN_HAND_RATIO = 0.76f
    const val AUTO_CAPTURE_MAX_HAND_RATIO = 0.84f
    const val AUTO_CAPTURE_MAX_TILT_DEGREES = 14f
    const val AUTO_CAPTURE_READY_HOLD_MS = 1000L
    const val AUTO_CAPTURE_COOLDOWN_MS = 2000L

    /**
     * realtime 분석 주기
     */
    const val REALTIME_ANALYSIS_INTERVAL = 5

    /**
     * 디버그용 이미지 저장 여부
     */
    const val SAVE_CAPTURED_IMAGES_FOR_TEST = true

    /**
     * 랜드마크 디버그 오버레이 표시 여부
     */
    const val SHOW_LANDMARK_DEBUG_OVERLAY = false
}