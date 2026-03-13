package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur

import androidx.camera.core.ImageProxy
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur.helper.calculateLaplacianVariance
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper.sampleCenterLumaRegion

private const val REALTIME_ANALYSIS_TARGET_WIDTH = 96
private const val REALTIME_ANALYSIS_TARGET_HEIGHT = 128
private const val REALTIME_CROP_WIDTH_RATIO = 0.70f
private const val REALTIME_CROP_HEIGHT_RATIO = 0.70f

/**
 * 실시간 프리뷰 blur 계산 유틸
 *
 * 역할
 * - ImageProxy의 Y plane을 사용해 blur score를 계산한다
 * - Bitmap 변환 없이 빠르게 분석한다
 */

/**
 * 실시간 프리뷰 프레임의 blur score를 계산한다.
 *
 * @param image CameraX 프레임
 * @return 실시간 blur score
 */
fun calculateRealtimeBlurScore(
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
        targetWidth = REALTIME_ANALYSIS_TARGET_WIDTH,
        targetHeight = REALTIME_ANALYSIS_TARGET_HEIGHT,
        cropWidthRatio = REALTIME_CROP_WIDTH_RATIO,
        cropHeightRatio = REALTIME_CROP_HEIGHT_RATIO
    )

    if (sampledValues.isEmpty()) {
        return 0f
    }

    return calculateLaplacianVariance(
        values = sampledValues,
        width = REALTIME_ANALYSIS_TARGET_WIDTH,
        height = REALTIME_ANALYSIS_TARGET_HEIGHT
    )
}