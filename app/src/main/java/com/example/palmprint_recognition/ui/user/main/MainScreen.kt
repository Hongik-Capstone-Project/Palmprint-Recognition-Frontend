package com.example.palmprint_recognition.ui.user.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 사용자 메인 화면
 */
@Composable
fun MainScreen(
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("사용자 메인 화면", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그아웃")
        }

        Button(
            onClick = onDeleteAccountClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("회원탈퇴")
        }
    }
}
