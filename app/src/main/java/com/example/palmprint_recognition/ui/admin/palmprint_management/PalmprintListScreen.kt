package com.example.palmprint_recognition.ui.admin.palmprint_management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.PalmprintInfo
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 * PalmprintListScreen
 *
 * - 특정 사용자(userId)의 손바닥 리스트를 조회하여 화면에 표시한다.
 * - Delete 버튼 클릭 → 상위 네비게이션에서 DeletePalmprintScreen으로 이동
 * - "손바닥 이미지 추가" 버튼 클릭 → UploadPalmprintScreen으로 이동
 *
 * - PalmprintListScreen() : ViewModel 연결 및 상태 구독
 * - PalmprintListContent() : UI 전용 함수 (Preview 가능)
 */
@Composable
fun PalmprintListScreen(
    userId: Int,
    userEmail: String? = null,
    onDeletePalmprintClick: (palmprintId: Int) -> Unit,
    onAddPalmprintClick: () -> Unit,
    onBack: () -> Unit = {},
    viewModel: PalmprintListViewModel = hiltViewModel()
) {
    // ViewModel의 상태 구독
    val state by viewModel.palmprintState.collectAsStateWithLifecycle()

    // 화면 진입 시 API 호출
    LaunchedEffect(userId) {
        viewModel.loadPalmprints(userId)
    }

    // UI 분리
    PalmprintListContent(
        uiState = state,
        userId = userId,
        userEmail = userEmail,
        onDeletePalmprintClick = onDeletePalmprintClick,
        onAddPalmprintClick = onAddPalmprintClick,
        onBack = onBack
    )
}

/**
 * PalmprintListContent
 *
 * - UI 전담 함수
 * - ViewModel 없음 → Preview 가능
 */
@Composable
fun PalmprintListContent(
    uiState: UiState<List<PalmprintInfo>>,
    userId: Int,
    userEmail: String? = null,
    onDeletePalmprintClick: (Int) -> Unit,
    onAddPalmprintClick: () -> Unit,
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // -----------------------
        // 사용자 ID / 이메일 표시
        // -----------------------
        Text(text = "손바닥 목록", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(text = "사용자 ID: $userId")
        Text(text = "사용자 이메일: $userEmail")

        Spacer(Modifier.height(24.dp))

        // -----------------------
        // 상태에 따른 화면 처리
        // -----------------------
        when (uiState) {

            UiState.Idle -> {
                Text("손바닥 목록을 준비 중...")
            }

            UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Text(
                    text = uiState.message ?: "오류가 발생했습니다.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            is UiState.Success -> {
                val list = uiState.data

                // 테이블 헤더
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("손바닥 ID")
                    Text("등록일시")
                    Spacer(Modifier.width(50.dp)) // 삭제 버튼 공간
                }

                Divider()

                // 리스트 출력
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(list) { palmprint ->
                        PalmprintRow(
                            info = palmprint,
                            onDeleteClick = {
                                onDeletePalmprintClick(palmprint.id)
                            }
                        )
                        Divider()
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // -----------------------
        // 손바닥 이미지 추가 버튼
        // -----------------------
        Button(
            onClick = onAddPalmprintClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("손바닥 이미지 추가")
        }
    }
}

/**
 * 리스트 1행 UI (PalmprintRow)
 */
@Composable
fun PalmprintRow(
    info: PalmprintInfo,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = info.id.toString())
        Text(text = info.createdAt)

        Button(
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
fun PreviewPalmprintListContent() {
    val sample = listOf(
        PalmprintInfo(1, 1, "2025-10-10T16:00:00Z"),
        PalmprintInfo(2, 1, "2025-10-09T14:30:00Z")
    )

    PalmprintListContent(
        uiState = UiState.Success(sample),
        userId = 1,
        userEmail = "hong@example.com",
        onDeletePalmprintClick = {},
        onAddPalmprintClick = {}
    )
}
