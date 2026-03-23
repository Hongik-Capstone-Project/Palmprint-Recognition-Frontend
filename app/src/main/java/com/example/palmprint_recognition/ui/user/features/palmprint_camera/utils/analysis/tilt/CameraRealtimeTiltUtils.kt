package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.tilt

import androidx.camera.core.ImageProxy
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper.sampleCenterLumaRegion
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.tilt.helper.calculateVerticalCenterOffsetRatio
import kotlin.math.max
import kotlin.math.min

private const val REALTIME_TILT_TARGET_WIDTH = 96
private const val REALTIME_TILT_TARGET_HEIGHT = 128

// 중앙부 위주로 조금 더 집중해서 기울기 변화에 민감하게
private const val REALTIME_TILT_CROP_WIDTH_RATIO = 0.50f
private const val REALTIME_TILT_CROP_HEIGHT_RATIO = 0.76f

// tilt를 좀 더 빨리 bad로 보내기 위한 gain
private const val REALTIME_TILT_GAIN = 1.35f

/**
 * 실시간 프리뷰 tilt 계산 유틸
 *
 * 역할
 * - ImageProxy의 Y plane을 사용해 기울기 점수를 계산한다
 * - Bitmap 변환 없이 빠르게 분석한다
 */

/**
 * 실시간 프레임에서 기울기 근사값을 계산한다.
 *
 * 반환값 의미
 * - 0에 가까울수록 수직 정렬이 잘 된 상태
 * - 값이 커질수록 좌우로 기울어진 상태
 *
 * @param image CameraX 프레임
 * @param darkPixelThreshold 손바닥 후보 밝기 기준
 * @return 정규화된 기울기 오프셋 값
 */
fun calculateRealtimeTiltScore(
    image: ImageProxy,
    darkPixelThreshold: Float
): Float {
    val yPlane = image.planes.firstOrNull() ?: return 0f
    val buffer = yPlane.buffer.duplicate()
    buffer.rewind()

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
        targetWidth = REALTIME_TILT_TARGET_WIDTH,
        targetHeight = REALTIME_TILT_TARGET_HEIGHT,
        cropWidthRatio = REALTIME_TILT_CROP_WIDTH_RATIO,
        cropHeightRatio = REALTIME_TILT_CROP_HEIGHT_RATIO
    )

    if (sampledValues.isEmpty()) {
        return 0f
    }

    val adaptiveThreshold = calculateAdaptiveTiltThreshold(
        values = sampledValues,
        fallback = darkPixelThreshold
    )

    val rawOffset = calculateVerticalCenterOffsetRatio(
        values = sampledValues,
        width = REALTIME_TILT_TARGET_WIDTH,
        height = REALTIME_TILT_TARGET_HEIGHT,
        darkPixelThreshold = adaptiveThreshold
    )

    return (rawOffset * REALTIME_TILT_GAIN).coerceIn(0f, 1f)
}

private fun calculateAdaptiveTiltThreshold(
    values: FloatArray,
    fallback: Float
): Float {
    if (values.isEmpty()) {
        return fallback
    }

    var sum = 0f
    for (value in values) {
        sum += value
    }

    val mean = sum / values.size.toFloat()

    // fallback을 유지하되 평균 밝기에 따라 소폭만 보정
    val adaptive = (mean - 14f).coerceIn(145f, 185f)
    return ((fallback * 0.6f) + (adaptive * 0.4f)).coerceIn(140f, 190f)
}