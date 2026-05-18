package com.example.palmprint_recognition.ui.demo.features.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.common.button.PrimaryButton

@Composable
fun DemoManagementSection(
    onRegisterClick: () -> Unit,
    onVerifyClick: () -> Unit,
    onGuideClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = "데모 메뉴",
        modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF796E6E))
            .padding(horizontal = 16.dp, vertical = 25.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        DemoMenuButton(
            text = "손바닥 등록",
            onClick = onRegisterClick
        )

        DemoMenuButton(
            text = "손바닥 인증",
            onClick = onVerifyClick
        )

        DemoMenuButton(
            text = "설정",
            onClick = onGuideClick
        )
    }
}

@Composable
fun DemoMenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sideWeight = 29f
    val centerWeight = 298f

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(sideWeight))

        PrimaryButton(
            text = text,
            onClick = onClick,
            modifier = Modifier.weight(centerWeight),
            width = null,
            height = 62.dp,
            backgroundColor = Color(0xFFDDE1E6),
            borderColor = Color(0xFF697077),
            textColor = Color(0xFF21272A),
            textSize = 16
        )

        Spacer(modifier = Modifier.weight(sideWeight))
    }
}