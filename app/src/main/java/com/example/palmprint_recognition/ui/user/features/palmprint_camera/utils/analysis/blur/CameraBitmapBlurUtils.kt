package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur

import android.graphics.Bitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper.bitmapToGrayValues
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur.helper.calculateLaplacianVariance
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper.resizeBitmapForAnalysis

/**
 * 촬영 후 Bitmap blur 계산 유틸
 *
 * 역할
 * - 촬영된 Bitmap의 선명도를 계산한다
 */

/**
 * Bitmap의 blur score를 계산한다.
 *
 * 구현 방식
 * - Bitmap을 작은 크기로 축소
 * - grayscale 배열로 변환
 * - Laplacian variance를 계산
 *
 * @param bitmap 분석할 Bitmap
 * @return 선명도 점수
 */
fun calculateBitmapBlurScore(
    bitmap: Bitmap
): Float {
    val resizedBitmap = resizeBitmapForAnalysis(bitmap)

    val width = resizedBitmap.width
    val height = resizedBitmap.height

    if (width < 3 || height < 3) {
        return 0f
    }

    val grayValues = bitmapToGrayValues(resizedBitmap)

    return calculateLaplacianVariance(
        values = grayValues,
        width = width,
        height = height
    )
}