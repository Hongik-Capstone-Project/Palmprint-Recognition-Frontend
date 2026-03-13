package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.ratio

import android.graphics.Bitmap

/**
 * 촬영 후 Bitmap ratio 계산 유틸
 *
 * 역할
 * - 원본 이미지 대비 crop 이미지 면적 비율을 계산한다
 */

/**
 * 원본 이미지 대비 가이드 crop 영역의 면적 비율을 계산한다.
 *
 * @param original 원본 Bitmap
 * @param cropped crop된 Bitmap
 * @return 원본 대비 crop 영역 비율(%)
 */
fun calculateBitmapRatioPercent(
    original: Bitmap,
    cropped: Bitmap
): Float {
    val totalArea = original.width.toFloat() * original.height.toFloat()
    val croppedArea = cropped.width.toFloat() * cropped.height.toFloat()

    if (totalArea <= 0f) {
        return 0f
    }

    return (croppedArea / totalArea) * 100f
}