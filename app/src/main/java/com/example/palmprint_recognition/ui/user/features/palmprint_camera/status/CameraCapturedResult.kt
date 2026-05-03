package com.example.palmprint_recognition.ui.user.features.palmprint_camera.status

import android.graphics.Bitmap

/**
 * 카메라 촬영 완료 결과
 *
 * 역할
 * - 원본 이미지와 서버 전송용 crop 이미지를 전달한다
 *
 * @property originalBitmap 원본 촬영 이미지
 * @property croppedBitmap 서버 전송용 crop 이미지
 */
data class CameraCapturedResult(
    val originalBitmap: Bitmap,
    val croppedBitmap: Bitmap
)