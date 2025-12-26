package com.example.palmprint_recognition.ui.common.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.common.button.PrimaryButton
import com.example.palmprint_recognition.ui.common.logo.Logo

@Composable
fun ResultScreen(
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RootLayoutWeighted(
        headerWeight = 2f,
        bodyWeight = 4f,
        footerWeight = 6f,
        header = { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Logo() } },
        body = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = message)
            }
        },
        footer = {
            Footer {
                PrimaryButton(
                    text = buttonText,
                    onClick = onButtonClick
                )
            }
        }
    )
}
