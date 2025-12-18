package com.example.palmprint_recognition.ui.admin.features.device_management.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.admin.features.device_management.viewmodel.DeleteDeviceViewModel
import com.example.palmprint_recognition.ui.common.button.VerticalTwoButtons
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.core.state.UiState

/**
 * 디바이스 삭제 Screen
 *
 * - "예" → deleteDevice(deviceId) 호출 → 성공 시 onConfirmDelete()
 * - "아니오" → onCancel()
 */
@Composable
fun DeleteDeviceScreen(
    deviceId: Int,
    onConfirmDelete: () -> Unit,
    onCancel: () -> Unit,
    viewModel: DeleteDeviceViewModel = hiltViewModel()
) {
    val uiState by viewModel.deleteState.collectAsStateWithLifecycle()

    // ✅ 성공 시 1회만 처리 + 상태 초기화
    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) {
            onConfirmDelete()
            viewModel.clearState()
        }
    }

    DeleteDeviceContent(
        uiState = uiState,
        onYesClick = { viewModel.deleteDevice(deviceId) },
        onNoClick = onCancel
    )
}

/**
 * UI Only (Preview 가능)
 */
@Composable
internal fun DeleteDeviceContent(
    uiState: UiState<Unit> = UiState.Idle,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoading = uiState is UiState.Loading
    val errorMessage = (uiState as? UiState.Error)?.message

    RootLayout(
        headerWeight = 2f,
        bodyWeight = 4f,
        footerWeight = 6f,
        sectionGapWeight = 0.4f,

        header = { /* 헤더 없음 */ },

        body = {
            DeleteDeviceMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        },

        footer = {
            Footer {
                DeleteDeviceActionSection(
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onYesClick = onYesClick,
                    onNoClick = onNoClick,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 24.dp)
                )
            }
        }
    )
}

@Composable
private fun DeleteDeviceMessageSection(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "해당 디바이스 정보를\n삭제하시겠습니까?",
            textAlign = TextAlign.Center,
            fontSize = 26.sp
        )
    }
}

@Composable
private fun DeleteDeviceActionSection(
    isLoading: Boolean,
    errorMessage: String?,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!errorMessage.isNullOrBlank()) {
            Text(text = errorMessage)
        }

        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            else -> {
                VerticalTwoButtons(
                    firstText = if (errorMessage.isNullOrBlank()) "예" else "다시 시도",
                    secondText = "아니오",
                    onFirstClick = onYesClick,
                    onSecondClick = onNoClick
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDeleteDeviceContent() {
    DeleteDeviceContent(
        uiState = UiState.Idle,
        onYesClick = {},
        onNoClick = {}
    )
}
