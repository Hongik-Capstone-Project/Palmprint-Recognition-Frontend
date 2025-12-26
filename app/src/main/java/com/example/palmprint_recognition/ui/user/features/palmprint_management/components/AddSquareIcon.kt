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
 * CSS의 add-square 느낌 (사각형 테두리 + 플러스)
 * - 테두리 색: #21272A (CoolGray/90)
 */
@Composable
fun AddSquareIcon(
    size: Dp = 61.dp,
    strokeWidth: Dp = 1.5.dp,
    color: Color = Color(0xFF21272A),
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val sw = strokeWidth.toPx()
        val pad = sw / 2f

        // outer square
        drawRect(
            color = color,
            topLeft = Offset(pad, pad),
            size = Size(this.size.width - sw, this.size.height - sw),
            style = Stroke(width = sw)
        )

        // plus
        val cx = this.size.width / 2f
        val cy = this.size.height / 2f
        val arm = this.size.minDimension * 0.20f

        drawLine(
            color = color,
            start = Offset(cx - arm, cy),
            end = Offset(cx + arm, cy),
            strokeWidth = sw
        )
        drawLine(
            color = color,
            start = Offset(cx, cy - arm),
            end = Offset(cx, cy + arm),
            strokeWidth = sw
        )
    }
}
