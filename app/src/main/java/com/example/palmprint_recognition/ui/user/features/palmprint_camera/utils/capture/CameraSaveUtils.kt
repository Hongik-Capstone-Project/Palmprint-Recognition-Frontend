package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore

private const val PALM_SAMPLE_DIRECTORY = "PalmprintSamples"

/**
 * Bitmap을 갤러리에 저장한다.
 *
 * 저장 위치
 * - Pictures/PalmprintSamples
 *
 * 파일 용도 예시
 * - palm_raw_시간값.jpg
 * - palm_crop_시간값.jpg
 *
 * @param context Context
 * @param bitmap 저장할 Bitmap
 * @param fileName 저장 파일명
 * @return 저장 성공 여부
 */
fun saveBitmapToGallery(
    context: Context,
    bitmap: Bitmap,
    fileName: String
): Boolean {
    val resolver = context.contentResolver

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/$PALM_SAMPLE_DIRECTORY"
            )
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val imageUri = resolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ) ?: return false

    return try {
        resolver.openOutputStream(imageUri)?.use { outputStream ->
            val isSuccess = bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                95,
                outputStream
            )

            if (!isSuccess) {
                return false
            }
        } ?: return false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val updateValues = ContentValues().apply {
                put(MediaStore.Images.Media.IS_PENDING, 0)
            }
            resolver.update(imageUri, updateValues, null, null)
        }

        true
    } catch (_: Exception) {
        false
    }
}