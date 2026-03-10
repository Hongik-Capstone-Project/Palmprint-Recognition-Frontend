package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.io.File

/**
 * CameraX 촬영 관련 유틸
 */

/**
 * CameraX Preview + ImageCapture + ImageAnalysis 바인딩
 *
 * 역할
 * - 카메라 프리뷰 표시
 * - 사진 촬영 기능 연결
 * - 실시간 프레임 분석 기능 연결
 *
 * 현재 단계
 * - ImageAnalysis 구조만 먼저 추가한다
 * - 실제 blur 계산/프레임 스킵 로직은 다음 단계에서 추가한다
 *
 * @param context Context
 * @param lifecycleOwner LifecycleOwner
 * @param previewView CameraX PreviewView
 * @param analyzer 실시간 프레임 분석기
 * @param onReadyCapture 준비된 ImageCapture 콜백
 */
fun bindCameraUseCases(
    context: Context,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    previewView: PreviewView,
    analyzer: ImageAnalysis.Analyzer,
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

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageRotationEnabled(true)
            .build()
            .also {
                it.setAnalyzer(executor, analyzer)
            }

        val selector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            selector,
            preview,
            imageCapture,
            imageAnalysis
        )

        onReadyCapture(imageCapture)
    }, executor)
}

/**
 * 이미지를 임시 파일로 저장한 뒤 Bitmap으로 변환한다
 *
 * 주의
 * - EXIF 회전 보정 후
 * - 프리뷰 방향과 Bitmap 방향을 한 번 더 맞춘다
 *
 * @param context Context
 * @param imageCapture CameraX ImageCapture
 * @param previewWidth 프리뷰 너비
 * @param previewHeight 프리뷰 높이
 * @param onSuccess 방향 보정이 끝난 Bitmap 콜백
 * @param onFailure 실패 메시지 콜백
 */
fun captureToFileThenBitmap(
    context: Context,
    imageCapture: ImageCapture,
    previewWidth: Float,
    previewHeight: Float,
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
                outputFileResults: androidx.camera.core.ImageCapture.OutputFileResults
            ) {
                val decodedBitmap = decodeBitmapWithExifRotation(photoFile.absolutePath)

                if (decodedBitmap == null) {
                    onFailure("촬영 이미지 디코딩 실패")
                    return
                }

                val alignedBitmap = alignBitmapToPreviewOrientation(
                    bitmap = decodedBitmap,
                    previewWidth = previewWidth,
                    previewHeight = previewHeight
                )

                onSuccess(alignedBitmap)
            }

            override fun onError(
                exception: ImageCaptureException
            ) {
                onFailure("촬영 실패: ${exception.message}")
            }
        }
    )
}