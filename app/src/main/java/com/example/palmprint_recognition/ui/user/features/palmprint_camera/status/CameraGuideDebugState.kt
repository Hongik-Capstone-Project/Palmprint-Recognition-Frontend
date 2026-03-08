package com.example.palmprint_recognition.ui.user.features.palmprint_camera.status

/**
 * 손바닥 가이드 디버그 상태
 *
 * 역할
 * - 최근 crop 비율 저장
 * - 비율 누적합 저장
 * - 평균 계산에 필요한 개수 저장
 */
data class CameraGuideDebugState(
    val lastRatio: Float? = null,
    val ratioSum: Float = 0f,
    val ratioCount: Int = 0
) {

    /**
     * 누적 평균 비율
     */
    val averageRatio: Float?
        get() = if (ratioCount == 0) null else ratioSum / ratioCount

    /**
     * 새로운 비율을 누적한 상태 반환
     *
     * @param ratio 새로 계산된 가이드 영역 비율
     * @return 업데이트된 상태
     */
    fun addRatio(
        ratio: Float
    ): CameraGuideDebugState {
        return copy(
            lastRatio = ratio,
            ratioSum = ratioSum + ratio,
            ratioCount = ratioCount + 1
        )
    }
}