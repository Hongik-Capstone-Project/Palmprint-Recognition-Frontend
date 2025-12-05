package com.example.palmprint_recognition.ui.common.components.table

import androidx.compose.runtime.Immutable

@Immutable
data class TableColumn(
    val title: String,
    val width: Int? = null,    // widthDp → null이면 weight 사용
    val weight: Float = 1f
)

