package com.example.palmprint_recognition.ui.common.table

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ✅ 테이블 한 줄(Row)
 * - 기본 높이: 48dp (TableHeader와 동일)
 * - 텍스트 개행 시 Row 높이 자동 증가
 */
@Composable
fun TableRow(
    values: List<String>,
    columns: List<TableColumn>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()

            // ✅✅✅ [수정 핵심]
            .heightIn(min = 48.dp)    // ✅ 기본 높이 48dp + 개행 시 자동 확장

            .border(1.dp, Color(0xFFDDE1E6))
            .clickable { onClick() }

            // ✅ [수정] 기본 높이 안에서 중앙 정렬 유지
            .padding(vertical = 0.dp),

        verticalAlignment = Alignment.CenterVertically // ✅ 기본은 중앙 정렬 유지
    ) {

        values.forEachIndexed { index, value ->

            val col = columns[index]

            val cellModifier = when {
                col.width != null -> Modifier.width(col.width.dp)
                else -> Modifier.weight(col.weight)
            }

            Box(
                modifier = cellModifier
                    .padding(horizontal = 12.dp)
                    .fillMaxHeight(),                 // ✅ Row 높이에 맞게 확장
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = Color(0xFF121619),

                    // ✅✅✅ [유지] 가변 Row 핵심 옵션
                    maxLines = Int.MAX_VALUE,        // 개행 무제한 허용
                    overflow = TextOverflow.Clip    // 말줄임 X
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTableRow_DefaultHeight() {
    val columns = listOf(
        TableColumn("ID", width = 60),
        TableColumn("내용", weight = 1f)
    )

    TableRow(
        values = listOf(
            "1",
            "기본 한 줄 텍스트"
        ),
        columns = columns,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTableRow_MultiLine() {
    val columns = listOf(
        TableColumn("ID", width = 60),
        TableColumn("내용", weight = 1f)
    )

    TableRow(
        values = listOf(
            "2",
            "이 텍스트는 매우 길어서 자동으로 개행이 발생하고\nRow의 높이도 자동으로 커집니다.\nHeader 높이 48dp 기준 유지 확인용입니다."
        ),
        columns = columns,
        onClick = {}
    )
}
