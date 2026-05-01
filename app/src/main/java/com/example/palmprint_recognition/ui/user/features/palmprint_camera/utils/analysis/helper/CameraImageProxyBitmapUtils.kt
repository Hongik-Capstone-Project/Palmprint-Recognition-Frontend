package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream

/**
 * CameraX ImageProxy를 Bitmap으로 변환하는 유틸리티
 *
 * 역할
 * - 실시간 ImageAnalysis 프레임을 MediaPipe 입력용 Bitmap으로 변환한다
 * - ImageProxy의 rotationDegrees를 반영해 Bitmap 방향을 보정한다
 */

/**
 * ImageProxy를 회전 보정된 Bitmap으로 변환한다.
 *
 * @param image CameraX 분석 프레임
 * @return 회전 보정된 Bitmap, 변환 실패 시 null
 */
fun convertImageProxyToBitmap(
    image: ImageProxy
): Bitmap? {
    val yBuffer = image.planes.getOrNull(0)?.buffer?.duplicate() ?: return null
    val uBuffer = image.planes.getOrNull(1)?.buffer?.duplicate() ?: return null
    val vBuffer = image.planes.getOrNull(2)?.buffer?.duplicate() ?: return null

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = YuvImage(
        nv21,
        ImageFormat.NV21,
        image.width,
        image.height,
        null
    )

    val outputStream = ByteArrayOutputStream()

    val isSuccess = yuvImage.compressToJpeg(
        Rect(
            0,
            0,
            image.width,
            image.height
        ),
        90,
        outputStream
    )

    if (!isSuccess) {
        return null
    }

    val jpegBytes = outputStream.toByteArray()

    val bitmap = BitmapFactory.decodeByteArray(
        jpegBytes,
        0,
        jpegBytes.size
    ) ?: return null

    return rotateBitmap(
        bitmap = bitmap,
        rotationDegrees = image.imageInfo.rotationDegrees
    )
}

/**
 * Bitmap을 지정된 각도만큼 회전한다.
 *
 * @param bitmap 원본 Bitmap
 * @param rotationDegrees 회전 각도
 * @return 회전된 Bitmap
 */
private fun rotateBitmap(
    bitmap: Bitmap,
    rotationDegrees: Int
): Bitmap {
    if (rotationDegrees == 0) {
        return bitmap
    }

    val matrix = Matrix().apply {
        postRotate(rotationDegrees.toFloat())
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