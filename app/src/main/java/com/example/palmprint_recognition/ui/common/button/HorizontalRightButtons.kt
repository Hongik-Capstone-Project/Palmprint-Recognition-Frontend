package com.example.palmprint_recognition.ui.common.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 오른쪽 정렬 가로 버튼 2개 그룹
 * - 왼쪽 공간은 비우고, 두 버튼만 오른쪽에 배치
 */
@Composable
fun HorizontalRightButtons(
    left: @Composable () -> Unit,
    right: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        left()
        Spacer(modifier = Modifier.width(12.dp))
        right()
    }
}
