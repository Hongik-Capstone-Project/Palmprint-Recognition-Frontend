package com.example.palmprint_recognition.ui.demo.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Base64
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest

private const val DEMO_UPLOAD_MAX_WIDTH = 720
private const val DEMO_UPLOAD_JPEG_QUALITY = 90

/**
 * 데모 손바닥 업로드 이미지 준비 결과
 *
 * 역할
 * - 서버에 보낼 Bitmap, JPEG byte, Base64 문자열을 함께 보관한다
 *
 * @property bitmap 서버 전송 직전 Bitmap
 * @property jpegBytes JPEG 압축 결과
 * @property base64 서버 전송용 Base64 문자열
 * @property sha256 JPEG byte 기준 SHA-256 해시
 */
data class DemoPalmprintUploadImage(
    val bitmap: Bitmap,
    val jpegBytes: ByteArray,
    val base64: String,
    val sha256: String
)

/**
 * 서버 전송용 손바닥 이미지를 준비한다.
 *
 * 처리 순서
 * - crop된 Bitmap을 최대 너비 기준으로 축소
 * - JPEG byte로 압축
 * - Base64 문자열로 변환
 * - byte 해시를 계산
 *
 * @param bitmap crop된 손바닥 Bitmap
 * @return 서버 전송용 이미지 정보
 */
fun prepareDemoPalmprintUploadImage(
    bitmap: Bitmap
): DemoPalmprintUploadImage? {
    val resizedBitmap = resizeBitmapKeepingRatio(
        bitmap = bitmap,
        maxWidth = DEMO_UPLOAD_MAX_WIDTH
    )

    val outputStream = ByteArrayOutputStream()

    val isSuccess = resizedBitmap.compress(
        Bitmap.CompressFormat.JPEG,
        DEMO_UPLOAD_JPEG_QUALITY,
        outputStream
    )

    if (!isSuccess) {
        return null
    }

    val jpegBytes = outputStream.toByteArray()

    val base64 = Base64.encodeToString(
        jpegBytes,
        Base64.NO_WRAP
    )

    return DemoPalmprintUploadImage(
        bitmap = resizedBitmap,
        jpegBytes = jpegBytes,
        base64 = base64,
        sha256 = calculateSha256(jpegBytes)
    )
}

/**
 * 서버 전송 직전 이미지를 디버깅용으로 저장한다.
 *
 * @param context Android Context
 * @param uploadImage 서버 전송용 이미지 정보
 * @param prefix 파일명 접두어
 */
fun saveDemoPalmprintUploadDebugImage(
    context: Context,
    uploadImage: DemoPalmprintUploadImage,
    prefix: String
) {
    runCatching {
        val directory = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "demo_palmprint_upload"
        )

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(
            directory,
            "${prefix}_${System.currentTimeMillis()}.jpg"
        )

        FileOutputStream(file).use { outputStream ->
            outputStream.write(uploadImage.jpegBytes)
        }

        Timber.tag("DemoPalmUpload").d(
            "saved=%s width=%d height=%d bytes=%d base64Length=%d sha256=%s",
            file.absolutePath,
            uploadImage.bitmap.width,
            uploadImage.bitmap.height,
            uploadImage.jpegBytes.size,
            uploadImage.base64.length,
            uploadImage.sha256
        )
    }.onFailure { exception ->
        Timber.tag("DemoPalmUpload").e(
            exception,
            "Failed to save demo palmprint upload image"
        )
    }
}

/**
 * Bitmap을 비율 유지하며 축소한다.
 *
 * @param bitmap 원본 Bitmap
 * @param maxWidth 최대 너비
 * @return 축소된 Bitmap
 */
private fun resizeBitmapKeepingRatio(
    bitmap: Bitmap,
    maxWidth: Int
): Bitmap {
    if (bitmap.width <= maxWidth) {
        return bitmap
    }

    val ratio = maxWidth.toFloat() / bitmap.width.toFloat()
    val targetHeight = (bitmap.height * ratio).toInt().coerceAtLeast(1)

    return Bitmap.createScaledBitmap(
        bitmap,
        maxWidth,
        targetHeight,
        true
    )
}

/**
 * ByteArray의 SHA-256 해시를 계산한다.
 *
 * @param bytes 해시 계산 대상
 * @return SHA-256 문자열
 */
private fun calculateSha256(
    bytes: ByteArray
): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(bytes)

    return hashBytes.joinToString("") { byte ->
        "%02x".format(byte)
    }
}