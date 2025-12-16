package com.example.palmprint_recognition.ui.admin.features.report_management.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.ui.admin.features.report_management.viewmodel.ReportListViewModel
import com.example.palmprint_recognition.ui.common.layout.Header
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.common.table.TableColumn
import com.example.palmprint_recognition.ui.common.table.TableView
import com.example.palmprint_recognition.ui.core.state.PaginationUiState

/**
 * 신고 내역 목록 Screen
 *
 * @param onReportClick 신고 클릭 시 상세 화면 이동
 * @param viewModel ReportListViewModel
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
        onLoadMore = viewModel::loadNextPage
    )
}

/**
 * 신고 내역 목록 UI
 */
@Composable
private fun ReportListContent(
    uiState: PaginationUiState<ReportInfo>,
    onReportClick: (Int) -> Unit,
    onLoadMore: () -> Unit
) {
    RootLayout(

        // DeviceListScreen과 동일한 비율
        headerWeight = 2f,
        bodyWeight = 8f,
        footerWeight = 0f,
        sectionGapWeight = 0.4f,

        header = {
            Header(
                userName = "OO",
                userEmail = "email"
            )
        },

        body = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                ReportListTableSection(
                    uiState = uiState,
                    onReportClick = onReportClick,
                    onLoadMore = onLoadMore,
                    modifier = Modifier.weight(1f)
                )
            }
        },

        footer = {
            // 신고 목록 화면에는 Footer 없음
        }
    )
}

/**
 * 신고 내역 테이블 Section
 */
@Composable
private fun ReportListTableSection(
    uiState: PaginationUiState<ReportInfo>,
    onReportClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = listOf(
        TableColumn(
            title = "신고자 ID",
            weight = 1f
        ),
        TableColumn(
            title = "처리 상태",
            weight = 1f
        )
    )

    TableView(
        title = "신고 내역 목록",
        columns = columns,
        rows = uiState.items.map { report ->
            listOf(
                report.userId.toString(),
                report.status
            )
        },
        hasMoreData = uiState.hasMore,
        isLoading = uiState.isLoadingInitial || uiState.isLoadingMore,
        modifier = modifier.fillMaxWidth(),
        onRowClick = { index ->
            onReportClick(uiState.items[index].id)
        },
        onLoadMore = onLoadMore
    )
}

/**
 * Preview
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewReportListScreen() {
    val mockState = PaginationUiState(
        items = listOf(
            ReportInfo(
                id = 1,
                createdAt = "2025-12-06",
                userId = 101,
                reportType = "오탐지",
                description = "인증 실패",
                status = "pending"
            )
        ),
        isLoadingInitial = false,
        isLoadingMore = false,
        hasMore = true
    )

    ReportListContent(
        uiState = mockState,
        onReportClick = {},
        onLoadMore = {}
    )
}

