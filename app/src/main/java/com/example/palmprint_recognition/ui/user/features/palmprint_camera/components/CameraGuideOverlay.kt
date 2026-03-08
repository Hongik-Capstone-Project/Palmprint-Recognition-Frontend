package com.example.palmprint_recognition.ui.user.features.palmprint_camera.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Size

/**
 * 손바닥 촬영 가이드라인 오버레이
 *
 * 기능
 * - 중앙 세로 타원 가이드
 * - 손목 기준선 표시
 * - 반투명 마스크
 *
 * 목적
 * 사용자가 일정한 위치와 크기로 손바닥을 촬영하도록 유도한다.
 *
 * @param modifier Compose Modifier
 */
@Composable
fun CameraGuideOverlay(
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val w = size.width
        val h = size.height

        val ovalW = w * 0.75f
        val ovalH = h * 0.50f

        val ovalLeft = (w - ovalW) / 2f
        val ovalTop = (h - ovalH) / 2f

        val ovalRect = Rect(
            ovalLeft,
            ovalTop,
            ovalLeft + ovalW,
            ovalTop + ovalH
        )

        val wristY = ovalRect.bottom + (h * 0.03f)
        val wristLineHalf = ovalW * 0.35f

        // 전체 마스크
        drawRect(color = Color(0x88000000))

        // 타원 내부 밝게
        drawOval(
            color = Color(0x22000000),
            topLeft = Offset(ovalRect.left, ovalRect.top),
            size = Size(ovalRect.width, ovalRect.height)
        )

        // 타원 테두리
        drawOval(
            color = Color.White,
            topLeft = Offset(ovalRect.left, ovalRect.top),
            size = Size(ovalRect.width, ovalRect.height),
            style = Stroke(width = 6f)
        )

        // 손목 가이드선
        drawLine(
            color = Color.White,
            start = Offset(w / 2f - wristLineHalf, wristY),
            end = Offset(w / 2f + wristLineHalf, wristY),
            strokeWidth = 6f
        )
    }
}