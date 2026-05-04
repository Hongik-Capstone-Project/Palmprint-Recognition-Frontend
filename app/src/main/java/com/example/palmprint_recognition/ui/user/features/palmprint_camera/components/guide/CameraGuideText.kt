package com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 카메라 상단 가이드 문구
 *
 * 사용자가 손 위치를 올바르게 맞추도록 안내한다.
 *
 * @param text 표시할 가이드 문구
 * @param modifier Compose Modifier
 */
@Composable
fun CameraGuideText(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0x66000000))
            .padding(vertical = 6.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = Color.White
        )
    }
}