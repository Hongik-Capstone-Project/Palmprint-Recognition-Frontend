package com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.guide

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.guide.calculateGuideSpec

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
 * 주의
 * - 가이드라인 좌표는 CameraGuideUtils의 calculateGuideSpec() 기준을 사용한다
 * - 화면에 보이는 타원과 실제 crop 기준이 동일해야 한다
 *
 * @param modifier Compose Modifier
 */
@Composable
fun CameraGuideOverlay(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val viewWidth = size.width
        val viewHeight = size.height

        val guideSpec = calculateGuideSpec(
            viewW = viewWidth,
            viewH = viewHeight
        )

        val ovalRect = guideSpec.ovalRect
        val wristY = guideSpec.wristY
        val wristLineHalf = ovalRect.width * 0.35f

        // 전체 마스크
        drawRect(
            color = Color(0x22000000)
        )

        // 타원 내부를 조금 더 밝게 표시
        drawOval(
            color = Color(0x00000000),
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
            start = Offset(
                x = ovalRect.center.x - wristLineHalf,
                y = wristY
            ),
            end = Offset(
                x = ovalRect.center.x + wristLineHalf,
                y = wristY
            ),
            strokeWidth = 6f
        )
    }
}