package com.example.palmprint_recognition.ui.user.features.palmprint_camera.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 안전한 셔터 아이콘(배경색에 의존하지 않음)
 * - 바깥 링 + 안쪽 원 + 작은 하이라이트
 */
@Composable
fun CameraShutterIcon(
    iconSize: Dp = 96.dp,
    ringColor: Color = Color.White,
    innerColor: Color = Color(0x66FFFFFF), // 살짝 반투명
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(iconSize)) {
        val r = size.minDimension / 2f
        val center = Offset(r, r)

        // 바깥 링
        drawCircle(
            color = ringColor,
            radius = r * 0.95f,
            style = Stroke(width = r * 0.12f)
        )

        // 안쪽 원
        drawCircle(
            color = innerColor,
            radius = r * 0.62f
        )

        // 작은 하이라이트(위쪽)
        drawCircle(
            color = Color(0xAAFFFFFF),
            radius = r * 0.08f,
            center = Offset(center.x - r * 0.25f, center.y - r * 0.25f)
        )
    }
}