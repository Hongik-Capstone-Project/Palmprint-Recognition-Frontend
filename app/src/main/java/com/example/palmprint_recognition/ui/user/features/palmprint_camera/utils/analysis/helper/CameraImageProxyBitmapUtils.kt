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
 * - YUV_420_888 ImageProxy를 NV21 byte array로 안전하게 변환한다.
 * - rowStride / pixelStride 차이를 고려한다.
 * - MediaPipe 입력용 Bitmap을 생성한다.
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
    val nv21Bytes = convertYuv420888ToNv21(
        image = image
    ) ?: return null

    val yuvImage = YuvImage(
        nv21Bytes,
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
 * YUV_420_888 ImageProxy를 NV21 byte array로 변환한다.
 *
 * NV21 구조:
 * - Y plane 전체
 * - VU VU VU 순서의 chroma plane
 *
 * @param image CameraX ImageProxy
 * @return NV21 byte array
 */
private fun convertYuv420888ToNv21(
    image: ImageProxy
): ByteArray? {
    if (image.planes.size < 3) {
        return null
    }

    val width = image.width
    val height = image.height

    val yPlane = image.planes[0]
    val uPlane = image.planes[1]
    val vPlane = image.planes[2]

    val yBuffer = yPlane.buffer.duplicate()
    val uBuffer = uPlane.buffer.duplicate()
    val vBuffer = vPlane.buffer.duplicate()

    val nv21 = ByteArray(width * height + width * height / 2)

    var outputIndex = 0

    val yRowStride = yPlane.rowStride
    val yPixelStride = yPlane.pixelStride

    for (row in 0 until height) {
        for (col in 0 until width) {
            val yIndex = row * yRowStride + col * yPixelStride
            nv21[outputIndex++] = yBuffer.get(yIndex)
        }
    }

    val chromaHeight = height / 2
    val chromaWidth = width / 2

    val uRowStride = uPlane.rowStride
    val uPixelStride = uPlane.pixelStride
    val vRowStride = vPlane.rowStride
    val vPixelStride = vPlane.pixelStride

    for (row in 0 until chromaHeight) {
        for (col in 0 until chromaWidth) {
            val uIndex = row * uRowStride + col * uPixelStride
            val vIndex = row * vRowStride + col * vPixelStride

            nv21[outputIndex++] = vBuffer.get(vIndex)
            nv21[outputIndex++] = uBuffer.get(uIndex)
        }
    }

    return nv21
}

/**
 * Bitmap을 지정된 각도만큼 회전한다.
 *
 * @param bitmap 원본 Bitmap
 * @param rotationDegrees 회전 각도
 * @return 회전 보정된 Bitmap
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