package com.example.palmprint_recognition.ui.common.button

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 가로 버튼 2개 그룹 (동일 폭)
 * @param left 오른쪽/왼쪽 버튼 슬롯
 * @param right 오른쪽 버튼 슬롯
 */
@Composable
fun HorizontalTwoButtons(
    modifier: Modifier = Modifier,
    left: @Composable () -> Unit,
    right: @Composable () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) { left() }
        Box(modifier = Modifier.weight(1f)) { right() }
    }
}
