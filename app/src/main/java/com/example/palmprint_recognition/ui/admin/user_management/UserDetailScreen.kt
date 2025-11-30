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
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 * UserDetailScreen (수정 + 상세 기능 통합)
 *
 * 역할
 * - 특정 userId 의 사용자 상세 정보를 불러온 뒤
 * - 바로 수정할 수 있는 화면
 *
 * 구조
 * - UserDetailScreen() : ViewModel 연결 담당
 * - UserDetailContent() : UI만 담당 (Preview 지원)
 *
 * @param userId 선택한 사용자 ID
 * @param onDeleteClick 삭제 버튼 클릭 시 상위에서 처리할 콜백
 */
@Composable
fun UserDetailScreen(
    userId: Int,
    onDeleteClick: () -> Unit = {},
    onPalmprintListClick: () -> Unit,
    viewModel: UserDetailViewModel = hiltViewModel()
) {
    // ViewModel 의 상태 구독
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 화면 진입 시 사용자 로딩
    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    when (val ui = state) {

        UiState.Idle -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("사용자 정보를 준비 중...")
            }
        }

        UiState.Loading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text(text = "오류 발생: ${ui.message}")
            }
        }

        is UiState.Success -> {
            UserDetailContent(
                user = ui.data,
                onSaveClick = { name, email ->
                    viewModel.updateUser(userId, name, email)
                },
                onDeleteClick = onDeleteClick,
                onPalmprintListClick = onPalmprintListClick
            )
        }
    }
}

/**
 * UserDetailContent (UI 전용, Preview 가능)
 */
@Composable
fun UserDetailContent(
    user: AdminUserInfo,
    onSaveClick: (String?, String?) -> Unit,
    onDeleteClick: () -> Unit,
    onPalmprintListClick: () -> Unit
) {
    // 내부 UI 상태
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var password by remember { mutableStateOf("") }

    var isAdmin by remember { mutableStateOf(false) } // API에는 없지만 UI는 제공

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "유저 정보",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(20.dp))

        // 이름 입력
        Text("이름")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("이름 입력") }
        )
        Spacer(Modifier.height(16.dp))

        // 이메일 입력
        Text("이메일")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("이메일 입력") }
        )
        Spacer(Modifier.height(16.dp))

        // 비밀번호 입력 (서버 API 미지원, UI 요구로만 존재)
        Text("비밀번호")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("비밀번호 입력") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(Modifier.height(16.dp))

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onPalmprintListClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text("손바닥 리스트 보기")
        }

        // 관리자 계정 여부
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isAdmin, onCheckedChange = { isAdmin = it })
            Text(text = "관리자 계정")
        }

        Spacer(Modifier.height(24.dp))

        // 저장 버튼
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSaveClick(name, email) }
        ) {
            Text("저장")
        }

        Spacer(Modifier.height(12.dp))

        // 삭제 버튼
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onDeleteClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text("삭제")
        }
    }
}

/**
 * Preview
 */
@Preview(showBackground = true)
@Composable
fun PreviewUserDetailContent() {
    UserDetailContent(
        user = AdminUserInfo(
            id = 1,
            name = "홍길동",
            email = "hong@example.com"
        ),
        onSaveClick = { _, _ -> },
        onDeleteClick = {},
        onPalmprintListClick = {}
    )
}
