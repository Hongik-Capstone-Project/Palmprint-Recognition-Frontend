package com.example.palmprint_recognition.ui.demo.features.home.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.demo.features.home.components.DemoManagementSection
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable

@Composable
fun DemoHomeScreen(
    onRegisterClick: () -> Unit,
    onVerifyClick: () -> Unit,
    onGuideClick: () -> Unit
) {
    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = { HeaderContainer() },
        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                DemoManagementSection(
                    onRegisterClick = onRegisterClick,
                    onVerifyClick = onVerifyClick,
                    onGuideClick = onGuideClick
                )
            }
        }
    )
}