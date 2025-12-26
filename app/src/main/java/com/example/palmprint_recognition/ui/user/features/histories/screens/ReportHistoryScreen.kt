package com.example.palmprint_recognition.ui.user.features.histories.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.UserVerificationLog
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.common.screens.ResultScreen
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.user.features.histories.viewmodel.ReportHistoryViewModel

@Composable
fun ReportHistoryScreen(
    log: UserVerificationLog,
    onReportSuccess: () -> Unit, // 보통 "메인으로 돌아가기"
    onCancel: () -> Unit = onReportSuccess,
    viewModel: ReportHistoryViewModel = hiltViewModel()
) {
    val reportState by viewModel.reportState.collectAsStateWithLifecycle()
    val alreadyReported by viewModel.alreadyReported.collectAsStateWithLifecycle()

    // 성공 시: 결과 화면(공용)으로 전환
    if (reportState is UiState.Success) {
        ResultScreen(
            message = "신고가 접수되었습니다.\n불편을 드려 죄송합니다",
            buttonText = "메인으로 돌아가기",
            onButtonClick = {
                viewModel.clearState()
                onReportSuccess()
            }
        )
        return
    }

    ReportHistoryContent(
        log = log,
        reportState = reportState,
        alreadyReported = alreadyReported,
        onReport = { reason -> viewModel.report(logId = log.id, reason = reason) },
        onCancel = onCancel
    )
}

@Composable
private fun ReportHistoryContent(
    log: UserVerificationLog,
    reportState: UiState<*>,
    alreadyReported: Boolean,
    onReport: (String) -> Unit,
    onCancel: () -> Unit
) {
    var reason by remember { mutableStateOf("") }
    var localErrorMessage by remember { mutableStateOf<String?>(null) }

    val isLoading = reportState is UiState.Loading
    val serverErrorMessage = (reportState as? UiState.Error)?.message

    val canReport = !alreadyReported && !isLoading

    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = { HeaderContainer() },
        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                ReportHistoryLogSection(log = log)

                // (1) 이미 신고된 상태로 진입하면 ReportReasonSection 숨김
                if (!alreadyReported) {
                    ReportReasonSection(
                        reason = reason,
                        onReasonChange = { reason = it },
                        enabled = !isLoading
                    )
                }

                localErrorMessage?.let { Text(text = it) }
                serverErrorMessage?.let { Text(text = it) }

                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) { CircularProgressIndicator() }
                }
            }
        },
        footer = {
            Footer {
                val buttonText = if (alreadyReported) "이미 신고된 내역입니다" else "신고하기"

                SingleCenterButton(
                    text = buttonText,
                    enabled = canReport,
                    onClick = {
                        if (!canReport) return@SingleCenterButton

                        localErrorMessage = null
                        val trimmed = reason.trim()
                        if (trimmed.isBlank()) {
                            localErrorMessage = "신고 사유를 입력해주세요."
                            return@SingleCenterButton
                        }

                        onReport(trimmed)
                    }
                )

                // 필요하면 취소 버튼도 추가 가능 (현재는 onCancel 파라미터만 유지)
                // Spacer(Modifier.height(8.dp))
                // SingleCenterButton(text = "취소", enabled = !isLoading, onClick = onCancel)
            }
        }
    )
}

@Composable
private fun ReportHistoryLogSection(
    log: UserVerificationLog,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 140.dp)
            .border(1.dp, Color(0xFFC1C7CD))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "인증일시: ${log.createdAt}")
        Text(text = "사용자 ID: ${log.userId}")
        Text(text = "기관 ID: ${log.institutionId}")
        Text(text = "기관명: ${log.institutionName}")
        Text(text = "위치: ${log.location}")
        Text(text = "성공여부: ${if (log.isSuccess) "승인" else "거부"}")
        Text(text = "인증유형: ${log.authType}")
    }
}

@Composable
private fun ReportReasonSection(
    reason: String,
    onReasonChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 140.dp)
            .border(1.dp, Color(0xFFC1C7CD))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "신고 사유")

        BasicTextField(
            value = reason,
            onValueChange = { if (enabled) onReasonChange(it) },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            textStyle = TextStyle(fontSize = 16.sp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    if (reason.isBlank()) {
                        Text(
                            text = "신고 사유를 입력해주세요.",
                            fontSize = 16.sp,
                            color = Color(0xFF9AA0A6)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewReportHistoryContent_NotReported() {
    val log = UserVerificationLog(
        id = "694318c3afdd0d1fc2943a25",
        createdAt = "2025-12-17T20:54:46.606000Z",
        userId = 10,
        institutionId = 1,
        institutionName = "string",
        location = "string",
        isSuccess = true,
        authType = "entry"
    )

    ReportHistoryContent(
        log = log,
        reportState = UiState.Idle,
        alreadyReported = false,
        onReport = {},
        onCancel = {}
    )
}
