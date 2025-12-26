package com.example.palmprint_recognition.ui.common.checkbox

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * ✅ SVG 기반 체크 아이콘 벡터 정의
 * (Figma SVG 1:1 대응)
 */
object PalmCheckIcons {

    // ✅ 체크 안된 원형 아이콘
    val Unchecked: ImageVector = ImageVector.Builder(
        name = "Unchecked",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            stroke = SolidColor(Color(0xFF21272A)),
            strokeLineWidth = 1.5f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(12f, 22f)
            curveTo(17.5228f, 22f, 22f, 17.5228f, 22f, 12f)
            curveTo(22f, 6.47715f, 17.5228f, 2f, 12f, 2f)
            curveTo(6.47715f, 2f, 2f, 6.47715f, 2f, 12f)
            curveTo(2f, 17.5228f, 6.47715f, 22f, 12f, 22f)
            close()
        }
    }.build()

    // ✅ 체크된 아이콘 (원 + 체크)
    val Checked: ImageVector = ImageVector.Builder(
        name = "Checked",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {

        // ✅ 체크 표시
        path(
            stroke = SolidColor(Color(0xFF21272A)),
            strokeLineWidth = 1.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(7f, 12.5f)
            lineTo(10f, 15.5f)
            lineTo(17f, 8.5f)
        }

        // ✅ 원 테두리
        path(
            stroke = SolidColor(Color(0xFF21272A)),
            strokeLineWidth = 1.5f
        ) {
            moveTo(12f, 22f)
            curveTo(17.5228f, 22f, 22f, 17.5228f, 22f, 12f)
            curveTo(22f, 6.47715f, 17.5228f, 2f, 12f, 2f)
            curveTo(6.47715f, 2f, 2f, 6.47715f, 2f, 12f)
            curveTo(2f, 17.5228f, 6.47715f, 22f, 12f, 22f)
            close()
        }

    }.build()
}


// ✅ 체크 안된 아이콘 프리뷰
@Preview(
    name = "Palm Unchecked Icon",
    showBackground = true
)
@Composable
fun PreviewPalmUncheckedIcon() {
    Icon(
        imageVector = PalmCheckIcons.Unchecked,
        contentDescription = "Unchecked",
        modifier = Modifier.size(48.dp)
    )
}

// ✅ 체크된 아이콘 프리뷰
@Preview(
    name = "Palm Checked Icon",
    showBackground = true
)
@Composable
fun PreviewPalmCheckedIcon() {
    Icon(
        imageVector = PalmCheckIcons.Checked,
        contentDescription = "Checked",
        modifier = Modifier.size(48.dp)
    )
}