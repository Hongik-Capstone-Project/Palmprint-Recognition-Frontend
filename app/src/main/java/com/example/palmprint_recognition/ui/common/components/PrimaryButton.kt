package com.example.palmprint_recognition.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Primary Button - Figma 1:1 스타일 재현
 *
 * @param text 버튼 안의 텍스트
 * @param onClick 클릭 이벤트
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(356.dp)
            .height(56.dp)
            .background(Color(0xFFC1C7CD))                // CoolGray/30
            .border(2.dp, Color(0xFFC1C7CD))             // Border
            .clickable { onClick() },                    // 클릭 가능
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPrimaryButton() {
    PrimaryButton(
        text = "유저 정보 추가",
        onClick = {},
        modifier = Modifier
            .padding(16.dp)
    )
}


