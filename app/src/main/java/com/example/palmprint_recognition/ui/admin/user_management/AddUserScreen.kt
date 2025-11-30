package com.example.palmprint_recognition.ui.admin.user_management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 * AddUserScreen
 *
 * 역할
 * - 새로운 유저를 등록하는 화면
 * - ViewModel(addUser) → AdminRepository → API 호출
 * - 성공 시 onAddSuccess(newUserId) 콜백 실행
 *
 * 구조
 * - AddUserScreen(): ViewModel 상태 처리 + 성공 시 콜백 실행
 * - AddUserContent(): UI 전용, Preview 지원
 */
@Composable
fun AddUserScreen(
    onAddSuccess: (Int) -> Unit,
    viewModel: AddUserViewModel = hiltViewModel()
) {
    val uiState by viewModel.addUserState.collectAsStateWithLifecycle()

    // API 성공 시 자동 실행
    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            val newUserId = (uiState as UiState.Success).data.id
            onAddSuccess(newUserId)
        }
    }

    // UI 표시
    AddUserContent(
        uiState = uiState,
        onAddUser = { name, email, password ->
            viewModel.addUser(name, email, password)
        }
    )
}

/**
 * AddUserContent (UI Only)
 *
 * - 입력 UI + 버튼
 * - ViewModel 없음 → Preview 가능
 */
@Composable
fun AddUserContent(
    uiState: UiState<*> = UiState.Loading,
    onAddUser: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isAdminAccount by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("유저 정보 추가", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(20.dp))

        /** 이름 */
        Text("이름")
        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("이름 입력") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        /** 이메일 */
        Text("이메일")
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("example@test.com") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        /** 비밀번호 */
        Text("비밀번호")
        TextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            placeholder = { Text("비밀번호 입력") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        /** 비밀번호 확인 */
        Text("비밀번호 확인")
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            placeholder = { Text("다시 입력") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        /** 관리자 여부 */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isAdminAccount,
                onCheckedChange = { isAdminAccount = it }
            )
            Text("관리자 계정")
        }

        Spacer(Modifier.height(20.dp))

        /** API 에러 표시 */
        if (uiState is UiState.Error) {
            Text(
                text = uiState.message ?: "오류가 발생했습니다.",
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(12.dp))
        }

        /** 추가 버튼 */
        Button(
            onClick = {
                if (password != confirmPassword) {
                    // TODO: 비밀번호 불일치 로컬 메시지
                    return@Button
                }
                onAddUser(name, email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("추가")
        }
    }
}

/**
 * Preview
 */
@Preview(showBackground = true)
@Composable
fun PreviewAddUserScreen() {
    AddUserContent(
        uiState = UiState.Loading,
        onAddUser = { _, _, _ -> }
    )
}
