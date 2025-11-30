package com.example.palmprint_recognition.ui.admin.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.admin.common.UiState
import com.example.palmprint_recognition.data.model.VerificationRecord
import com.example.palmprint_recognition.data.model.AdminUserInfo

/**
 * VerificationListScreen
 *
 * 역할
 * - 인증 내역 리스트를 서버에서 불러오고 ViewModel 상태를 관찰하여 UI 업데이트
 * - 실제 UI는 아래 VerificationListContent() 에서 책임진다
 *
 * Navigation
 * - 뒤로가기 버튼은 없음 → 시스템 하단바 “뒤로가기”로 Dashboard 화면 복귀
 */
@Composable
fun VerificationListScreen(
    onBack: () -> Unit,
    viewModel: VerificationListViewModel = hiltViewModel()
) {
    val uiState by viewModel.verificationState.collectAsStateWithLifecycle()

    // 화면 진입 시 API 호출
    LaunchedEffect(Unit) {
        viewModel.fetchVerificationList()
    }

    VerificationListContent(
        uiState = uiState,
        onRetry = { viewModel.fetchVerificationList() }
    )
}

/**
 * VerificationListContent (UI 전담)
 *
 * - Preview 를 위해 순수 UI만 포함
 * - 실제 데이터는 Preview에서 dummy 값 전달
 */
@Composable
fun VerificationListContent(
    uiState: UiState<*>,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "인증 내역 목록",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        when (uiState) {

            UiState.Idle -> {
                Text("초기 상태입니다.")
            }

            UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is UiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.message ?: "오류가 발생했습니다.",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = onRetry) { Text("다시 시도") }
                }
            }

            is UiState.Success<*> -> {
                val data = uiState.data as List<VerificationRecord>

                VerificationTable(records = data)
            }
        }
    }
}

/**
 * VerificationTable (리스트 테이블 UI)
 *
 * - Figma 와이어프레임 기반 테이블 UI
 * - 인증내역, 사용자명, 이메일, 상태, 생성일 등을 표시할 수 있으나
 *   지금은 Cell Text 형태로 간단히 표시
 */
@Composable
fun VerificationTable(records: List<VerificationRecord>) {

    /* --- 테이블 헤더 --- */
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE9ECF2))
            .padding(vertical = 12.dp)
    ) {
        Text(
            "인증내역",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f).padding(start = 16.dp)
        )
        Text(
            "사용자",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f).padding(start = 16.dp)
        )
    }

    /* --- 리스트 데이터 --- */
    LazyColumn {
        items(records) { record ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp)
            ) {
                Text(
                    text = record.status,
                    modifier = Modifier.weight(1f).padding(start = 16.dp)
                )
                Text(
                    text = record.user.email,
                    modifier = Modifier.weight(1f).padding(start = 16.dp)
                )
            }
        }
    }
}

/**
 * Preview
 */
@Preview(showBackground = true)
@Composable
fun PreviewVerificationListContent() {
    val dummy = listOf(
        VerificationRecord(
            id = "1",
            user = AdminUserInfo(1, "홍길동", "hong@example.com"),
            status = "approved",
            createdAt = "2025-10-10T16:00:00Z"
        ),
        VerificationRecord(
            id = "2",
            user = AdminUserInfo(2, "김철수", "kim@example.com"),
            status = "rejected",
            createdAt = "2025-10-11T10:00:00Z"
        )
    )
    VerificationListContent(
        uiState = UiState.Success(dummy),
        onRetry = {}
    )
}
