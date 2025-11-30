package com.example.palmprint_recognition.ui.admin.report_management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 * ReportListScreen
 *
 * - ViewModel(ReportListViewModel)을 구독하여 신고 내역 리스트를 가져온다.
 * - 상태(UiState)에 따라 Loading / Error / Success 화면을 분기 처리한다.
 * - Navigation 은 하지 않으며, 클릭 이벤트(onReportClick)는 부모에서 받는다.
 *
 * @param onReportClick 신고 항목 클릭 시 (reportId) 전달
 */
@Composable
fun ReportListScreen(
    onReportClick: (Int) -> Unit,
    viewModel: ReportListViewModel = hiltViewModel()
) {
    // ViewModel → Compose UI 로 상태(StateFlow) 구독
    val uiState by viewModel.reportListState.collectAsStateWithLifecycle()

    ReportListContent(
        state = uiState,
        onReportClick = onReportClick
    )
}

/**
 * ================================================================================
 * 🟩 ReportListContent (순수 UI 전용)
 * ================================================================================
 *
 * ✔ Preview 를 위해 ViewModel과 분리
 * ✔ 상태에 따른 분기 및 UI 렌더링
 *
 * @param state 신고 내역을 담는 UiState
 * @param onReportClick 리스트 항목 클릭 콜백
 */
@Composable
fun ReportListContent(
    state: UiState<List<ReportInfo>>,
    onReportClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        /** 제목 영역 */
        Text(
            text = "신고 내역 목록",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        /** ───────────────────────────────
         *  상태(UiState)에 따른 UI 분기
         *  ─────────────────────────────── */
        when (state) {

            UiState.Idle,
            UiState.Loading -> {
                Text("불러오는 중…")
            }

            is UiState.Error -> {
                Text(
                    text = state.message ?: "오류가 발생했습니다.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            is UiState.Success -> {
                val reports = state.data

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(reports, key = { it.id }) { report ->
                        ReportRowItem(
                            report = report,
                            onClick = { onReportClick(report.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

/**
 * ================================================================================
 * 🟧 ReportRowItem
 * ================================================================================
 *
 * ✔ 신고내역 리스트 한 줄 Row
 */
@Composable
fun ReportRowItem(
    report: ReportInfo,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            // 첫 번째 줄 : 신고 사유
            Text(
                text = report.reason,
                style = MaterialTheme.typography.bodyLarge
            )

            // 두 번째 줄 : 신고 상태 + 신고한 유저 정보
            Text(
                text = "${report.status} · ${report.user.name}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * ================================================================================
 * 🟨 Preview
 * ================================================================================
 */
@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewReportListContent() {
    val sampleReports = listOf(
        ReportInfo(
            id = 1,
            verificationLogId = "650f7c1a8e4b3f0012345678",
            reason = "Unauthorized use detected",
            status = "pending",
            user = com.example.palmprint_recognition.data.model.AdminUserInfo(
                id = 1,
                name = "Alice",
                email = "alice@example.com"
            ),
            createdAt = "2025-10-07T14:00:00Z"
        ),
        ReportInfo(
            id = 2,
            verificationLogId = "650f7c1a8e4b3f0012345679",
            reason = "Suspicious activity",
            status = "approved",
            user = com.example.palmprint_recognition.data.model.AdminUserInfo(
                id = 2,
                name = "Bob",
                email = "bob@example.com"
            ),
            createdAt = "2025-10-06T10:30:00Z"
        )
    )

    ReportListContent(
        state = UiState.Success(sampleReports),
        onReportClick = {}
    )
}
