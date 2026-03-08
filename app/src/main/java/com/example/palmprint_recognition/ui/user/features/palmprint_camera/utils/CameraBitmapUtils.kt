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
 * - 프리뷰 방향에 맞춘 추가 회전 보정
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

/**
 * 프리뷰 방향과 Bitmap 방향을 일치시킨다
 *
 * 동작
 * - 프리뷰가 세로인데 Bitmap이 가로면 90도 회전
 * - 프리뷰가 가로인데 Bitmap이 세로면 90도 회전
 * - 이미 방향이 같으면 그대로 반환
 *
 * @param bitmap 원본 Bitmap
 * @param previewWidth 프리뷰 너비
 * @param previewHeight 프리뷰 높이
 * @return 프리뷰 방향과 맞춘 Bitmap
 */
fun alignBitmapToPreviewOrientation(
    bitmap: Bitmap,
    previewWidth: Float,
    previewHeight: Float
): Bitmap {
    val isPreviewPortrait = previewHeight >= previewWidth
    val isBitmapPortrait = bitmap.height >= bitmap.width

    if (isPreviewPortrait == isBitmapPortrait) {
        return bitmap
    }

    val matrix = Matrix().apply {
        postRotate(90f)
    }

    return Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
    )
}