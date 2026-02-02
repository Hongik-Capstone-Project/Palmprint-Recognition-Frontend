package com.example.palmprint_recognition.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Close: ImageVector
    get() = ImageVector.Builder(
        name = "Close",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        // circle (stroke)
        path(
            fill = SolidColor(Color(0x00000000)),
            stroke = SolidColor(Color(0xFF21272A)),
            strokeLineWidth = 1.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero
        ) {
            addPath(PathParser().parsePathString(
                "M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z"
            ).toNodes())
        }

        // x line 1
        path(
            fill = SolidColor(Color(0x00000000)),
            stroke = SolidColor(Color(0xFF21272A)),
            strokeLineWidth = 1.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero
        ) {
            addPath(PathParser().parsePathString("M15 16L9 8").toNodes())
        }

        // x line 2
        path(
            fill = SolidColor(Color(0x00000000)),
            stroke = SolidColor(Color(0xFF21272A)),
            strokeLineWidth = 1.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero
        ) {
            addPath(PathParser().parsePathString("M9 16L15 8").toNodes())
        }
    }.build()
