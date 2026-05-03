package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture

import android.content.Context
import android.graphics.Bitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCapturedResult
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.guide.cropBitmapByGuideRect
import timber.log.Timber

/**
 * 촬영된 원본 Bitmap을 crop하여 최종 결과를 만든다.
 *
 * @param originalBitmap 촬영 원본 Bitmap
 * @param previewWidth 프리뷰 너비
 * @param previewHeight 프리뷰 높이
 * @return 촬영 결과
 */
fun createCameraCapturedResult(
    originalBitmap: Bitmap,
    previewWidth: Float,
    previewHeight: Float
): CameraCapturedResult {
    val croppedBitmap = cropBitmapByGuideRect(
        bitmap = originalBitmap,
        viewW = previewWidth,
        viewH = previewHeight
    )

    return CameraCapturedResult(
        originalBitmap = originalBitmap,
        croppedBitmap = croppedBitmap
    )
}

/**
 * 테스트용 원본 / crop 이미지를 저장한다.
 *
 * @param context Context
 * @param result 촬영 결과
 */
fun saveCapturedBitmapsForTest(
    context: Context,
    result: CameraCapturedResult
) {
    val timestamp = System.currentTimeMillis()

    val rawSaved = saveBitmapToGallery(
        context = context,
        bitmap = result.originalBitmap,
        fileName = "palm_raw_$timestamp.jpg"
    )

    val cropSaved = saveBitmapToGallery(
        context = context,
        bitmap = result.croppedBitmap,
        fileName = "palm_crop_$timestamp.jpg"
    )

    Timber.tag("PalmSave").d(
        "rawSaved=%s cropSaved=%s",
        rawSaved,
        cropSaved
    )
}

/**
 * 촬영 결과 로그 메시지를 생성한다.
 *
 * @param previewWidth 프리뷰 너비
 * @param previewHeight 프리뷰 높이
 * @param result 촬영 결과
 * @return 로그 문자열
 */
fun buildPalmCropLogMessage(
    previewWidth: Float,
    previewHeight: Float,
    result: CameraCapturedResult
): String {
    return "preview=${previewWidth}x${previewHeight}, " +
            "original=${result.originalBitmap.width}x${result.originalBitmap.height}, " +
            "cropped=${result.croppedBitmap.width}x${result.croppedBitmap.height}"
}