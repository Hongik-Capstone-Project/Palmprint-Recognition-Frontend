package com.example.palmprint_recognition.ui.admin.device_management

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 * DeviceDetailScreen (조회 + 수정 통합 화면)
 *
 * @param deviceId 상세 조회될 디바이스 ID
 * @param onDeleteClick "삭제" 클릭 → DeleteDeviceScreen 으로 이동
 */
@Composable
fun DeviceDetailScreen(
    deviceId: Int,
    onDeleteClick: () -> Unit,
    viewModel: DeviceDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 화면 진입 시 디바이스 정보 로딩
    LaunchedEffect(deviceId) {
        viewModel.loadDevice(deviceId)
    }

    when (val ui = state) {

        UiState.Idle -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("디바이스 정보를 준비 중...")
            }
        }

        UiState.Loading -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("오류 발생: ${ui.message}")
            }
        }

        is UiState.Success -> {
            DeviceDetailContent(
                device = ui.data,
                onSaveClick = { identifier, memo ->
                    viewModel.updateDevice(deviceId, identifier, memo)

                    // ✔ 업데이트 후 최신 데이터 다시 조회
                    viewModel.loadDevice(deviceId)
                },

                onDeleteClick = onDeleteClick
            )
        }
    }
}

/**
 * DeviceDetailContent (UI Only, Preview 가능)
 */
@Composable
fun DeviceDetailContent(
    device: DeviceInfo,
    onSaveClick: (String?, String?) -> Unit,
    onDeleteClick: () -> Unit
) {
    var identifier by remember { mutableStateOf(device.identifier) }
    var memo by remember { mutableStateOf(device.memo) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "디바이스 정보",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(20.dp))

        /** identifier */
        Text("identifier")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = identifier,
            onValueChange = { identifier = it },
            placeholder = { Text("identifier 입력") }
        )

        Spacer(Modifier.height(16.dp))

        /** memo */
        Text("memo")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = memo,
            onValueChange = { memo = it },
            placeholder = { Text("메모 입력") }
        )

        Spacer(Modifier.height(24.dp))

        /** 저장 버튼 */
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSaveClick(identifier, memo) }
        ) {
            Text("저장")
        }

        Spacer(Modifier.height(12.dp))

        /** 삭제 버튼 */
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onDeleteClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text("삭제")
        }
    }
}

/**
 * Preview
 */
@Preview(showBackground = true)
@Composable
fun PreviewDeviceDetailContent() {
    DeviceDetailContent(
        device = DeviceInfo(
            id = 1,
            identifier = "DEVICE-12345",
            memo = "실험실용 센서 장치",
            createdAt = "2025-10-10T16:00:00Z"
        ),
        onSaveClick = { _, _ -> },
        onDeleteClick = {}
    )
}
