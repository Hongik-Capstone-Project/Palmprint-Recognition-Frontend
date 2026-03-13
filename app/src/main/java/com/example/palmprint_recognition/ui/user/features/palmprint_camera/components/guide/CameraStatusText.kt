package com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 카메라 상태 및 디버그 메시지 표시
 *
 * @param text 표시할 메시지
 * @param modifier Compose Modifier
 */
@Composable
fun CameraStatusText(
    text: String,
    modifier: Modifier = Modifier
) {

    Text(
        text = text,
        color = Color.White,
        modifier = modifier
            .background(Color(0x66000000))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}