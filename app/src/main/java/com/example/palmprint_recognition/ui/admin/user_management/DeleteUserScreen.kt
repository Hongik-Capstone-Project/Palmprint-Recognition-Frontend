package com.example.palmprint_recognition.ui.admin.user_management

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 * DeleteUserScreen
 *
 * 역할
 * - 유저 삭제 전 "정말 삭제하시겠습니까?"를 묻는 화면.
 * - "예" → deleteUser() 호출 → UserList로 이동
 * - "아니오" → UserDetailScreen 으로 복귀
 *
 * 분리 구조
 * - DeleteUserScreen()  : ViewModel + 상태 처리
 * - DeleteUserContent() : UI만 포함 (Preview 가능)
 */
@Composable
fun DeleteUserScreen(
    userId: Int,
    onConfirmDelete: () -> Unit, // 삭제 성공 시 → UserListScreen 이동
    onCancel: () -> Unit,        // 버튼 "아니오" 클릭 → UserDetailScreen 복귀
    viewModel: DeleteUserViewModel = hiltViewModel()
) {
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    when (deleteState) {

        UiState.Idle -> {
            // Idle 상태는 "아직 어떤 동작도 하지 않음"
            // → 일반적인 Delete 화면 UI를 그대로 노출
            DeleteUserContent(
                onYesClick = { viewModel.deleteUser(userId) },
                onNoClick = onCancel
            )
        }

        UiState.Loading -> {
            // 삭제 버튼 클릭 후 API 호출 중
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            // API 오류 발생 시 메시지 표시
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("삭제 중 오류발생: ${(deleteState as UiState.Error).message}")

                    Spacer(Modifier.height(20.dp))

                    Button(onClick = onCancel) {
                        Text("뒤로가기")
                    }
                }
            }
        }

        is UiState.Success -> {
            // 삭제 성공 → 유저 리스트 화면으로 이동
            onConfirmDelete()
        }
    }
}

/**
 * DeleteUserContent (UI Only)
 *
 * - Preview 가능
 * - ViewModel과 분리된 순수 UI
 */
@Composable
fun DeleteUserContent(
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 삭제 문구
        Text(
            text = "해당 유저 정보를\n삭제하시겠습니까?",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(40.dp))

        // 예 버튼
        Button(
            onClick = onYesClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text("예")
        }

        Spacer(Modifier.height(16.dp))

        // 아니오 버튼
        Button(
            onClick = onNoClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text("아니오")
        }
    }
}

/**
 * Preview
 */
@Preview(showBackground = true)
@Composable
fun PreviewDeleteUserContent() {
    DeleteUserContent(
        onYesClick = {},
        onNoClick = {}
    )
}
