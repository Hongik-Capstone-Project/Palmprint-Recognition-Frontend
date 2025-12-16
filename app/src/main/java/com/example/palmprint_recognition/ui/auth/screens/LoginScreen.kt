package com.example.palmprint_recognition.ui.auth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.core.state.UiState

/**
 * 로그인 화면
 *
 * @param role "ADMIN" or "USER"
 */
@Composable
fun LoginScreen(
    role: String,
    onLoginSuccess: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = if (role == "ADMIN") "관리자 로그인" else "사용자 로그인",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        Text("이메일")
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Text("비밀번호")
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(20.dp))


        val state = loginState

        when (state) {
            is UiState.Error -> {
                Text(
                    text = state.message ?: "로그인 실패",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
            }

            UiState.Loading -> {
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
            }

            else -> Unit
        }

        Button(
            onClick = {
                viewModel.onLoginClick(email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그인")
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onBack) {
            Text("뒤로")
        }
    }

    /**
     * 로그인 성공 시 콜백 실행 후 상태 초기화
     */
    LaunchedEffect(loginState) {
        if (loginState is UiState.Success) {
            onLoginSuccess(role)
            viewModel.resetLoginState()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        role = "ADMIN",
        onLoginSuccess = {},
        onBack = {}
    )
}
