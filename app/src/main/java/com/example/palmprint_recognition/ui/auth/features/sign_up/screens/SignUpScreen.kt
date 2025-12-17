package com.example.palmprint_recognition.ui.auth.features.sign_up.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.auth.features.sign_up.viewmodel.SignUpViewModel
import com.example.palmprint_recognition.ui.common.button.VerticalTwoButtons
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.common.logo.Logo
import com.example.palmprint_recognition.ui.core.state.UiState
import androidx.compose.ui.tooling.preview.Preview
import com.example.palmprint_recognition.ui.theme.PalmprintRecognitionTheme

/**
 * 회원가입 Screen
 *
 * @param onSignUpClick 회원가입 성공 시 호출 (보통 Login 화면으로 이동)
 * @param onBackClick 뒤로가기 클릭 시 호출
 * @param viewModel SignUpViewModel
 */
@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val form by viewModel.form.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            // 회원가입 성공 → 상위 네비게이션에서 Login으로 보내기
            viewModel.resetState()
            onSignUpClick()
        }
    }

    SignUpContent(
        uiState = uiState,
        name = form.name,
        email = form.email,
        password = form.password,
        confirmPassword = form.confirmPassword,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onSignUpClick = {
            viewModel.signUp()
        },
        onBackClick = onBackClick
    )
}

/**
 * 회원가입 화면 UI
 */
@Composable
private fun SignUpContent(
    uiState: UiState<*> = UiState.Idle,
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val isLoading = uiState is UiState.Loading
    val errorMessage = (uiState as? UiState.Error)?.message

    RootLayout(
        headerWeight = 2f,
        bodyWeight = 5f,
        footerWeight = 3f,
        sectionGapWeight = 0.4f,

        // ===============================
        // HEADER (로고만)
        // ===============================
        header = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Logo(width = 180.dp)
            }
        },

        // ===============================
        // BODY (입력 필드)
        // ===============================
        body = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                SignUpFieldSection(
                    name = name,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    onNameChange = onNameChange,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange
                )

                if (!errorMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    androidx.compose.material3.Text(text = errorMessage)
                }
            }
        },

        // ===============================
        // FOOTER (회원가입 / 뒤로가기)
        // ===============================
        footer = {
            Footer {
                VerticalTwoButtons(
                    firstText = if (isLoading) "회원가입 중..." else "회원가입",
                    secondText = "뒤로 가기",
                    onFirstClick = {
                        if (!isLoading) onSignUpClick()
                    },
                    onSecondClick = {
                        if (!isLoading) onBackClick()
                    },

                )
            }
        }
    )
}

/**
 * 회원가입 정보 입력 필드 Section
 */
@Composable
private fun SignUpFieldSection(
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LabeledField(
            label = "이름",
            value = name,
            onValueChange = onNameChange
        )

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

        LabeledField(
            label = "비밀번호 확인",
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            isPassword = true
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewSignUpContent_Idle() {
    PalmprintRecognitionTheme {
        SignUpContent(
            uiState = UiState.Idle,
            name = "",
            email = "",
            password = "",
            confirmPassword = "",
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onSignUpClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewSignUpContent_Error() {
    PalmprintRecognitionTheme {
        SignUpContent(
            uiState = UiState.Error("Email already exists"),
            name = "홍길동",
            email = "user@test.com",
            password = "12345678",
            confirmPassword = "12345678",
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onSignUpClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewSignUpContent_Loading() {
    PalmprintRecognitionTheme {
        SignUpContent(
            uiState = UiState.Loading,
            name = "홍길동",
            email = "user@test.com",
            password = "12345678",
            confirmPassword = "12345678",
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onSignUpClick = {},
            onBackClick = {}
        )
    }
}
