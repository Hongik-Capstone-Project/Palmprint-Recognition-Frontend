package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper

import android.graphics.Bitmap
import android.graphics.Color

/**
 * grayscale 변환 관련 유틸
 *
 * 역할
 * - Bitmap RGB 값을 grayscale 배열로 변환한다
 */

/**
 * RGB 값을 grayscale 값으로 변환한다.
 *
 * @param red 빨간색 채널 값
 * @param green 초록색 채널 값
 * @param blue 파란색 채널 값
 * @return grayscale 값
 */
fun rgbToGray(
    red: Int,
    green: Int,
    blue: Int
): Float {
    return 0.299f * red + 0.587f * green + 0.114f * blue
}

/**
 * Bitmap을 grayscale 배열로 변환한다.
 *
 * @param bitmap 변환할 Bitmap
 * @return grayscale 값 배열
 */
fun bitmapToGrayValues(
    bitmap: Bitmap
): FloatArray {
    val width = bitmap.width
    val height = bitmap.height

    val pixels = IntArray(width * height)
    bitmap.getPixels(
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

        grayValues[index] = rgbToGray(
            red = Color.red(pixel),
            green = Color.green(pixel),
            blue = Color.blue(pixel)
        )
    }

    return grayValues
}