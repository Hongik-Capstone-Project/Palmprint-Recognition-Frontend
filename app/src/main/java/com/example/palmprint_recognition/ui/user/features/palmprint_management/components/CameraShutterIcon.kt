package com.example.palmprint_recognition.ui.user.features.palmprint_management.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 카메라 화면의 큰 카메라 아이콘(대략 ionicons camera-sharp 느낌)
 * - CSS: width 147, height 136
 */
@Composable
fun CameraShutterIcon(
    width: Dp = 147.dp,
    height: Dp = 136.dp,
    color: Color = Color(0xFF21272A),
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(width, height)) {
        val w = size.width
        val h = size.height

        // 간단한 카메라 실루엣: body + lens(네모)
        val stroke = (w * 0.06f)

        // body
        drawRect(
            color = color,
            topLeft = Offset(w * 0.06f, h * 0.25f),
            size = Size(w * 0.88f, h * 0.60f),
        )

        // 상단 돌출부(뷰파인더 느낌)
        drawRect(
            color = Color(0xFFB3B3B3), // 배경색을 파서 모양을 만드는 방식
            topLeft = Offset(w * 0.18f, h * 0.18f),
            size = Size(w * 0.25f, h * 0.12f),
        )

        // lens hole (배경색으로 뚫기)
        drawRect(
            color = Color(0xFFB3B3B3),
            topLeft = Offset(w * 0.38f, h * 0.40f),
            size = Size(w * 0.24f, h * 0.24f),
        )

        // lens border (다시 테두리로 렌즈 강조)
        drawRect(
            color = color,
            topLeft = Offset(w * 0.38f, h * 0.40f),
            size = Size(w * 0.24f, h * 0.24f),
            style = Stroke(width = stroke)
        )
    }
}
