package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.tilt

import android.graphics.Bitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper.bitmapToGrayValues
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper.resizeBitmapForAnalysis
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.tilt.helper.calculateVerticalCenterOffsetRatio

/**
 * 촬영 후 Bitmap tilt 계산 유틸
 *
 * 역할
 * - 촬영된 Bitmap의 기울기 점수를 계산한다
 */

/**
 * Bitmap의 기울기 점수를 계산한다.
 *
 * 구현 방식
 * - Bitmap을 작은 크기로 축소한다
 * - grayscale 배열로 변환한다
 * - 위쪽/아래쪽 중심 x 차이로 기울기를 근사한다
 *
 * @param bitmap 분석할 Bitmap
 * @param darkPixelThreshold 손바닥 후보 밝기 기준
 * @param maxAnalysisSize 분석용 최대 길이
 * @return 정규화된 기울기 점수
 */
fun calculateBitmapTiltScore(
    bitmap: Bitmap,
    darkPixelThreshold: Float,
    maxAnalysisSize: Int = 256
): Float {
    val resizedBitmap = resizeBitmapForAnalysis(
        bitmap = bitmap,
        maxAnalysisSize = maxAnalysisSize
    )

    val width = resizedBitmap.width
    val height = resizedBitmap.height

    if (width < 3 || height < 3) {
        return 0f
    }

    val grayValues = bitmapToGrayValues(resizedBitmap)

    return calculateVerticalCenterOffsetRatio(
        values = grayValues,
        width = width,
        height = height,
        darkPixelThreshold = darkPixelThreshold
    )
}