package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper

/**
 * Y plane 중앙 영역 샘플링 유틸
 *
 * 역할
 * - ImageProxy의 Y plane 바이트 배열을 중앙 기준으로 축소 샘플링한다
 * - rowStride, pixelStride를 반영한다
 * - 범위를 벗어나지 않도록 안전하게 처리한다
 */

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
 * @param cropWidthRatio 중앙 crop 너비 비율
 * @param cropHeightRatio 중앙 crop 높이 비율
 * @return 축소된 밝기 배열
 */
fun sampleCenterLumaRegion(
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
    if (source.isEmpty()) {
        return FloatArray(0)
    }

    if (sourceWidth <= 0 || sourceHeight <= 0) {
        return FloatArray(0)
    }

    if (rowStride <= 0 || pixelStride <= 0) {
        return FloatArray(0)
    }

    val result = FloatArray(targetWidth * targetHeight)

    val cropWidth = (sourceWidth * cropWidthRatio)
        .toInt()
        .coerceAtLeast(targetWidth)

    val cropHeight = (sourceHeight * cropHeightRatio)
        .toInt()
        .coerceAtLeast(targetHeight)

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