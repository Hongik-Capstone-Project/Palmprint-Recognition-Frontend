package com.example.palmprint_recognition.ui.common.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.common.button.PrimaryButton

@Composable
fun PalmFooter(
    buttons: List<@Composable () -> Unit> = emptyList(), // ✅ [수정] 버튼 리스트 방식
    modifier: Modifier = Modifier
) {
    if (buttons.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ✅ 1~2개 → 세로 정렬
        buttons.take(2).forEach {
            it()
        }

        // ✅ 3~4개 → 아래 줄 좌우 분할
        if (buttons.size > 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                buttons.drop(2).take(2).forEach {
                    Box(modifier = Modifier.weight(1f)) {
                        it()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPalmFooter_TwoVerticalButtons() {
    PalmFooter(
        buttons = listOf(
            {
                PrimaryButton(
                    text = "예",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
            },
            {
                PrimaryButton(
                    text = "아니오",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    )
}
