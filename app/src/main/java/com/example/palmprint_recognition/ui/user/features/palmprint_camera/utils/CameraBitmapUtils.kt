package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface

/**
 * Bitmap 관련 유틸리티 함수 모음
 *
 * 역할
 * - 촬영된 이미지 디코딩
 * - EXIF 회전 보정
 */

/**
 * EXIF 회전 정보를 반영하여 Bitmap을 디코딩한다
 *
 * @param filePath 촬영된 이미지 파일 경로
 * @return 회전이 보정된 Bitmap
 */
fun decodeBitmapWithExifRotation(
    filePath: String
): Bitmap? {

    val bmp = BitmapFactory.decodeFile(filePath) ?: return null

    val exif = runCatching {
        ExifInterface(filePath)
    }.getOrNull() ?: return bmp

    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )

    val rotateDegrees = when (orientation) {

        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270

        else -> 0
    }

    if (rotateDegrees == 0) return bmp

    val matrix = Matrix().apply {
        postRotate(rotateDegrees.toFloat())
    }

    return Bitmap.createBitmap(
        bmp,
        0,
        0,
        bmp.width,
        bmp.height,
        matrix,
        true
    )
}