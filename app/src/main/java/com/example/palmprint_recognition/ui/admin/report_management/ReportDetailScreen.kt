package com.example.palmprint_recognition.ui.admin.report_management

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 * ReportDetailScreen
 *
 * - ViewModel 을 통해 신고 상세 정보를 로드하고 수정한다.
 * - Navigation 은 직접 하지 않고, 저장 성공 후 단순히 화면 상태 갱신만 한다.
 *
 * @param reportId 상세 조회할 신고 ID
 */
@Composable
fun ReportDetailScreen(
    reportId: Int,
    onSaveSuccess: () -> Unit,
    viewModel: ReportDetailViewModel = hiltViewModel()
) {
    // ViewModel 상태 구독
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 화면 진입 시 신고 상세 조회
    LaunchedEffect(reportId) {
        viewModel.loadReport(reportId)
    }

    when (val ui = state) {

        UiState.Idle,
        UiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(ui.message ?: "오류 발생")
            }
        }

        is UiState.Success -> {
            val report = ui.data
            ReportDetailContent(
                report = report,
                onSaveClick = { updatedStatus ->
                    viewModel.updateReportStatus(
                        reportId = reportId,
                        status = updatedStatus,
                        onSuccess = onSaveSuccess   // ← 저장 성공 시 Navigation 호출!
                    )
                }
            )
        }
    }
}

/**
 * ReportDetailContent (순수 UI)
 *
 * - 신고 상세 텍스트 + 상태 라디오 버튼 선택 + 저장 버튼 UI
 * - Preview 가능하도록 ViewModel 분리
 */
@Composable
fun ReportDetailContent(
    report: ReportInfo,
    onSaveClick: (String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(report.status) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "신고내역 상세정보",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        /** 신고내역 상세정보 박스 */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(4.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("신고 ID: ${report.id}")
                Text("로그 ID: ${report.verificationLogId}")
                Text("사유: ${report.reason}")
                Text("상태: ${report.status}")
                Text("신고자: ${report.user.name} (${report.user.email})")
                Text("신고 시각: ${report.createdAt}")
            }
        }

        Spacer(Modifier.height(24.dp))

        /** -----------------------------
         *  상태 선택 라디오 버튼
         *  ----------------------------- */
        Text("상태 변경")
        Spacer(Modifier.height(12.dp))

        StatusRadioButton(
            label = "처리 전",
            value = "pending",
            selected = selectedStatus == "pending",
            onClick = { selectedStatus = "pending" }
        )

        StatusRadioButton(
            label = "승인",
            value = "approved",
            selected = selectedStatus == "approved",
            onClick = { selectedStatus = "approved" }
        )

        StatusRadioButton(
            label = "기각",
            value = "rejected",
            selected = selectedStatus == "rejected",
            onClick = { selectedStatus = "rejected" }
        )

        Spacer(Modifier.height(24.dp))

        /** 저장 버튼 */
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSaveClick(selectedStatus) }
        ) {
            Text("저장")
        }
    }
}

/**
 * 라디오 버튼 컴포저블
 */
@Composable
fun StatusRadioButton(
    label: String,
    value: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}

/**
 * Preview
 */
@Preview(showBackground = true)
@Composable
fun PreviewReportDetailContent() {
    ReportDetailContent(
        report = ReportInfo(
            id = 1,
            verificationLogId = "ABC123",
            reason = "Unauthorized use detected",
            status = "pending",
            user = AdminUserInfo(id = 1, name = "Alice", email = "alice@example.com"),
            createdAt = "2025-10-07T14:00:00Z"
        ),
        onSaveClick = {}
    )
}
