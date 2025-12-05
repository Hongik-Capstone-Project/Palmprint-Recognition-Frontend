package com.example.palmprint_recognition.ui.common.components.table

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 테이블의 한 줄(Row)
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
            .width(360.dp)
            .height(48.dp)
            .border(1.dp, Color(0xFFDDE1E6))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        values.forEachIndexed { index, value ->

            val col = columns[index]

            val cellModifier = when {
                col.width != null -> Modifier.width(col.width.dp)
                else -> Modifier.weight(col.weight)
            }

            Box(
                modifier = cellModifier
                    .fillMaxHeight()
                    .padding(start = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = Color(0xFF121619)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTableRow() {
    val columns = listOf(
        TableColumn("user_id", width = 60),
        TableColumn("email", weight = 1f)
    )
    TableRow(
        values = listOf("1", "alice@example.com"),
        columns = columns,
        onClick = {}
    )
}
