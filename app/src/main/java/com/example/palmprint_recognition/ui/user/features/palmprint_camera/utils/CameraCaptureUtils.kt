package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.io.File

/**
 * CameraX 촬영 관련 유틸
 */

/**
 * CameraX Preview + ImageCapture 바인딩
 */
fun bindCameraUseCases(
    context: Context,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    previewView: PreviewView,
    onReadyCapture: (ImageCapture) -> Unit
) {

    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    val executor = ContextCompat.getMainExecutor(context)

    cameraProviderFuture.addListener({

        val cameraProvider = cameraProviderFuture.get()

        previewView.scaleType = PreviewView.ScaleType.FIT_CENTER

        val preview = Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

        val rotation = previewView.display.rotation

        val imageCapture = ImageCapture.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(rotation)
            .build()

        val selector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()

        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            selector,
            preview,
            imageCapture
        )

        onReadyCapture(imageCapture)

    }, executor)
}

/**
 * 이미지 촬영 후 Bitmap으로 변환
 */
fun captureToFileThenBitmap(
    context: Context,
    imageCapture: ImageCapture,
    onSuccess: (Bitmap) -> Unit,
    onFailure: (String) -> Unit
) {

    val executor = ContextCompat.getMainExecutor(context)

    val photoFile = File.createTempFile(
        "palm_capture_",
        ".jpg",
        context.cacheDir
    )

    val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        output,
        executor,
        object : ImageCapture.OnImageSavedCallback {

            override fun onImageSaved(
                outputFileResults: ImageCapture.OutputFileResults
            ) {

                val bmp = decodeBitmapWithExifRotation(photoFile.absolutePath)

                if (bmp == null) {
                    onFailure("촬영 이미지 디코딩 실패")
                } else {
                    onSuccess(bmp)
                }
            }

            override fun onError(
                exception: ImageCaptureException
            ) {

                onFailure("촬영 실패: ${exception.message}")
            }
        }
    )
}