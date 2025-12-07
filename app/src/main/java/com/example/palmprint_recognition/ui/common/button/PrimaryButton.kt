package com.example.palmprint_recognition.ui.common.button

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ✅ Primary Button (상대 위치 + 스타일 전부 외부 제어)
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,

    modifier: Modifier = Modifier,                // ✅ [유지] 상대 위치 + 여백 제어

    width: Dp = 356.dp,                           // ✅ [추가] 버튼 너비 파라미터화
    height: Dp = 56.dp,                           // ✅ [추가] 버튼 높이 파라미터화

    backgroundColor: Color = Color(0xFFC1C7CD),   // ✅ [추가] 버튼 배경색 파라미터화
    borderColor: Color = Color(0xFFC1C7CD),       // ✅ [추가] 버튼 테두리 색

    textColor: Color = Color.White,               // ✅ [추가] 텍스트 색상
    textSize: Int = 20                            // ✅ [추가] 텍스트 크기
) {
    Box(
        modifier = modifier
            .width(width)                         // ✅ [수정] 하드코딩 제거
            .height(height)                       // ✅ [수정]
            .background(backgroundColor)          // ✅ [수정]
            .border(2.dp, borderColor)            // ✅ [수정]
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = textSize.sp,               // ✅ [수정]
            fontWeight = FontWeight.Medium,
            color = textColor                     // ✅ [수정]
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPrimaryButton() {
    PrimaryButton(
        text = "유저 정보 추가",
        onClick = {},
        modifier = Modifier.padding(16.dp)
    )
}
