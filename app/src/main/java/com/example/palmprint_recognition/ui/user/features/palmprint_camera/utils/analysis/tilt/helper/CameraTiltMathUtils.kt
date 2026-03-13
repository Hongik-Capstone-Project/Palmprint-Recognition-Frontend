package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.tilt.helper

import kotlin.math.abs

/**
 * tilt 계산 수학 유틸
 *
 * 역할
 * - 밝기 배열에서 위쪽/아래쪽 중심 x 차이를 계산한다
 * - 이를 기울기 점수로 정규화한다
 */

/**
 * 위쪽 절반과 아래쪽 절반의 중심 x 차이를 비율로 계산한다.
 *
 * 계산 방식
 * - threshold 이하 픽셀을 손바닥 후보로 본다
 * - 위쪽 절반의 평균 x를 구한다
 * - 아래쪽 절반의 평균 x를 구한다
 * - 두 평균 x 차이를 width로 나누어 정규화한다
 *
 * @param values 밝기 배열
 * @param width 배열 너비
 * @param height 배열 높이
 * @param darkPixelThreshold 손바닥 후보 밝기 기준
 * @return 정규화된 중심 x 차이
 */
fun calculateVerticalCenterOffsetRatio(
    values: FloatArray,
    width: Int,
    height: Int,
    darkPixelThreshold: Float
): Float {
    if (values.isEmpty()) {
        return 0f
    }

    if (width <= 0 || height <= 0) {
        return 0f
    }

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