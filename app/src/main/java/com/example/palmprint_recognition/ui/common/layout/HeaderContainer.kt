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
fun HeaderContainer(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    // 비로그인 상태에서는 헤더를 숨기거나, 기본값을 표시할 수 있음
    val name = authState.name ?: "-"
    val email = authState.email ?: "-"
    val role = authState.role

    Header(
        userName = name,
        userEmail = email,
        userRole = role
    )
}
