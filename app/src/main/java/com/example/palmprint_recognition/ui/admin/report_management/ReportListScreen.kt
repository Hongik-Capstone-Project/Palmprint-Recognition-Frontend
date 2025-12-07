package com.example.palmprint_recognition.ui.admin.report_management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.ui.admin.common.PaginationUiState
import com.example.palmprint_recognition.ui.common.components.LogoPalmAI
import com.example.palmprint_recognition.ui.common.components.ProfileCard
import com.example.palmprint_recognition.ui.common.table.TableColumn
import com.example.palmprint_recognition.ui.common.table.TableView

/**
 * ViewModel 연결된 ReportList 화면
 */
@Composable
fun ReportListScreen(
    onReportClick: (Int) -> Unit,
    viewModel: ReportListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReportListContent(
        uiState = uiState,
        onReportClick = onReportClick,
        onLoadMore = { viewModel.loadNextPage() }
    )
}

/**
 * 순수 UI 전용
 */
@Composable
fun ReportListContent(
    uiState: PaginationUiState<ReportInfo>,
    onReportClick: (Int) -> Unit,
    onLoadMore: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // 1) 로고
        LogoPalmAI(
            modifier = Modifier.offset(x = 20.dp, y = 0.dp)
        )

        // 2) 프로필 카드
        ProfileCard(
            name = "OO",
            email = "email",
            modifier = Modifier
                .offset(x = 17.dp, y = 89.dp)
                .width(368.dp)
                .height(48.dp)
        )

        // 3) 타이틀
        Text(
            text = "신고 내역 목록",
            style = Typography.titleMedium.copy(
                fontSize = 14.sp,
                color = Color(0xFF21272A)
            ),
            modifier = Modifier.offset(x = 28.dp, y = 189.dp)
        )

        // 4) 테이블
        val tableColumns = listOf(
            TableColumn("신고자 ID", width = 80),
            TableColumn("처리 상태", weight = 1f)
        )

        TableView(
            columns = tableColumns,
            rows = uiState.items.map { report ->
                listOf(
                    report.user.id.toString(),
                    report.status
                )
            },
            hasMoreData = uiState.hasMore,
            isLoading = uiState.isLoadingMore,
            modifier = Modifier.offset(x = 25.dp, y = 222.dp),

            onRowClick = { index ->
                val reportId = uiState.items[index].id
                onReportClick(reportId)
            },
            onLoadMore = onLoadMore
        )
    }
}

/**
 * Preview
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewReportListScreen() {

    val sample = PaginationUiState(
        items = listOf(
            ReportInfo(
                id = 1,
                verificationLogId = "abc",
                reason = "불법 사용 의심",
                status = "pending",
                user = com.example.palmprint_recognition.data.model.AdminUserInfo(
                    id = 10,
                    name = "Alice",
                    email = "alice@example.com"
                ),
                createdAt = "2024-01-01"
            ),
            ReportInfo(
                id = 2,
                verificationLogId = "def",
                reason = "이상 패턴 탐지",
                status = "approved",
                user = com.example.palmprint_recognition.data.model.AdminUserInfo(
                    id = 22,
                    name = "Bob",
                    email = "bob@example.com"
                ),
                createdAt = "2024-01-02"
            ),
        ),
        isLoadingInitial = false,
        isLoadingMore = false,
        hasMore = true
    )

    ReportListContent(
        uiState = sample,
        onReportClick = {},
        onLoadMore = {}
    )
}
