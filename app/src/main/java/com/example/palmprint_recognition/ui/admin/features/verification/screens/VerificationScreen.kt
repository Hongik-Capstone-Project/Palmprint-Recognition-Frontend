package com.example.palmprint_recognition.ui.admin.features.verification.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.VerificationDevice
import com.example.palmprint_recognition.data.model.VerificationRecord
import com.example.palmprint_recognition.data.model.VerificationSummaryResponse
import com.example.palmprint_recognition.data.model.VerificationUser
import com.example.palmprint_recognition.ui.admin.features.verification.viewmodel.VerificationViewModel
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.common.table.TableColumn
import com.example.palmprint_recognition.ui.common.table.TableView
import com.example.palmprint_recognition.ui.core.state.PaginationUiState
import com.example.palmprint_recognition.ui.core.state.UiState

@Composable
fun VerificationScreen(
    viewModel: VerificationViewModel = hiltViewModel()
) {
    val summaryState by viewModel.summaryState.collectAsStateWithLifecycle()
    val listState by viewModel.listState.collectAsStateWithLifecycle()

    VerificationContent(
        summaryState = summaryState,
        listState = listState,
        onLoadMore = viewModel::loadNextPage
    )
}

@Composable
private fun VerificationContent(
    summaryState: UiState<VerificationSummaryResponse>,
    listState: PaginationUiState<VerificationRecord>,
    onLoadMore: () -> Unit
) {
    RootLayout(
        headerWeight = 2f,
        bodyWeight = 8f,
        footerWeight = 0f,
        sectionGapWeight = 0.4f,

        header = {
            HeaderContainer()
        },

        body = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                VerificationSummarySection(summaryState = summaryState)

                VerificationListSection(
                    listState = listState,
                    onLoadMore = onLoadMore
                )
            }
        },

        footer = { /* 없음 */ }
    )
}

/**
 * 상단 요약 섹션 (박스 + 텍스트 4줄)
 */
@Composable
private fun VerificationSummarySection(
    summaryState: UiState<VerificationSummaryResponse>,
    modifier: Modifier = Modifier
) {
    when (val ui = summaryState) {
        UiState.Idle, UiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp)
                    .border(1.dp, Color(0xFFC1C7CD)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFC1C7CD))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "요약 정보를 불러오지 못했습니다.")
                Text(text = ui.message ?: "")
            }
        }

        is UiState.Success -> {
            val s = ui.data
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFC1C7CD))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "전체 유저 수: ${s.totalUsers}명")
                Text(text = "등록된 손바닥 수: ${s.registeredPalms}개")
                Text(text = "인증 요청 수: ${s.totalVerifications}건")
                Text(text = "인증 성공률: ${formatPercent(s.successRate)}")
            }
        }
    }
}

private fun formatPercent(value: Double): String {
    // 서버가 0~100을 주든 0~1을 주든 대응(안전)
    val v = if (value in 0.0..1.0) value * 100.0 else value
    return String.format("%.0f%%", v)
}

/**
 * 하단 목록 섹션 (DeviceList와 동일 구조 + 텍스트만 변경)
 */
@Composable
private fun VerificationListSection(
    listState: PaginationUiState<VerificationRecord>,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = listOf(
        TableColumn(title = "user_id", width = 80),
        TableColumn(title = "기관명", weight = 1f),
        TableColumn(title = "위치", weight = 1f),
        TableColumn(title = "성공 여부", width = 80)
    )

    TableView(
        title = "인증 통계 상세",
        columns = columns,
        rows = listState.items.map { record ->
            listOf(
                record.user.id.toString(),
                record.device.institutionName,
                record.device.location,
                if (record.success) "성공" else "실패"
            )
        },
        hasMoreData = listState.hasMore,
        isLoading = listState.isLoadingInitial || listState.isLoadingMore,
        modifier = modifier.fillMaxWidth(),
        onRowClick = { /* 상세 화면 필요 시 여기에서 처리 */ },
        onLoadMore = onLoadMore
    )

    // 필요하면 에러 메시지 표시(선택)
    listState.errorMessage?.let {
        Spacer(Modifier.height(8.dp))
        Text(text = it)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewVerificationScreen() {
    val summary = UiState.Success(
        VerificationSummaryResponse(
            totalUsers = 10,
            registeredPalms = 7,
            totalVerifications = 120,
            successRate = 92.0
        )
    )

    val list = PaginationUiState(
        items = listOf(
            VerificationRecord(
                id = "abc",
                user = VerificationUser(1, "Alice", "alice@example.com"),
                device = VerificationDevice(10, "홍익대학교", "T동 3층"),
                success = true,
                createdAt = "2025-12-06"
            )
        ),
        isLoadingInitial = false,
        isLoadingMore = false,
        hasMore = true
    )

    VerificationContent(
        summaryState = summary,
        listState = list,
        onLoadMore = {}
    )
}
