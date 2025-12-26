package com.example.palmprint_recognition.ui.common.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SingleCenterButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    PrimaryButton(
        text = text,
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth()
    )
}
