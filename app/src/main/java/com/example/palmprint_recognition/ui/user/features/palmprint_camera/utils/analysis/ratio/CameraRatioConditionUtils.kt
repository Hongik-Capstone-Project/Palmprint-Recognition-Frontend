package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.ratio

import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition

/**
 * ratio 상태 판단 유틸
 *
 * 역할
 * - ratio 값을 기반으로 TOO_FAR / TOO_CLOSE / READY를 판단한다
 */

/**
 * ratio 값을 바탕으로 거리 관련 상태를 판단한다.
 *
 * @param ratio ratio 값(%)
 * @param tooFarThreshold 너무 멀다고 판단하는 하한값
 * @param tooCloseThreshold 너무 가깝다고 판단하는 상한값
 * @return TOO_FAR / TOO_CLOSE / READY
 */
fun evaluateRatioCondition(
    ratio: Float,
    tooFarThreshold: Float,
    tooCloseThreshold: Float
): CameraCaptureCondition {
    return when {
        ratio < tooFarThreshold -> CameraCaptureCondition.TOO_FAR
        ratio > tooCloseThreshold -> CameraCaptureCondition.TOO_CLOSE
        else -> CameraCaptureCondition.READY
    }
}