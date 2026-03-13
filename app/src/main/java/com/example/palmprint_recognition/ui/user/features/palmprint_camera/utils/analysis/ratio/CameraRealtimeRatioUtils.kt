package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.ratio

import androidx.camera.core.ImageProxy
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper.sampleCenterLumaRegion

private const val REALTIME_RATIO_TARGET_WIDTH = 96
private const val REALTIME_RATIO_TARGET_HEIGHT = 128
private const val REALTIME_RATIO_CROP_WIDTH_RATIO = 0.55f
private const val REALTIME_RATIO_CROP_HEIGHT_RATIO = 0.70f
private const val REALTIME_DARK_PIXEL_THRESHOLD = 150f

/**
 * 실시간 프리뷰 ratio 계산 유틸
 *
 * 역할
 * - ImageProxy의 Y plane을 사용해 ratio 근사값을 계산한다
 * - 중앙 영역의 어두운 픽셀 비율을 기반으로 손바닥 점유율을 추정한다
 */

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

    val sampledValues = sampleCenterLumaRegion(
        source = bytes,
        sourceWidth = sourceWidth,
        sourceHeight = sourceHeight,
        rowStride = rowStride,
        pixelStride = pixelStride,
        targetWidth = REALTIME_RATIO_TARGET_WIDTH,
        targetHeight = REALTIME_RATIO_TARGET_HEIGHT,
        cropWidthRatio = REALTIME_RATIO_CROP_WIDTH_RATIO,
        cropHeightRatio = REALTIME_RATIO_CROP_HEIGHT_RATIO
    )

    if (sampledValues.isEmpty()) {
        return 0f
    }

    return calculateDarkPixelRatioPercent(
        values = sampledValues,
        darkPixelThreshold = REALTIME_DARK_PIXEL_THRESHOLD
    )
}

/**
 * 밝기 배열에서 어두운 픽셀 비율을 계산한다.
 *
 * @param values 밝기 배열
 * @param darkPixelThreshold 어두운 픽셀 판단 기준
 * @return 어두운 픽셀 비율(%)
 */
private fun calculateDarkPixelRatioPercent(
    values: FloatArray,
    darkPixelThreshold: Float
): Float {
    if (values.isEmpty()) {
        return 0f
    }

    var darkPixelCount = 0

    for (value in values) {
        if (value <= darkPixelThreshold) {
            darkPixelCount++
        }
    }

    return (darkPixelCount.toFloat() / values.size.toFloat()) * 100f
}