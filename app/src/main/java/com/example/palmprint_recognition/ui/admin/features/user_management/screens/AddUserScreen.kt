package com.example.palmprint_recognition.ui.admin.features.user_management.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel.AddUserViewModel
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.checkbox.CheckBox
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.core.state.UiState

/**
 * 유저 추가 Screen
 *
 * @param onAddSuccess 유저 생성 성공 시 newUserId 전달
 */
@Composable
fun AddUserScreen(
    onAddSuccess: (Int) -> Unit,
    viewModel: AddUserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        val success = uiState as? UiState.Success
        val newUserId = (success?.data)?.id
        if (newUserId != null) {
            onAddSuccess(newUserId)
            viewModel.clearState()
        }
    }

    AddUserContent(
        uiState = uiState,
        onSubmit = { name, email, password, isAdmin ->
            viewModel.addUser(
                name = name,
                email = email,
                password = password,
                isAdmin = isAdmin
            )
        }
    )
}

/**
 * 유저 추가 UI
 */
@Composable
private fun AddUserContent(
    uiState: UiState<*> = UiState.Idle,
    onSubmit: (String, String, String, Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isAdminAccount by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }

    val isLoading = uiState is UiState.Loading
    val serverError = (uiState as? UiState.Error)?.message

    fun validateAndSubmit() {
        localError = null

        val n = name.trim()
        val e = email.trim()

        when {
            n.isBlank() -> localError = "이름을 입력해주세요."
            e.isBlank() -> localError = "이메일을 입력해주세요."
            password.isBlank() -> localError = "비밀번호를 입력해주세요."
            password.length < 8 -> localError = "비밀번호는 8자 이상이어야 합니다."
            password != confirmPassword -> localError = "비밀번호가 일치하지 않습니다."
            else -> onSubmit(n, e, password, isAdminAccount)
        }
    }

    RootLayout(
        headerWeight = 2f,
        bodyWeight = 7f,
        footerWeight = 1f,
        sectionGapWeight = 0.4f,

        header = { HeaderContainer() },

        body = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                AddUserFieldSection(
                    name = name,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    enabled = !isLoading,
                    onNameChange = { name = it },
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it },
                    onConfirmPasswordChange = { confirmPassword = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AdminAccountCheckSection(
                    isAdminAccount = isAdminAccount,
                    enabled = !isLoading,
                    onCheckedChange = { isAdminAccount = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!localError.isNullOrBlank()) {
                    Text(text = localError!!)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (!serverError.isNullOrBlank()) {
                    Text(text = serverError)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        },

        footer = {
            Footer {
                SingleCenterButton(
                    text = if (isLoading) "추가 중..." else "추가",
                    onClick = { if (!isLoading) validateAndSubmit() }
                )
            }
        }
    )
}

/**
 * 유저 입력 필드 Section
 */
@Composable
private fun AddUserFieldSection(
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    enabled: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "유저 정보 추가")

        LabeledField(
            label = "이름",
            value = name,
            onValueChange = { if (enabled) onNameChange(it) }
        )

        LabeledField(
            label = "이메일",
            value = email,
            onValueChange = { if (enabled) onEmailChange(it) }
        )

        LabeledField(
            label = "비밀번호",
            value = password,
            onValueChange = { if (enabled) onPasswordChange(it) },
            isPassword = true
        )

        LabeledField(
            label = "비밀번호 확인",
            value = confirmPassword,
            onValueChange = { if (enabled) onConfirmPasswordChange(it) },
            isPassword = true
        )
    }
}

/**
 * 관리자 계정 체크 Section
 */
@Composable
private fun AdminAccountCheckSection(
    isAdminAccount: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    CheckBox(
        checked = isAdminAccount,
        text = "관리자 계정",
        readOnly = !enabled,
        onCheckedChange = { if (enabled) onCheckedChange(it) }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewAddUserContent() {
    AddUserContent(
        uiState = UiState.Idle,
        onSubmit = { _, _, _, _ -> }
    )
}
