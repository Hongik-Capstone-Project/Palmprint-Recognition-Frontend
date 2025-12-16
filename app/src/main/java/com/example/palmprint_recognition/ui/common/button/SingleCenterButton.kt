package com.example.palmprint_recognition.ui.common.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.palmprint_recognition.ui.common.button.PrimaryButton

/**
 * 단일 버튼 중앙 정렬 그룹
 * @param text 버튼 텍스트
 * @param onClick 버튼 클릭 이벤트
 */
@Composable
fun SingleCenterButton(
    text: String,
    onClick: () -> Unit
) {
    PrimaryButton(
        text = text,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    )
}