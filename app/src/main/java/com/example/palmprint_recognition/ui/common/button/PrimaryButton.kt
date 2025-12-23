package com.example.palmprint_recognition.ui.common.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,

    width: Dp? = 356.dp,
    height: Dp? = 56.dp,

    backgroundColor: Color = Color(0xFFC1C7CD),
    borderColor: Color = Color(0xFFC1C7CD),

    textColor: Color = Color.White,
    textSize: Int = 20,

    enabled: Boolean = true
) {
    val sizedModifier = modifier
        .then(if (width != null) Modifier.width(width) else Modifier)
        .then(if (height != null) Modifier.height(height) else Modifier)

    val bg = if (enabled) backgroundColor else Color(0xFFE0E0E0)
    val br = if (enabled) borderColor else Color(0xFFE0E0E0)
    val tc = if (enabled) textColor else Color(0xFF9AA0A6)

    Box(
        modifier = sizedModifier
            .background(bg)
            .border(2.dp, br)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = textSize.sp,
            fontWeight = FontWeight.Medium,
            color = tc
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPrimaryButton() {
    PrimaryButton(
        text = "유저 정보 추가",
        onClick = {},
        width = 356.dp,
        height = 56.dp,
        enabled = false
    )
}
