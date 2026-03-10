package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import android.graphics.Bitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition

private const val DEFAULT_TOO_FAR_RATIO_THRESHOLD = 42f
private const val DEFAULT_TOO_CLOSE_RATIO_THRESHOLD = 68f

/**
 * crop 영역 비율 계산 및 판단 관련 유틸
 */

/**
 * 원본 이미지 대비 가이드 crop 영역의 면적 비율을 계산한다.
 *
 * 주의
 * - 손바닥 실제 점유율이 아니라 직사각형 crop 영역 비율이다
 *
 * @param original 원본 Bitmap
 * @param cropped crop된 Bitmap
 * @return 원본 대비 crop 영역 면적 비율(%)
 */
fun calcGuideCropAreaRatioPercent(
    original: Bitmap,
    cropped: Bitmap
): Float {
    val total = original.width.toFloat() * original.height.toFloat()
    val part = cropped.width.toFloat() * cropped.height.toFloat()

    if (total <= 0f) return 0f

    return (part / total) * 100f
}

/**
 * crop 영역 비율을 바탕으로 거리 관련 상태를 판단한다.
 *
 * @param ratio crop 영역 비율(%)
 * @param tooFarThreshold 너무 멀다고 판단하는 하한값
 * @param tooCloseThreshold 너무 가깝다고 판단하는 상한값
 * @return TOO_FAR / TOO_CLOSE / READY
 */
fun evaluateGuideRatioCondition(
    ratio: Float,
    tooFarThreshold: Float = DEFAULT_TOO_FAR_RATIO_THRESHOLD,
    tooCloseThreshold: Float = DEFAULT_TOO_CLOSE_RATIO_THRESHOLD
): CameraCaptureCondition {
    return when {
        ratio < tooFarThreshold -> CameraCaptureCondition.TOO_FAR
        ratio > tooCloseThreshold -> CameraCaptureCondition.TOO_CLOSE
        else -> CameraCaptureCondition.READY
    }
}