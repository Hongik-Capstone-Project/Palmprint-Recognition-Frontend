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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.user.features.histories.viewmodel.ReportHistoryViewModel

@Composable
fun ReportHistoryScreen(
    log: UserVerificationLog,
    onReportSuccess: () -> Unit,
    onCancel: () -> Unit = onReportSuccess,
    viewModel: ReportHistoryViewModel = hiltViewModel()
) {
    val reportState by viewModel.reportState.collectAsStateWithLifecycle()
    val alreadyReported by viewModel.alreadyReported.collectAsStateWithLifecycle()

    val isSuccess = reportState is UiState.Success
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onReportSuccess()
            viewModel.clearState()
        }
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

                // ✅ (1) 이미 신고된 상태로 진입하면 ReportReasonSection 숨김
                if (!alreadyReported) {
                    ReportReasonSection(
                        reason = reason,
                        onReasonChange = { reason = it },
                        enabled = !isLoading
                    )
                }

                // 로컬/서버 에러
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
                // ✅ (2) 이미 신고된 상태면 버튼 텍스트 변경 + 비활성화
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

/**
 * ✅ 화면 전용 입력 박스
 * - ReportHistoryLogSection과 "동일한 박스 스타일" 적용
 * - multiline + 내용이 길어지면 height가 자연스럽게 늘어나도록 minHeight만 지정
 * - LabeledField 사용 안 함
 */
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewReportHistoryContent_AlreadyReported() {
    val log = UserVerificationLog(
        id = "694318c3afdd0d1fc2943a25",
        createdAt = "2025-12-17T20:54:46.606000Z",
        userId = 10,
        institutionId = 1,
        institutionName = "string",
        location = "string",
        isSuccess = false,
        authType = "entry"
    )

    ReportHistoryContent(
        log = log,
        reportState = UiState.Error("Already reported"),
        alreadyReported = true,
        onReport = {},
        onCancel = {}
    )
}
