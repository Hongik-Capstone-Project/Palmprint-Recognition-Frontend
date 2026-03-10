package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition
import kotlin.math.max
import kotlin.math.min

private const val DEFAULT_BLUR_THRESHOLD = 120f
private const val MAX_ANALYSIS_SIZE = 256

/**
 * Blur score 계산 및 판단 관련 유틸
 *
 * 구현 방식
 * - Bitmap을 작은 크기로 축소
 * - grayscale 변환
 * - 3x3 Laplacian 값의 분산(variance) 계산
 * - 값이 클수록 선명하고, 작을수록 흐리다
 */

/**
 * Bitmap의 blur score를 계산한다.
 *
 * @param bitmap 분석할 Bitmap
 * @return 선명도 점수
 */
fun calculateBlurScore(
    bitmap: Bitmap
): Float {
    val resizedBitmap = resizeBitmapForAnalysis(bitmap)

    val width = resizedBitmap.width
    val height = resizedBitmap.height

    if (width < 3 || height < 3) return 0f

    val pixels = IntArray(width * height)
    resizedBitmap.getPixels(
        pixels,
        0,
        width,
        0,
        0,
        width,
        height
    )

    val gray = FloatArray(width * height)

    for (index in pixels.indices) {
        val pixel = pixels[index]
        val red = Color.red(pixel)
        val green = Color.green(pixel)
        val blue = Color.blue(pixel)

        gray[index] = 0.299f * red + 0.587f * green + 0.114f * blue
    }

    var sum = 0f
    var sumSquared = 0f
    var count = 0

    for (y in 1 until height - 1) {
        for (x in 1 until width - 1) {
            val center = gray[y * width + x]
            val top = gray[(y - 1) * width + x]
            val bottom = gray[(y + 1) * width + x]
            val left = gray[y * width + (x - 1)]
            val right = gray[y * width + (x + 1)]

            val laplacian = top + bottom + left + right - 4f * center

            sum += laplacian
            sumSquared += laplacian * laplacian
            count++
        }
    }

    if (count == 0) return 0f

    val mean = sum / count
    val variance = (sumSquared / count) - (mean * mean)

    return max(0f, variance)
}

/**
 * blur score를 바탕으로 흐림 상태를 판단한다.
 *
 * @param blurScore 선명도 점수
 * @param blurThreshold 최소 선명도 기준값
 * @return TOO_BLURRY / READY
 */
fun evaluateBlurCondition(
    blurScore: Float,
    blurThreshold: Float = DEFAULT_BLUR_THRESHOLD
): CameraCaptureCondition {
    return if (blurScore < blurThreshold) {
        CameraCaptureCondition.TOO_BLURRY
    } else {
        CameraCaptureCondition.READY
    }
}

/**
 * 분석 성능을 위해 Bitmap 크기를 줄인다.
 *
 * @param bitmap 원본 Bitmap
 * @return 분석용 축소 Bitmap
 */
private fun resizeBitmapForAnalysis(
    bitmap: Bitmap
): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    val longerSide = max(width, height)

    if (longerSide <= MAX_ANALYSIS_SIZE) {
        return bitmap
    }

    val scale = MAX_ANALYSIS_SIZE.toFloat() / longerSide.toFloat()

    val targetWidth = max(1, (width * scale).toInt())
    val targetHeight = max(1, (height * scale).toInt())

    return Bitmap.createScaledBitmap(
        bitmap,
        targetWidth,
        targetHeight,
        true
    )
}