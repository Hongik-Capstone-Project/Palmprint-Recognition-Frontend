package com.example.palmprint_recognition.ui.demo.features.guide.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable

@Composable
fun DemoGuideScreen() {
    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = {
            HeaderContainer()
        },
        body = {
            // 임시 빈 화면
        },
        footer = {
            Footer {
                // 임시 빈 영역
            }
        }
    )
}