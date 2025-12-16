package com.example.palmprint_recognition.ui.common.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


// ==============================
// ✅ Segmented Button 컴포넌트
// ==============================

@Composable
fun PalmSegmentedButton(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFFF2F4F8))
            .border(1.dp, Color(0xFFC1C7CD)),
    ) {
        options.forEach { option ->

            val isSelected = option == selectedOption

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (isSelected) Color(0xFF21272A) else Color.Transparent
                    )
                    .clickable { onOptionSelected(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (isSelected) Color.White else Color(0xFF21272A)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PalmSegmentedButtonPreview() {
    var status by remember { mutableStateOf("처리 전") }

    PalmSegmentedButton(
        options = listOf("처리 전", "승인", "기각"),
        selectedOption = status,
        onOptionSelected = { status = it },
        modifier = Modifier.padding(16.dp)
    )
}
