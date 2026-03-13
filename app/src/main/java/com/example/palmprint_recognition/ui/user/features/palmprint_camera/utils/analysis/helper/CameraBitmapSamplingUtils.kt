package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper

import android.graphics.Bitmap
import kotlin.math.max

private const val DEFAULT_MAX_BITMAP_ANALYSIS_SIZE = 256

/**
 * Bitmap 분석용 샘플링 유틸
 *
 * 역할
 * - Bitmap 분석 전에 크기를 줄여 계산 비용을 낮춘다
 */

/**
 * Bitmap 분석 성능을 위해 크기를 줄인다.
 *
 * @param bitmap 원본 Bitmap
 * @param maxAnalysisSize 분석에 사용할 최대 길이
 * @return 분석용 축소 Bitmap
 */
fun resizeBitmapForAnalysis(
    bitmap: Bitmap,
    maxAnalysisSize: Int = DEFAULT_MAX_BITMAP_ANALYSIS_SIZE
): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    val longerSide = max(width, height)

    if (longerSide <= maxAnalysisSize) {
        return bitmap
    }

    val scale = maxAnalysisSize.toFloat() / longerSide.toFloat()

    val targetWidth = max(1, (width * scale).toInt())
    val targetHeight = max(1, (height * scale).toInt())

    return Bitmap.createScaledBitmap(
        bitmap,
        targetWidth,
        targetHeight,
        true
    )
}