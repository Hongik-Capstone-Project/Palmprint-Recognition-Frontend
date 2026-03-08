package com.example.palmprint_recognition.ui.user.features.palmprint_camera.screens

import android.Manifest
import android.graphics.Bitmap
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.*
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.bindCameraUseCases
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.captureToFileThenBitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.cropBitmapByGuideRect
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.calcGuideCropAreaRatioPercent

/**
 * 손바닥 촬영용 커스텀 카메라 화면
 *
 * 기능
 * - CameraX 프리뷰 표시
 * - 손바닥 가이드라인 표시
 * - 촬영 버튼 처리
 * - 촬영 결과를 상위 화면으로 전달
 *
 * @param onCaptured 촬영 완료된 Bitmap 콜백
 * @param onCancel 카메라 종료 콜백
 */
@Composable
fun CameraScreen(
    onCaptured: (Bitmap) -> Unit,
    onCancel: () -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var isCapturing by remember { mutableStateOf(false) }

    val previewView = remember { PreviewView(context) }

    var previewW by remember { mutableStateOf(0f) }
    var previewH by remember { mutableStateOf(0f) }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) {
            error = "카메라 권한이 필요합니다."
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        if (!hasPermission) {

            CameraPermissionContent(
                message = error ?: "카메라 권한 확인 중...",
                onCancel = onCancel
            )

            return@Box
        }

        CameraPreview(
            previewView = previewView,
            modifier = Modifier.fillMaxSize(),
            onPreviewSizeChanged = { w, h ->
                previewW = w
                previewH = h
            }
        )

        CameraGuideOverlay(
            modifier = Modifier.fillMaxSize()
        )

        CameraGuideText(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 44.dp)
        )

        error?.let {
            CameraStatusText(
                text = it,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 96.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {

            CameraCaptureButton(
                enabled = !isCapturing
            ) {

                val cap = imageCapture

                if (cap == null) {
                    error = "카메라 준비 중입니다"
                    return@CameraCaptureButton
                }

                isCapturing = true
                error = null

                captureToFileThenBitmap(
                    context = context,
                    imageCapture = cap,
                    onSuccess = { bmp ->

                        isCapturing = false

                        if (previewW <= 0f || previewH <= 0f) {

                            error = "프리뷰 크기 확인 실패"
                            onCaptured(bmp)

                            return@captureToFileThenBitmap
                        }

                        val cropped = cropBitmapByGuideRect(
                            bitmap = bmp,
                            viewW = previewW,
                            viewH = previewH
                        )

                        val ratio = calcGuideCropAreaRatioPercent(
                            bmp,
                            cropped
                        )

                        Log.d("PalmGuide", "Guide ratio = $ratio")

                        onCaptured(cropped)
                    },
                    onFailure = {

                        isCapturing = false
                        error = it
                    }
                )
            }
        }

        if (isCapturing) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }
    }
}