package com.example.palmprint_recognition.ui.auth.features.login.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.auth.features.login.viewmodel.LoginViewModel
import com.example.palmprint_recognition.ui.common.button.VerticalTwoButtons
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.common.logo.Logo
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.theme.PalmprintRecognitionTheme
import androidx.compose.material3.Text

@Composable
fun LoginScreen(
    onSignUpClick: () -> Unit,
    authViewModel: AuthViewModel,                 // ✅ AppNavHost에서 내려받음
    viewModel: LoginViewModel = hiltViewModel()
) {
    val form by viewModel.form.collectAsStateWithLifecycle()
    val uiState by viewModel.loginState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            authViewModel.refreshAuthState()      // ✅ “같은 인스턴스” 갱신
            viewModel.resetLoginState()
        }
    }

    LoginContent(
        uiState = uiState,
        email = form.email,
        password = form.password,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = { viewModel.login() },
        onSignUpClick = onSignUpClick
    )
}

@Composable
private fun LoginContent(
    uiState: UiState<*> = UiState.Idle,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val isLoading = uiState is UiState.Loading
    val errorMessage = (uiState as? UiState.Error)?.message

    RootLayout(
        headerWeight = 3f,
        bodyWeight = 3f,
        footerWeight = 4f,
        sectionGapWeight = 0.4f,

        header = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Logo(width = 200.dp)
            }
        },

        body = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                LoginFieldSection(
                    email = email,
                    password = password,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange
                )

                if (!errorMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = errorMessage)
                }
            }
        },

        footer = {
            Footer {
                VerticalTwoButtons(
                    firstText = if (isLoading) "로그인 중..." else "로그인",
                    secondText = "회원가입",
                    onFirstClick = { if (!isLoading) onLoginClick() },
                    onSecondClick = { if (!isLoading) onSignUpClick() },
                )
            }
        }
    )
}

@Composable
private fun LoginFieldSection(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LabeledField(
            label = "이메일",
            value = email,
            onValueChange = onEmailChange
        )

        LabeledField(
            label = "비밀번호",
            value = password,
            onValueChange = onPasswordChange,
            isPassword = true
        )
    }
}

/**
 * ✅ Preview는 hiltViewModel()을 못 쓰므로, LoginContent만 Preview하는 방식 유지
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewLoginContent_Idle() {
    PalmprintRecognitionTheme {
        LoginContent(
            uiState = UiState.Idle,
            email = "",
            password = "",
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onSignUpClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewLoginContent_Error() {
    PalmprintRecognitionTheme {
        LoginContent(
            uiState = UiState.Error("Invalid email or password"),
            email = "user@test.com",
            password = "12345678",
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onSignUpClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewLoginContent_Loading() {
    PalmprintRecognitionTheme {
        LoginContent(
            uiState = UiState.Loading,
            email = "user@test.com",
            password = "12345678",
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onSignUpClick = {}
        )
    }
}
