package com.example.palmprint_recognition.ui.admin.features.device_management.screens

import androidx.compose.foundation.layout.*
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
 * - "예" → deleteDevice() 호출 → 성공 시 DEVICE_LIST로 이동(onConfirmDelete)
 * - "아니오" → DEVICE_DETAIL로 복귀(onCancel)
 */
@Composable
fun DeleteDeviceScreen(
    deviceId: Int,
    onConfirmDelete: () -> Unit,
    onCancel: () -> Unit,
    viewModel: DeleteDeviceViewModel = hiltViewModel()
) {
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    LaunchedEffect(deleteState) {
        if (deleteState is UiState.Success) {
            onConfirmDelete()
            viewModel.clearState()
        }
    }

    DeleteDeviceContent(
        uiState = deleteState,
        onYesClick = { viewModel.deleteDevice(deviceId) },
        onNoClick = onCancel
    )
}

/**
 * UI Only (Preview 가능)
 */
@Composable
internal fun DeleteDeviceContent(
    uiState: UiState<Unit>,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RootLayout(
        headerWeight = 2f,
        bodyWeight = 4f,
        footerWeight = 6f,
        sectionGapWeight = 0.4f,

        header = {
            // 헤더 없음 (구조 유지용)
        },

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
                    uiState = uiState,
                    onYesClick = onYesClick,
                    onNoClick = onNoClick,
                    modifier = Modifier
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
    uiState: UiState<Unit>,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when (uiState) {
            UiState.Idle -> {
                VerticalTwoButtons(
                    firstText = "예",
                    secondText = "아니오",
                    onFirstClick = onYesClick,
                    onSecondClick = onNoClick
                )
            }

            UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Error -> {
                Text(text = uiState.message ?: "디바이스 삭제 중 오류가 발생했습니다.")
                VerticalTwoButtons(
                    firstText = "다시 시도",
                    secondText = "아니오",
                    onFirstClick = onYesClick,
                    onSecondClick = onNoClick
                )
            }

            is UiState.Success -> {
                // 화면 전환은 Screen의 LaunchedEffect에서 처리
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
