package com.example.palmprint_recognition.ui.common.table

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TableHeader(
    columns: List<TableColumn>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()                         // ✅ [수정] 화면 너비 대응
            .height(48.dp)
            .background(Color(0xFFF2F4F8))
            .border(1.dp, Color(0xFFDDE1E6)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        columns.forEach { col ->

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
                    text = col.title,
                    fontSize = 14.sp,
                    color = Color(0xFF121619)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTableHeader() {
    val columns = listOf(
        TableColumn("user_id", width = 60),
        TableColumn("email", weight = 1f)
    )
    TableHeader(columns)
}
