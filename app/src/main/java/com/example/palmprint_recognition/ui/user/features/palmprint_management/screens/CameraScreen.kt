package com.example.palmprint_recognition.ui.user.features.palmprint_management.screens

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.palmprint_recognition.ui.user.features.palmprint_management.components.CameraShutterIcon
import java.nio.ByteBuffer

@Composable
fun CameraScreen(
    onCaptured: (Bitmap) -> Unit,
    onCancel: () -> Unit,
) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) error = "카메라 권한이 필요합니다."
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3B3B3)) // CSS 배경
    ) {
        if (!hasPermission) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = error ?: "카메라 권한 확인 중...")
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "돌아가기",
                    modifier = Modifier.clickable { onCancel() }
                )
            }
            return@Box
        }

        CameraPreviewWithCapture(
            context = context,
            onCaptured = onCaptured,
            onError = { msg -> error = msg }
        )

        // 하단 카메라 아이콘 버튼
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clickable {
                        // CameraPreviewWithCapture 내부 버튼이 아니라
                        // "외부 버튼 클릭"으로 찍고 싶으면 상태 공유가 필요
                        // => 단순화를 위해 내부에 실제 캡처 트리거를 둠
                    },
                contentAlignment = Alignment.Center
            ) {
                // 실제 캡처 버튼은 CameraPreviewWithCapture 안에 있음
            }
        }

        error?.let {
            Text(
                text = it,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 24.dp)
            )
        }
    }
}

@Composable
private fun CameraPreviewWithCapture(
    context: Context,
    onCaptured: (Bitmap) -> Unit,
    onError: (String) -> Unit,
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    val selector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        Log.e("PalmCamera", "bind 실패", e)
                        onError("카메라 초기화에 실패했습니다.")
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            }
        )

        // 큰 카메라 아이콘 버튼 (CSS: 147x136, 아래쪽)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clickable {
                        val cap = imageCapture ?: run {
                            onError("카메라가 아직 준비되지 않았습니다.")
                            return@clickable
                        }
                        captureToBitmap(
                            context = context,
                            imageCapture = cap,
                            onSuccess = onCaptured,
                            onFailure = { onError(it) }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                CameraShutterIcon()
            }
        }
    }
}

private fun captureToBitmap(
    context: Context,
    imageCapture: ImageCapture,
    onSuccess: (Bitmap) -> Unit,
    onFailure: (String) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(context)

    imageCapture.takePicture(
        executor,
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                try {
                    val bitmap = imageProxyToBitmap(image)
                    onSuccess(bitmap)
                } catch (e: Exception) {
                    onFailure("촬영 이미지 처리 중 오류가 발생했습니다.")
                } finally {
                    image.close()
                }
            }

            override fun onError(exception: ImageCaptureException) {
                onFailure("촬영에 실패했습니다: ${exception.message}")
            }
        }
    )
}

private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
    // 여기서는 많은 기기에서 동작하는 방식(첫 plane buffer)으로 우선 구현.
    val buffer: ByteBuffer = image.planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}
