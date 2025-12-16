package com.example.palmprint_recognition.ui.admin.features.report_management.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.ui.admin.features.report_management.viewmodel.ReportDetailViewModel
import com.example.palmprint_recognition.ui.admin.navigation.AdminRoutes
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.checkbox.CheckBoxGroup
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.Header
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.core.state.UiState

@Composable
fun ReportDetailScreen(
    reportId: Int,
    navController: NavController,
    onSaveSuccess: (() -> Unit)? = null,
    viewModel: ReportDetailViewModel = hiltViewModel()
) {
    val reportState by viewModel.reportState.collectAsStateWithLifecycle()
    val selectedIndex by viewModel.selectedStatusIndex.collectAsStateWithLifecycle()
    val saveState by viewModel.saveState.collectAsStateWithLifecycle()

    LaunchedEffect(reportId) {
        viewModel.loadReport(reportId)
    }

    LaunchedEffect(saveState) {
        if (saveState is UiState.Success) {
            onSaveSuccess?.invoke()
            viewModel.clearSaveState()
        }
    }

    BackHandler {
        navController.navigate(AdminRoutes.REPORT_LIST) {
            popUpTo(AdminRoutes.REPORT_LIST) { inclusive = true }
        }
    }

    when (val ui = reportState) {
        UiState.Idle -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("신고 정보를 준비 중...")
        }

        UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("오류 발생: ${ui.message}")
        }

        is UiState.Success -> {
            val isSaving = saveState is UiState.Loading
            ReportDetailContent(
                report = ui.data,
                selectedIndex = selectedIndex,
                isSaving = isSaving,
                saveErrorMessage = (saveState as? UiState.Error)?.message,
                onSelectStatus = viewModel::onSelectStatus,
                onSaveClick = { viewModel.saveStatus(reportId) }
            )
        }
    }
}

@Composable
private fun ReportDetailContent(
    report: ReportInfo,
    selectedIndex: Int,
    isSaving: Boolean,
    saveErrorMessage: String?,
    onSelectStatus: (Int) -> Unit,
    onSaveClick: () -> Unit
) {
    RootLayout(
        headerWeight = 2f,
        bodyWeight = 6.5f,
        footerWeight = 1.5f,
        sectionGapWeight = 0.4f,
        header = {
            Header(userName = "Alice", userEmail = "alice@example.com")
        },
        body = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ReportInfoFieldSection(report = report)

                ReportInfoBoxSection(report = report)

                ChangeStatusSection(
                    selectedIndex = selectedIndex,
                    readOnly = isSaving,
                    onSelectStatus = onSelectStatus
                )

                saveErrorMessage?.let { Text(text = it) }

                if (isSaving) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) { CircularProgressIndicator() }
                }
            }
        },
        footer = {
            Footer {
                SingleCenterButton(
                    text = if (isSaving) "저장 중..." else "저장",
                    onClick = onSaveClick
                )
            }
        }
    )
}

@Composable
private fun ReportInfoFieldSection(report: ReportInfo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LabeledField(
            label = "신고자 ID",
            value = report.userId.toString(),
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier.weight(1f)
        )

        LabeledField(
            label = "신고일시",
            value = report.createdAt,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier.weight(4f)
        )
    }
}

@Composable
private fun ReportInfoBoxSection(report: ReportInfo) {
    Text(text = "신고내역 상세정보")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
            .border(1.dp, Color(0xFFC1C7CD))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "신고 ID: ${report.id}")
        Text(text = "신고 유형: ${report.reportType}")
        Text(text = "설명: ${report.description}")
        Text(text = "현재 상태: ${report.status}")
    }
}

@Composable
private fun ChangeStatusSection(
    selectedIndex: Int,
    readOnly: Boolean,
    onSelectStatus: (Int) -> Unit
) {
    Text(text = "신고 상태 변경")

    CheckBoxGroup(
        options = ReportDetailViewModel.STATUS_OPTIONS,
        selectedIndex = selectedIndex,
        readOnly = readOnly,
        onSelect = onSelectStatus
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewReportDetailContent() {
    ReportDetailContent(
        report = ReportInfo(
            id = 100,
            createdAt = "2025-12-10T10:00:00.000Z",
            userId = 1,
            reportType = "abuse",
            description = "신고 내용 예시입니다.",
            status = "pending"
        ),
        selectedIndex = 0,
        isSaving = false,
        saveErrorMessage = null,
        onSelectStatus = {},
        onSaveClick = {}
    )
}
