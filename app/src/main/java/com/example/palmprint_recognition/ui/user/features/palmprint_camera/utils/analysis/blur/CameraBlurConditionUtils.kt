package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur

import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition

/**
 * blur 상태 판단 유틸
 *
 * 역할
 * - blur score를 기반으로 촬영 가능 상태를 판단한다
 */

/**
 * blur score를 바탕으로 흐림 상태를 판단한다.
 *
 * @param blurScore 선명도 점수
 * @param blurThreshold 최소 선명도 기준값
 * @return TOO_BLURRY / READY
 */
fun evaluateBlurCondition(
    blurScore: Float,
    blurThreshold: Float
): CameraCaptureCondition {
    return if (blurScore < blurThreshold) {
        CameraCaptureCondition.TOO_BLURRY
    } else {
        CameraCaptureCondition.READY
    }
}