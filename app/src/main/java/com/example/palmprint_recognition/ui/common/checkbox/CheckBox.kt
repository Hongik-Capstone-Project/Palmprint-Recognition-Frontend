package com.example.palmprint_recognition.ui.common.checkbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ✅ SVG 기반 체크박스 컴포넌트
 */
@Composable
fun CheckBox(
    checked: Boolean,
    text: String,
    readOnly: Boolean = false,          // ✅ 읽기 전용 모드
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit  // ✅ ViewModel 전달용 콜백
) {
    Row(
        modifier = modifier
            .clickable(enabled = !readOnly) {
                onCheckedChange(!checked)   // ✅ 상태 변경 전달
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ✅ 우리가 만든 SVG 벡터 아이콘 사용
        Icon(
            imageVector = if (checked) {
                PalmCheckIcons.Checked     // ✅ 체크된 아이콘
            } else {
                PalmCheckIcons.Unchecked   // ✅ 체크 안된 아이콘
            },
            contentDescription = null,
            tint = Color.Unspecified,     // ✅ SVG 원본 색상 유지
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCheckBox_Checked() {
    CheckBox(
        checked = true,
        text = "관리자 계정",
        onCheckedChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCheckBox_Unchecked() {
    CheckBox(
        checked = false,
        text = "관리자 계정",
        onCheckedChange = {}
    )
}
