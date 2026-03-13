package com.example.palmprint_recognition.ui.user.features.palmprint_camera.config

import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.CameraConditionConfig

/**
 * 카메라 분석 관련 중앙 설정
 *
 * 역할
 * - 테스트 중 사용할 condition on/off 관리
 * - blur / ratio / tilt threshold 관리
 *
 * 사용 방법
 * - threshold를 바꾸고 싶으면 이 파일만 수정한다
 * - 특정 조건을 끄고 싶으면 conditionConfig에서 false로 바꾼다
 */
object CameraAnalysisConfig {

    /**
     * 조건 활성화 설정
     */
    val conditionConfig = CameraConditionConfig(
        useRatioCondition = true,
        useBlurCondition = true,
        useTiltCondition = true
    )

    /**
     * 촬영 후 blur 기준값
     */
    const val BITMAP_BLUR_THRESHOLD = 120f

    /**
     * 실시간 blur 기준값
     */
    const val REALTIME_BLUR_THRESHOLD = 80f

    /**
     * 촬영 후 ratio 기준값
     */
    const val TOO_FAR_RATIO_THRESHOLD = 42f
    const val TOO_CLOSE_RATIO_THRESHOLD = 68f

    /**
     * 실시간 ratio 기준값
     */
    const val REALTIME_TOO_FAR_RATIO_THRESHOLD = 32f
    const val REALTIME_TOO_CLOSE_RATIO_THRESHOLD = 72f

    /**
     * tilt 기준값
     *
     * 값이 작을수록 더 엄격하게 기울기를 잡는다.
     */
    const val BITMAP_TILT_DARK_PIXEL_THRESHOLD = 150f
    const val REALTIME_TILT_DARK_PIXEL_THRESHOLD = 150f
    const val TILT_OFFSET_THRESHOLD = 0.10f
}