package com.example.palmprint_recognition.ui.user.features.palmprint_camera.config

import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.CameraConditionConfig

/**
 * 카메라 분석 관련 중앙 설정
 *
 * 역할
 * - 테스트 중 사용할 condition on/off를 관리한다
 * - blur, ratio, tilt threshold를 중앙에서 관리한다
 *
 * 사용 방법
 * - threshold를 변경하고 싶다면 이 파일만 수정한다
 * - 특정 조건을 끄고 싶다면 conditionConfig에서 false로 변경한다
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
     *
     * 값이 낮을수록 덜 예민하게 판정한다.
     */
    const val BITMAP_BLUR_THRESHOLD = 95f

    /**
     * 실시간 blur 기준값
     *
     * 값이 낮을수록 덜 예민하게 판정한다.
     */
    const val REALTIME_BLUR_THRESHOLD = 60f

    /**
     * 촬영 후 ratio 기준값
     *
     * - TOO_FAR는 낮출수록 덜 예민해진다
     * - TOO_CLOSE는 높일수록 덜 예민해진다
     */
    const val TOO_FAR_RATIO_THRESHOLD = 34f
    const val TOO_CLOSE_RATIO_THRESHOLD = 78f

    /**
     * 실시간 ratio 기준값
     *
     */
    const val REALTIME_TOO_FAR_RATIO_THRESHOLD = 48f
    const val REALTIME_TOO_CLOSE_RATIO_THRESHOLD = 100f

    /**
     * tilt 기준값
     *
     * - DARK_PIXEL_THRESHOLD를 높이면 손 영역을 더 넓게 잡는다
     * - TILT_OFFSET_THRESHOLD를 낮추면 더 예민하게 기울기를 감지한다
     */
    const val BITMAP_TILT_DARK_PIXEL_THRESHOLD = 165f
    const val REALTIME_TILT_DARK_PIXEL_THRESHOLD = 170f
    const val TILT_OFFSET_THRESHOLD = 0.13f
}