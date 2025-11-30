package com.example.palmprint_recognition.ui.admin.device_management

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 * DeleteDeviceScreen
 *
 * - 디바이스 삭제 여부를 묻는 화면
 * - "예" 클릭 시 → deleteDevice() 호출 → 성공 시 상위(navController)에서 DeviceListScreen으로 이동
 * - "아니오" 클릭 시 → DeviceDetailScreen 으로 복귀(popBackStack)
 *
 * @param deviceId 삭제할 디바이스의 ID
 * @param onConfirmDelete 삭제 성공 시 실행되는 콜백
 * @param onCancel 삭제 취소 시 실행되는 콜백
 */
@Composable
fun DeleteDeviceScreen(
    deviceId: Int,
    onConfirmDelete: () -> Unit,
    onCancel: () -> Unit,
    viewModel: DeleteDeviceViewModel = hiltViewModel()
) {
    val uiState by viewModel.deleteState.collectAsStateWithLifecycle()

    DeleteDeviceContent(
        uiState = uiState,
        onConfirmClick = {
            viewModel.deleteDevice(deviceId)
        },
        onCancelClick = onCancel,
        onDeleteSuccess = {
            onConfirmDelete()
        }
    )
}

/**
 * DeleteDeviceContent (UI 전용)
 *
 * - UI 분리를 통해 Preview 가능
 * - Yes/No 버튼과 텍스트만 포함(기능 중심)
 */
@Composable
fun DeleteDeviceContent(
    uiState: UiState<*> = UiState.Loading,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDeleteSuccess: () -> Unit
) {
    // 삭제 성공 → 이동 처리
    if (uiState is UiState.Success) {
        onDeleteSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "해당 디바이스 정보를\n삭제하시겠습니까?",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(40.dp))

        /** 예 버튼 */
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onClick = { onConfirmClick() }
        ) {
            Text("예")
        }

        Spacer(modifier = Modifier.height(20.dp))

        /** 아니오 버튼 */
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onClick = { onCancelClick() }
        ) {
            Text("아니오")
        }

        /** 에러 메시지 표시 */
        if (uiState is UiState.Error) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = uiState.message ?: "삭제 중 오류가 발생했습니다.",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 *  Preview
 */
@Preview(showBackground = true)
@Composable
fun PreviewDeleteDeviceContent() {
    DeleteDeviceContent(
        uiState = UiState.Loading,
        onConfirmClick = {},
        onCancelClick = {},
        onDeleteSuccess = {}
    )
}
