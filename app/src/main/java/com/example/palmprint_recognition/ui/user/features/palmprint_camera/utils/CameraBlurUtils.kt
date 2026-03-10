package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import android.graphics.Bitmap
import android.graphics.Color
import androidx.camera.core.ImageProxy
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition
import kotlin.math.max

private const val DEFAULT_BITMAP_BLUR_THRESHOLD = 120f
private const val DEFAULT_REALTIME_BLUR_THRESHOLD = 80f
private const val MAX_ANALYSIS_SIZE = 256
private const val REALTIME_ANALYSIS_TARGET_WIDTH = 96
private const val REALTIME_ANALYSIS_TARGET_HEIGHT = 128

/**
 * Blur score 계산 및 판단 관련 유틸
 *
 * 역할
 * - 촬영 후 Bitmap 기반 blur 계산
 * - 실시간 ImageProxy 기반 blur 계산
 * - blur threshold 판단
 */

/**
 * Bitmap의 blur score를 계산한다.
 *
 * 구현 방식
 * - Bitmap을 작은 크기로 축소
 * - grayscale 변환
 * - 3x3 Laplacian 값의 분산(variance) 계산
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

    return calculateLaplacianVariance(
        values = gray,
        width = width,
        height = height
    )
}

/**
 * Bitmap blur score를 바탕으로 흐림 상태를 판단한다.
 *
 * @param blurScore 선명도 점수
 * @param blurThreshold 최소 선명도 기준값
 * @return TOO_BLURRY / READY
 */
fun evaluateBlurCondition(
    blurScore: Float,
    blurThreshold: Float = DEFAULT_BITMAP_BLUR_THRESHOLD
): CameraCaptureCondition {
    return if (blurScore < blurThreshold) {
        CameraCaptureCondition.TOO_BLURRY
    } else {
        CameraCaptureCondition.READY
    }
}

/**
 * ImageProxy의 Y plane을 이용해 실시간 blur score를 계산한다.
 *
 * 구현 방식
 * - Y plane(밝기 채널)만 사용한다
 * - 중앙 영역을 작은 해상도로 샘플링한다
 * - 3x3 Laplacian 기반 분산값을 계산한다
 *
 * 주의
 * - 실시간 분석용이므로 전체 프레임을 Bitmap으로 변환하지 않는다
 * - 계산 비용을 줄이기 위해 축소 샘플링한다
 *
 * @param image CameraX 프레임
 * @return 실시간 blur score
 */
fun calculateRealtimeBlurScore(
    image: ImageProxy
): Float {
    val yPlane = image.planes.firstOrNull() ?: return 0f
    val buffer = yPlane.buffer
    val rowStride = yPlane.rowStride
    val pixelStride = yPlane.pixelStride

    val width = image.width
    val height = image.height

    if (!buffer.hasRemaining()) return 0f

    if (width < 3 || height < 3) return 0f

    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)

    if (bytes.isEmpty()) return 0f

    val sampledLuma = sampleCenterLumaRegion(
        source = bytes,
        sourceWidth = width,
        sourceHeight = height,
        rowStride = rowStride,
        pixelStride = pixelStride,
        targetWidth = REALTIME_ANALYSIS_TARGET_WIDTH,
        targetHeight = REALTIME_ANALYSIS_TARGET_HEIGHT
    )

    return calculateLaplacianVariance(
        values = sampledLuma,
        width = REALTIME_ANALYSIS_TARGET_WIDTH,
        height = REALTIME_ANALYSIS_TARGET_HEIGHT
    )
}

/**
 * 실시간 blur score를 바탕으로 흐림 상태를 판단한다.
 *
 * 현재 단계
 * - ratio 근사는 아직 하지 않으므로
 * - TOO_BLURRY 또는 READY만 반환한다
 *
 * @param blurScore 계산된 blur score
 * @param blurThreshold 선명도 최소 기준값
 * @return 실시간 촬영 상태
 */
fun evaluateRealtimeBlurCondition(
    blurScore: Float,
    blurThreshold: Float = DEFAULT_REALTIME_BLUR_THRESHOLD
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

/**
 * 원본 Y plane에서 중앙 영역을 축소 샘플링한다.
 *
 * 이유
 * - 전체 프레임을 다 보지 않고 중앙 중심부만 분석해도 blur 판단에는 충분하다
 * - 계산량을 줄일 수 있다
 *
 * @param source Y plane 바이트 배열
 * @param sourceWidth 원본 프레임 너비
 * @param sourceHeight 원본 프레임 높이
 * @param rowStride Y plane row stride
 * @param pixelStride Y plane pixel stride
 * @param targetWidth 샘플링 결과 너비
 * @param targetHeight 샘플링 결과 높이
 * @return 축소된 밝기 배열
 */
private fun sampleCenterLumaRegion(
    source: ByteArray,
    sourceWidth: Int,
    sourceHeight: Int,
    rowStride: Int,
    pixelStride: Int,
    targetWidth: Int,
    targetHeight: Int
): FloatArray {
    val result = FloatArray(targetWidth * targetHeight)

    val cropWidth = (sourceWidth * 0.7f).toInt().coerceAtLeast(targetWidth)
    val cropHeight = (sourceHeight * 0.7f).toInt().coerceAtLeast(targetHeight)

    val startX = ((sourceWidth - cropWidth) / 2).coerceAtLeast(0)
    val startY = ((sourceHeight - cropHeight) / 2).coerceAtLeast(0)

    for (targetY in 0 until targetHeight) {
        for (targetX in 0 until targetWidth) {
            val srcX = startX + (targetX * cropWidth / targetWidth)
            val srcY = startY + (targetY * cropHeight / targetHeight)

            val safeX = srcX.coerceIn(0, sourceWidth - 1)
            val safeY = srcY.coerceIn(0, sourceHeight - 1)

            val sourceIndex = safeY * rowStride + safeX * pixelStride
            val value = source[sourceIndex].toInt() and 0xFF

            result[targetY * targetWidth + targetX] = value.toFloat()
        }
    }

    return result
}

/**
 * 3x3 Laplacian 기반 분산값을 계산한다.
 *
 * 값 해석
 * - 값이 클수록 선명
 * - 값이 작을수록 흐림
 *
 * @param values 밝기 배열
 * @param width 배열 너비
 * @param height 배열 높이
 * @return 분산 기반 blur score
 */
private fun calculateLaplacianVariance(
    values: FloatArray,
    width: Int,
    height: Int
): Float {
    var sum = 0f
    var sumSquared = 0f
    var count = 0

    for (y in 1 until height - 1) {
        for (x in 1 until width - 1) {
            val center = values[y * width + x]
            val top = values[(y - 1) * width + x]
            val bottom = values[(y + 1) * width + x]
            val left = values[y * width + (x - 1)]
            val right = values[y * width + (x + 1)]

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