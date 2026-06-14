package com.example.palmprint_recognition.ui.demo.common

import androidx.compose.runtime.Composable
import com.example.palmprint_recognition.ui.common.layout.Header

@Composable
fun DemoHeaderContainer() {
    Header(
        userName = "홍길동",
        userEmail = "gildong@example.com",
        userRole = "USER"
    )
}