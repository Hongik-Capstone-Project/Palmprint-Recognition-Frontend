package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.ratio

import androidx.camera.core.ImageProxy
import kotlin.math.max
import kotlin.math.min

private const val REALTIME_RATIO_TARGET_WIDTH = 96
private const val REALTIME_RATIO_TARGET_HEIGHT = 128

private const val REALTIME_RATIO_CROP_WIDTH_RATIO = 0.58f
private const val REALTIME_RATIO_CROP_HEIGHT_RATIO = 0.76f

private const val REALTIME_RATIO_FIXED_DARK_THRESHOLD = 172f
private const val REALTIME_RATIO_ADAPTIVE_WEIGHT = 0.35f

private const val REALTIME_EDGE_BAND_THICKNESS = 10
private const val REALTIME_RATIO_SMOOTHING_ALPHA = 0.30f
private const val REALTIME_EDGE_TOUCH_SCORE_WEIGHT = 0.35f

private var smoothedCenterOccupancyPercent = 0f
private var smoothedEdgeTouchPercent = 0f

/**
 * 실시간 프리뷰 프레임에서 손바닥 거리 판정용 ratio 메트릭을 계산한다.
 *
 * 역할
 * - 중앙 ROI의 손 후보 점유율을 계산한다
 * - ROI 경계 접촉 비율을 계산한다
 * - TOO_FAR와 TOO_CLOSE 판정에 필요한 값을 분리해서 반환한다
 *
 * @param image CameraX 프리뷰 프레임
 * @return 실시간 ratio 메트릭
 */
fun calculateRealtimeRatioMetrics(
    image: ImageProxy
): CameraRealtimeRatioMetrics {
    val emptyMetrics = CameraRealtimeRatioMetrics(
        ratioScore = 0f,
        centerOccupancyPercent = 0f,
        edgeTouchPercent = 0f
    )

    val yPlane = image.planes.firstOrNull() ?: return emptyMetrics
    val buffer = yPlane.buffer.duplicate()

    buffer.rewind()

    if (!buffer.hasRemaining()) {
        return emptyMetrics
    }

    val sourceWidth = image.width
    val sourceHeight = image.height
    val rowStride = yPlane.rowStride
    val pixelStride = yPlane.pixelStride

    if (sourceWidth < 3 || sourceHeight < 3) {
        return emptyMetrics
    }

    if (rowStride <= 0 || pixelStride <= 0) {
        return emptyMetrics
    }

    val sourceBytes = ByteArray(buffer.remaining())
    buffer.get(sourceBytes)

    if (sourceBytes.isEmpty()) {
        return emptyMetrics
    }

    val sampledLumaValues = sampleCenterRegionLumaValues(
        source = sourceBytes,
        sourceWidth = sourceWidth,
        sourceHeight = sourceHeight,
        rowStride = rowStride,
        pixelStride = pixelStride,
        targetWidth = REALTIME_RATIO_TARGET_WIDTH,
        targetHeight = REALTIME_RATIO_TARGET_HEIGHT,
        cropWidthRatio = REALTIME_RATIO_CROP_WIDTH_RATIO,
        cropHeightRatio = REALTIME_RATIO_CROP_HEIGHT_RATIO
    )

    if (sampledLumaValues.isEmpty()) {
        return emptyMetrics
    }

    val darkPixelThreshold = calculateRealtimeDarkPixelThreshold(sampledLumaValues)

    val palmMask = createPalmCandidateMask(
        sampledLumaValues = sampledLumaValues,
        darkPixelThreshold = darkPixelThreshold
    )

    val centerOccupancyPercent = calculateCenterOccupancyPercent(
        palmMask = palmMask,
        width = REALTIME_RATIO_TARGET_WIDTH,
        height = REALTIME_RATIO_TARGET_HEIGHT
    )

    val edgeTouchPercent = calculateEdgeTouchRatioPercent(
        palmMask = palmMask,
        width = REALTIME_RATIO_TARGET_WIDTH,
        height = REALTIME_RATIO_TARGET_HEIGHT
    )

    smoothedCenterOccupancyPercent =
        if (smoothedCenterOccupancyPercent == 0f) {
            centerOccupancyPercent
        } else {
            (smoothedCenterOccupancyPercent * (1f - REALTIME_RATIO_SMOOTHING_ALPHA)) +
                    (centerOccupancyPercent * REALTIME_RATIO_SMOOTHING_ALPHA)
        }

    smoothedEdgeTouchPercent =
        if (smoothedEdgeTouchPercent == 0f) {
            edgeTouchPercent
        } else {
            (smoothedEdgeTouchPercent * (1f - REALTIME_RATIO_SMOOTHING_ALPHA)) +
                    (edgeTouchPercent * REALTIME_RATIO_SMOOTHING_ALPHA)
        }

    val ratioScore = (
            smoothedCenterOccupancyPercent +
                    (smoothedEdgeTouchPercent * REALTIME_EDGE_TOUCH_SCORE_WEIGHT)
            ).coerceIn(0f, 100f)

    return CameraRealtimeRatioMetrics(
        ratioScore = ratioScore,
        centerOccupancyPercent = smoothedCenterOccupancyPercent,
        edgeTouchPercent = smoothedEdgeTouchPercent
    )
}

/**
 * 중앙 ROI를 샘플링하여 밝기 배열로 변환한다.
 *
 * @return targetWidth * targetHeight 크기의 밝기 배열
 */
private fun sampleCenterRegionLumaValues(
    source: ByteArray,
    sourceWidth: Int,
    sourceHeight: Int,
    rowStride: Int,
    pixelStride: Int,
    targetWidth: Int,
    targetHeight: Int,
    cropWidthRatio: Float,
    cropHeightRatio: Float
): FloatArray {
    val cropWidth = max(1, (sourceWidth * cropWidthRatio).toInt())
    val cropHeight = max(1, (sourceHeight * cropHeightRatio).toInt())

    val startX = ((sourceWidth - cropWidth) / 2).coerceAtLeast(0)
    val startY = ((sourceHeight - cropHeight) / 2).coerceAtLeast(0)

    val sampledValues = FloatArray(targetWidth * targetHeight)

    for (targetY in 0 until targetHeight) {
        val sourceY = startY + ((targetY.toFloat() / targetHeight) * cropHeight).toInt()

        for (targetX in 0 until targetWidth) {
            val sourceX = startX + ((targetX.toFloat() / targetWidth) * cropWidth).toInt()
            val sourceIndex = sourceY * rowStride + sourceX * pixelStride
            val safeIndex = min(max(sourceIndex, 0), source.lastIndex)

            sampledValues[(targetY * targetWidth) + targetX] =
                (source[safeIndex].toInt() and 0xFF).toFloat()
        }
    }

    return sampledValues
}

/**
 * 프레임 평균 밝기를 기준으로 손 후보용 임계값을 계산한다.
 *
 * @param sampledLumaValues ROI 밝기 배열
 * @return 실시간 dark pixel threshold
 */
private fun calculateRealtimeDarkPixelThreshold(
    sampledLumaValues: FloatArray
): Float {
    var lumaSum = 0f

    for (value in sampledLumaValues) {
        lumaSum += value
    }

    val averageLuma = lumaSum / sampledLumaValues.size.toFloat()
    val adaptiveThreshold = (averageLuma - 14f).coerceIn(145f, 190f)

    return (
            (REALTIME_RATIO_FIXED_DARK_THRESHOLD * (1f - REALTIME_RATIO_ADAPTIVE_WEIGHT)) +
                    (adaptiveThreshold * REALTIME_RATIO_ADAPTIVE_WEIGHT)
            )
}

/**
 * 밝기 배열을 손바닥 후보 마스크로 변환한다.
 *
 * @param sampledLumaValues ROI 밝기 배열
 * @param darkPixelThreshold 손 후보 판단 임계값
 * @return 손 후보 여부 마스크
 */
private fun createPalmCandidateMask(
    sampledLumaValues: FloatArray,
    darkPixelThreshold: Float
): BooleanArray {
    val palmMask = BooleanArray(sampledLumaValues.size)

    for (index in sampledLumaValues.indices) {
        palmMask[index] = sampledLumaValues[index] <= darkPixelThreshold
    }

    return palmMask
}

/**
 * 중앙 영역 손 후보 점유율을 계산한다.
 *
 * @param palmMask 손 후보 여부 마스크
 * @param width ROI 너비
 * @param height ROI 높이
 * @return 중앙 영역 점유율(%)
 */
private fun calculateCenterOccupancyPercent(
    palmMask: BooleanArray,
    width: Int,
    height: Int
): Float {
    val centerStartX = width / 4
    val centerEndX = width - centerStartX
    val centerStartY = height / 4
    val centerEndY = height - centerStartY

    var centerPixelCount = 0
    var palmPixelCount = 0

    for (y in centerStartY until centerEndY) {
        for (x in centerStartX until centerEndX) {
            centerPixelCount++

            if (palmMask[(y * width) + x]) {
                palmPixelCount++
            }
        }
    }

    if (centerPixelCount == 0) {
        return 0f
    }

    return (palmPixelCount.toFloat() / centerPixelCount.toFloat()) * 100f
}

/**
 * ROI 경계 부근의 손 후보 접촉 비율을 계산한다.
 *
 * @param palmMask 손 후보 여부 마스크
 * @param width ROI 너비
 * @param height ROI 높이
 * @return 경계 접촉 비율(%)
 */
private fun calculateEdgeTouchRatioPercent(
    palmMask: BooleanArray,
    width: Int,
    height: Int
): Float {
    var edgePixelCount = 0
    var touchedEdgePixelCount = 0

    for (y in 0 until height) {
        for (x in 0 until width) {
            val isEdgePixel =
                x < REALTIME_EDGE_BAND_THICKNESS ||
                        x >= width - REALTIME_EDGE_BAND_THICKNESS ||
                        y < REALTIME_EDGE_BAND_THICKNESS ||
                        y >= height - REALTIME_EDGE_BAND_THICKNESS

            if (!isEdgePixel) {
                continue
            }

            edgePixelCount++

            if (palmMask[(y * width) + x]) {
                touchedEdgePixelCount++
            }
        }
    }

    if (edgePixelCount == 0) {
        return 0f
    }

    return (touchedEdgePixelCount.toFloat() / edgePixelCount.toFloat()) * 100f
}