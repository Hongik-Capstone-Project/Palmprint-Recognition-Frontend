package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition

private const val DEFAULT_TOO_FAR_RATIO_THRESHOLD = 42f
private const val DEFAULT_TOO_CLOSE_RATIO_THRESHOLD = 68f

private const val DEFAULT_REALTIME_TOO_FAR_RATIO_THRESHOLD = 32f
private const val DEFAULT_REALTIME_TOO_CLOSE_RATIO_THRESHOLD = 72f
private const val REALTIME_RATIO_TARGET_WIDTH = 96
private const val REALTIME_RATIO_TARGET_HEIGHT = 128
private const val REALTIME_DARK_PIXEL_THRESHOLD = 150

/**
 * crop 영역 비율 계산 및 판단 관련 유틸
 */

/**
 * 원본 이미지 대비 가이드 crop 영역의 면적 비율을 계산한다.
 *
 * @param original 원본 Bitmap
 * @param cropped crop된 Bitmap
 * @return 원본 대비 crop 영역 비율(%)
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

/**
 * 실시간 프리뷰 프레임에서 ratio를 근사 계산한다.
 *
 * @param image CameraX 프레임
 * @return 실시간 ratio 근사값(%)
 */
fun calculateRealtimeRatioEstimate(
    image: ImageProxy
): Float {
    val yPlane = image.planes.firstOrNull() ?: return 0f
    val buffer = yPlane.buffer

    if (!buffer.hasRemaining()) {
        return 0f
    }

    val rowStride = yPlane.rowStride
    val pixelStride = yPlane.pixelStride
    val width = image.width
    val height = image.height

    if (width < 3 || height < 3) return 0f
    if (rowStride <= 0 || pixelStride <= 0) return 0f

    val bytes = ByteArray(buffer.remaining())
    if (bytes.isEmpty()) {
        return 0f
    }

    buffer.get(bytes)

    val sampledLuma = sampleCenterLumaRegionForRatio(
        source = bytes,
        sourceWidth = width,
        sourceHeight = height,
        rowStride = rowStride,
        pixelStride = pixelStride,
        targetWidth = REALTIME_RATIO_TARGET_WIDTH,
        targetHeight = REALTIME_RATIO_TARGET_HEIGHT
    )

    android.util.Log.d(
        "RealtimeRatio",
        "width=$width, height=$height, rowStride=$rowStride, pixelStride=$pixelStride, bytes=${bytes.size}"
    )
    if (sampledLuma.isEmpty()) {
        return 0f
    }

    var darkCount = 0
    val totalCount = sampledLuma.size

    for (value in sampledLuma) {
        if (value <= REALTIME_DARK_PIXEL_THRESHOLD) {
            darkCount++
        }
    }

    if (totalCount == 0) return 0f

    return (darkCount.toFloat() / totalCount.toFloat()) * 100f
}

/**
 * 실시간 ratio 근사값을 바탕으로 거리 상태를 판단한다.
 *
 * @param ratioEstimate 실시간 ratio 근사값(%)
 * @param tooFarThreshold 너무 멀다고 판단하는 하한값
 * @param tooCloseThreshold 너무 가깝다고 판단하는 상한값
 * @return TOO_FAR / TOO_CLOSE / READY
 */
fun evaluateRealtimeRatioCondition(
    ratioEstimate: Float,
    tooFarThreshold: Float = DEFAULT_REALTIME_TOO_FAR_RATIO_THRESHOLD,
    tooCloseThreshold: Float = DEFAULT_REALTIME_TOO_CLOSE_RATIO_THRESHOLD
): CameraCaptureCondition {
    return when {
        ratioEstimate < tooFarThreshold -> CameraCaptureCondition.TOO_FAR
        ratioEstimate > tooCloseThreshold -> CameraCaptureCondition.TOO_CLOSE
        else -> CameraCaptureCondition.READY
    }
}

/**
 * 실시간 ratio 계산을 위해 원본 Y plane의 중앙 영역을 축소 샘플링한다.
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
private fun sampleCenterLumaRegionForRatio(
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