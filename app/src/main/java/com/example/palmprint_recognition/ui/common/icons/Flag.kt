package com.example.palmprint_recognition.ui.common.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Flag: ImageVector
    get() = ImageVector.Builder(
        name = "Flag",
        defaultWidth = 18.dp,
        defaultHeight = 21.dp,
        viewportWidth = 18f,
        viewportHeight = 21f
    ).apply {
        path(
            fill = SolidColor(Color(0xFF21272A)),
            pathFillType = PathFillType.NonZero
        ) {
            addPath(PathParser().parsePathString(
                "M1.78125 21H0V1.18922L0.219844 0.969844C0.381563 0.807656 1.34906 0 4.5 0C6.24422 0 8.19516 0.689531 9.91641 1.2975C11.3025 1.78734 12.6117 2.25 13.5 2.25C15.5077 2.25 16.9088 1.58203 16.9219 1.575L18 1.05281V13.2136L17.5856 13.4208C17.5177 13.4531 15.8995 14.25 13.5 14.25C12.3684 14.25 10.9509 13.9153 9.45047 13.5605C7.76391 13.162 6.02016 12.75 4.5 12.75C2.77172 12.75 2.16844 13.0116 1.78125 13.177V21Z"
            ).toNodes())
        }
    }.build()
