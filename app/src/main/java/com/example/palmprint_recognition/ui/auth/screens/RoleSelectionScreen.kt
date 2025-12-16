package com.example.palmprint_recognition.ui.auth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 역할 선택 화면
 *
 * - "일반 사용자" / "관리자" 중 하나를 고르게 한다.
 * - 실제 네비게이션은 상위(AuthNavigation)에서 처리.
 */
@Composable
fun RoleSelectionScreen(
    onUserLoginClick: () -> Unit,
    onAdminLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("로그인 유형을 선택하세요")

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onUserLoginClick
        ) {
            Text("일반 사용자 로그인")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAdminLoginClick
        ) {
            Text("관리자 로그인")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSignUpClick
        ) {
            Text("회원가입")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRoleSelectionScreen() {
    RoleSelectionScreen(
        onUserLoginClick = {},
        onAdminLoginClick = {},
        onSignUpClick = {}
    )
}
