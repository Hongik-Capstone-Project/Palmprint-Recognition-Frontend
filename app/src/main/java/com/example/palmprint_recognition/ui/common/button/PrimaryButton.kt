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

/**
 * ✅ Primary Button (상대 위치 + 스타일 전부 외부 제어)
 * - width/height가 null이면 modifier에서 지정한 크기(fillMaxWidth 등)를 그대로 사용
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,

    width: Dp? = 356.dp,     // ✅ nullable 지원
    height: Dp? = 56.dp,     // ✅ nullable 지원

    backgroundColor: Color = Color(0xFFC1C7CD),
    borderColor: Color = Color(0xFFC1C7CD),

    textColor: Color = Color.White,
    textSize: Int = 20
) {
    // ✅ width/height가 null이면 적용하지 않음 (fillMaxWidth 등 modifier 우선)
    val sizedModifier = modifier
        .then(if (width != null) Modifier.width(width) else Modifier)
        .then(if (height != null) Modifier.height(height) else Modifier)

    Box(
        modifier = sizedModifier
            .background(backgroundColor)
            .border(2.dp, borderColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = textSize.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPrimaryButton() {
    PrimaryButton(
        text = "유저 정보 추가",
        onClick = {},
        modifier = Modifier,     // 여기서 fillMaxWidth 같은 걸 주면 width=null로 반영 가능
        width = 356.dp,
        height = 56.dp
    )
}
