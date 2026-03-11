package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import androidx.camera.core.ImageProxy
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition
import kotlin.math.abs
import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.max

private const val MAX_TILT_BITMAP_ANALYSIS_SIZE = 256
private const val BITMAP_TILT_DARK_PIXEL_THRESHOLD = 150f
private const val REALTIME_TILT_TARGET_WIDTH = 96
private const val REALTIME_TILT_TARGET_HEIGHT = 128
private const val REALTIME_TILT_DARK_PIXEL_THRESHOLD = 150f
private const val DEFAULT_TILT_OFFSET_THRESHOLD_RATIO = 0.10f

/**
 * 손바닥 기울기 계산 관련 유틸
 *
 * 역할
 * - 실시간 프리뷰 프레임에서 기울기 근사값을 계산한다
 * - 기준값을 넘으면 TILT_BAD 상태로 판단한다
 *
 * 구현 방식
 * - Y plane 중앙 영역을 축소 샘플링한다
 * - 어두운 픽셀을 손바닥 후보로 본다
 * - 위쪽 절반 / 아래쪽 절반의 중심 x 좌표 차이로 기울기를 근사한다
 */


/**
 * 촬영 후 Bitmap의 기울기 점수를 계산한다.
 *
 * 구현 방식
 * - Bitmap을 작은 크기로 축소
 * - grayscale 기준으로 어두운 픽셀을 손바닥 후보로 본다
 * - 위쪽 절반 / 아래쪽 절반 중심 x 차이로 기울기를 근사한다
 *
 * @param bitmap 분석할 Bitmap
 * @return 정규화된 기울기 점수
 */
fun calculateTiltScore(
    bitmap: Bitmap
): Float {
    val resizedBitmap = resizeBitmapForTiltAnalysis(bitmap)

    val width = resizedBitmap.width
    val height = resizedBitmap.height

    if (width < 3 || height < 3) {
        return 0f
    }

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

    val grayValues = FloatArray(width * height)

    for (index in pixels.indices) {
        val pixel = pixels[index]
        val red = Color.red(pixel)
        val green = Color.green(pixel)
        val blue = Color.blue(pixel)

        grayValues[index] = 0.299f * red + 0.587f * green + 0.114f * blue
    }

    return calculateVerticalCenterOffsetRatio(
        values = grayValues,
        width = width,
        height = height,
        darkPixelThreshold = BITMAP_TILT_DARK_PIXEL_THRESHOLD
    )
}

/**
 * tilt 분석 성능을 위해 Bitmap 크기를 줄인다.
 *
 * @param bitmap 원본 Bitmap
 * @return 분석용 축소 Bitmap
 */
private fun resizeBitmapForTiltAnalysis(
    bitmap: Bitmap
): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    val longerSide = max(width, height)

    if (longerSide <= MAX_TILT_BITMAP_ANALYSIS_SIZE) {
        return bitmap
    }

    val scale = MAX_TILT_BITMAP_ANALYSIS_SIZE.toFloat() / longerSide.toFloat()

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
 * 실시간 프레임에서 기울기 근사값을 계산한다.
 *
 * 반환값 의미
 * - 0에 가까울수록 수직 정렬이 잘 된 상태
 * - 값이 커질수록 좌우로 기울어진 상태
 *
 * @param image CameraX 프레임
 * @return 정규화된 기울기 오프셋 값(0.0 ~ 1.0 근처)
 */
fun calculateRealtimeTiltScore(
    image: ImageProxy
): Float {
    val yPlane = image.planes.firstOrNull() ?: return 0f
    val buffer = yPlane.buffer

    if (!buffer.hasRemaining()) {
        return 0f
    }

    val sourceWidth = image.width
    val sourceHeight = image.height
    val rowStride = yPlane.rowStride
    val pixelStride = yPlane.pixelStride

    if (sourceWidth < 3 || sourceHeight < 3) {
        return 0f
    }

    if (rowStride <= 0 || pixelStride <= 0) {
        return 0f
    }

    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)

    if (bytes.isEmpty()) {
        return 0f
    }

    val sampledValues = sampleCenterLumaRegionForTilt(
        source = bytes,
        sourceWidth = sourceWidth,
        sourceHeight = sourceHeight,
        rowStride = rowStride,
        pixelStride = pixelStride,
        targetWidth = REALTIME_TILT_TARGET_WIDTH,
        targetHeight = REALTIME_TILT_TARGET_HEIGHT
    )

    if (sampledValues.isEmpty()) {
        return 0f
    }

    return calculateVerticalCenterOffsetRatio(
        values = sampledValues,
        width = REALTIME_TILT_TARGET_WIDTH,
        height = REALTIME_TILT_TARGET_HEIGHT,
        darkPixelThreshold = REALTIME_TILT_DARK_PIXEL_THRESHOLD
    )
}

/**
 * 기울기 점수를 바탕으로 tilt 상태를 판단한다.
 *
 * @param tiltScore 기울기 점수
 * @param tiltOffsetThreshold 기울기 허용 기준값
 * @return TILT_BAD / READY
 */
fun evaluateTiltCondition(
    tiltScore: Float,
    tiltOffsetThreshold: Float = DEFAULT_TILT_OFFSET_THRESHOLD_RATIO
): CameraCaptureCondition {
    return if (tiltScore > tiltOffsetThreshold) {
        CameraCaptureCondition.TILT_BAD
    } else {
        CameraCaptureCondition.READY
    }
}

/**
 * 중앙 영역의 Y plane을 축소 샘플링한다.
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
private fun sampleCenterLumaRegionForTilt(
    source: ByteArray,
    sourceWidth: Int,
    sourceHeight: Int,
    rowStride: Int,
    pixelStride: Int,
    targetWidth: Int,
    targetHeight: Int
): FloatArray {
    if (source.isEmpty()) {
        return FloatArray(0)
    }

    val result = FloatArray(targetWidth * targetHeight)

    val cropWidth = (sourceWidth * 0.55f).toInt().coerceAtLeast(targetWidth)
    val cropHeight = (sourceHeight * 0.70f).toInt().coerceAtLeast(targetHeight)

    val startX = ((sourceWidth - cropWidth) / 2).coerceAtLeast(0)
    val startY = ((sourceHeight - cropHeight) / 2).coerceAtLeast(0)

    for (targetY in 0 until targetHeight) {
        for (targetX in 0 until targetWidth) {
            val srcX = startX + (targetX * cropWidth / targetWidth)
            val srcY = startY + (targetY * cropHeight / targetHeight)

            val safeX = srcX.coerceIn(0, sourceWidth - 1)
            val safeY = srcY.coerceIn(0, sourceHeight - 1)

            val sourceIndex = safeY * rowStride + safeX * pixelStride

            val value = if (sourceIndex in source.indices) {
                source[sourceIndex].toInt() and 0xFF
            } else {
                255
            }

            result[targetY * targetWidth + targetX] = value.toFloat()
        }
    }

    return result
}

/**
 * 위쪽 절반과 아래쪽 절반의 중심 x 차이를 비율로 계산한다.
 *
 * 계산 방식
 * - threshold 이하 픽셀을 손바닥 후보로 본다
 * - 위쪽 절반의 평균 x
 * - 아래쪽 절반의 평균 x
 * - 두 평균 x 차이를 width로 나눠 정규화한다
 *
 * @param values 밝기 배열
 * @param width 배열 너비
 * @param height 배열 높이
 * @param darkPixelThreshold 손바닥 후보 밝기 기준
 * @return 정규화된 중심 x 차이
 */
private fun calculateVerticalCenterOffsetRatio(
    values: FloatArray,
    width: Int,
    height: Int,
    darkPixelThreshold: Float
): Float {
    var topXSum = 0f
    var topCount = 0

    var bottomXSum = 0f
    var bottomCount = 0

    val middleY = height / 2

    for (y in 0 until height) {
        for (x in 0 until width) {
            val value = values[y * width + x]

            if (value > darkPixelThreshold) {
                continue
            }

            if (y < middleY) {
                topXSum += x.toFloat()
                topCount++
            } else {
                bottomXSum += x.toFloat()
                bottomCount++
            }
        }
    }

    if (topCount == 0 || bottomCount == 0) {
        return 0f
    }

    val topCenterX = topXSum / topCount.toFloat()
    val bottomCenterX = bottomXSum / bottomCount.toFloat()

    return abs(topCenterX - bottomCenterX) / width.toFloat()
}