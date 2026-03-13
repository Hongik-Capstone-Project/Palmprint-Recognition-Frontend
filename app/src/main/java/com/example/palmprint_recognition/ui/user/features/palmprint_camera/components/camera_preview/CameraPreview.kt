package com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.camera_preview

import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.viewinterop.AndroidView

/**
 * CameraX PreviewView를 Compose에 표시한다
 *
 * 역할
 * - Android Camera PreviewView를 Compose UI에 렌더링
 * - Preview 크기 변화를 상위로 전달
 *
 * @param previewView CameraX PreviewView
 * @param modifier Compose Modifier
 * @param onPreviewSizeChanged 프리뷰 크기 변경 콜백
 */
@Composable
fun CameraPreview(
    previewView: PreviewView,
    modifier: Modifier = Modifier,
    onPreviewSizeChanged: (width: Float, height: Float) -> Unit
) {

    AndroidView(
        factory = { previewView },
        modifier = modifier.onSizeChanged { size ->
            onPreviewSizeChanged(size.width.toFloat(), size.height.toFloat())
        }
    )
}