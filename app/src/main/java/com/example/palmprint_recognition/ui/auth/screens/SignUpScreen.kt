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
 * SignUpScreen
 *
 * - 이름 / 이메일 / 비밀번호 입력
 * - 회원가입 버튼 클릭 → AuthViewModel.onSignUpClick 호출
 * - 회원가입 성공하면 onSignUpSuccess() 콜백 실행
 */
@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }

    SignUpContent(
        name = name,
        email = email,
        password = password,
        passwordConfirm = passwordConfirm,
        onNameChange = { name = it },
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onPasswordConfirmChange = { passwordConfirm = it },
        signUpState = signUpState,
        onSignUpClick = {
            if (password != passwordConfirm) {
                // 비밀번호 불일치 처리
                return@SignUpContent
            }
            viewModel.onSignUpClick(name, email, password)
        },
        onBack = onBack
    )

    /**
     * 회원가입 성공 → 상위 AuthNavigation 에게 전달
     */
    LaunchedEffect(signUpState) {
        if (signUpState is UiState.Success) {
            onSignUpSuccess()
            viewModel.resetSignUpState()
        }
    }
}

/**
 * SignUpContent (순수 UI)
 */
@Composable
fun SignUpContent(
    name: String,
    email: String,
    password: String,
    passwordConfirm: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    signUpState: UiState<*>,
    onSignUpClick: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("회원가입", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(20.dp))

        Text("이름")
        TextField(value = name, onValueChange = onNameChange, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(12.dp))

        Text("이메일")
        TextField(value = email, onValueChange = onEmailChange, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(12.dp))

        Text("비밀번호")
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(12.dp))

        Text("비밀번호 확인")
        TextField(
            value = passwordConfirm,
            onValueChange = onPasswordConfirmChange,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(20.dp))

        when (signUpState) {
            is UiState.Error -> {
                Text(
                    text = signUpState.message ?: "회원가입 실패",
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
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("회원가입")
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onBack) { Text("뒤로") }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpContent() {
    SignUpContent(
        name = "",
        email = "",
        password = "",
        passwordConfirm = "",
        onNameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onPasswordConfirmChange = {},
        signUpState = UiState.Idle,
        onSignUpClick = {},
        onBack = {}
    )
}
