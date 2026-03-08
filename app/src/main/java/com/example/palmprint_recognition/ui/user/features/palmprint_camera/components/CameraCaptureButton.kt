package com.example.palmprint_recognition.ui.user.features.palmprint_camera.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.user.features.palmprint_management.components.CameraShutterIcon

/**
 * 카메라 셔터 버튼
 *
 * 역할
 * - 촬영 버튼 UI
 * - 클릭 이벤트 상위 전달
 *
 * @param enabled 촬영 가능 여부
 * @param onClick 버튼 클릭 콜백
 */
@Composable
fun CameraCaptureButton(
    enabled: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(110.dp)
            .clickable(enabled = enabled) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        CameraShutterIcon(iconSize = 96.dp)

    }
}