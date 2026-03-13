package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur.helper

import kotlin.math.max

/**
 * blur 수학 계산 유틸
 *
 * 역할
 * - grayscale 배열에서 Laplacian variance를 계산한다
 */

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
fun calculateLaplacianVariance(
    values: FloatArray,
    width: Int,
    height: Int
): Float {
    if (values.isEmpty()) {
        return 0f
    }

    if (width < 3 || height < 3) {
        return 0f
    }

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

    if (count == 0) {
        return 0f
    }

    val mean = sum / count
    val variance = (sumSquared / count) - (mean * mean)

    return max(0f, variance)
}