package com.example.palmprint_recognition.ui.admin.features.user_management.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel.AddUserViewModel
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.Header
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
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
    val uiState by viewModel.addUserState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        val success = uiState as? UiState.Success<*>
        val newUserId =
            (success?.data as? com.example.palmprint_recognition.data.model.AddUserResponse)?.id
        if (newUserId != null) {
            onAddSuccess(newUserId)
            viewModel.clearState()
        }
    }

    AddUserContent(
        uiState = uiState,
        onAddUser = { name, email, password ->
            viewModel.addUser(name = name, email = email, password = password)
        }
    )
}

/**
 * 유저 추가 UI (Scrollable)
 */
@Composable
private fun AddUserContent(
    uiState: UiState<*> = UiState.Idle,
    onAddUser: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var localErrorMessage by remember { mutableStateOf<String?>(null) }

    val isLoading = uiState is UiState.Loading
    val serverErrorMessage = (uiState as? UiState.Error)?.message

    RootLayoutScrollable(
        sectionGap = 12.dp,

        header = {
            HeaderContainer()
        },

        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                AddUserFieldSection(
                    name = name,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    onNameChange = { name = it },
                    onEmailChange = { email = it },
                    onPasswordChange = { password = it },
                    onConfirmPasswordChange = { confirmPassword = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (localErrorMessage != null) {
                    Text(text = localErrorMessage!!)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (!serverErrorMessage.isNullOrBlank()) {
                    Text(text = serverErrorMessage)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },

        footer = {
            Footer {
                SingleCenterButton(
                    text = if (isLoading) "추가 중..." else "추가",
                    onClick = {
                        if (isLoading) return@SingleCenterButton

                        localErrorMessage = null

                        if (password != confirmPassword) {
                            localErrorMessage = "비밀번호가 일치하지 않습니다."
                            return@SingleCenterButton
                        }

                        onAddUser(name, email, password)
                    }
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
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "유저 정보 추가")

        LabeledField(label = "이름", value = name, onValueChange = onNameChange)
        LabeledField(label = "이메일", value = email, onValueChange = onEmailChange)

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
private fun PreviewAddUserContent() {
    AddUserContent(
        uiState = UiState.Idle,
        onAddUser = { _, _, _ -> }
    )
}
