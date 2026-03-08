package com.example.palmprint_recognition.ui.user.features.palmprint_camera.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 카메라 권한이 없을 때 표시되는 화면
 *
 * @param message 권한 안내 메시지
 * @param onCancel 이전 화면으로 돌아가기 콜백
 */
@Composable
fun CameraPermissionContent(
    message: String,
    onCancel: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = message,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "돌아가기",
            color = Color.White,
            modifier = Modifier.clickable {
                onCancel()
            }
        )
    }
}