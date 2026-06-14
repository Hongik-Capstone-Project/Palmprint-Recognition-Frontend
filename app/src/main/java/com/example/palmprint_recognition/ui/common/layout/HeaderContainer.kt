package com.example.palmprint_recognition.ui.common.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.palmprint_recognition.ui.auth.AuthViewModel

/**
 * HeaderContainer
 * - AuthViewModel(authState)을 구독하고
 * - Header에 userName/userEmail/userRole을 내려주는 "연결 컴포넌트"
 */
@Composable
fun HeaderContainer() {
    Header(
        userName = "홍길동",
        userEmail = "gildong@example.com",
        userRole = "USER"
    )
}