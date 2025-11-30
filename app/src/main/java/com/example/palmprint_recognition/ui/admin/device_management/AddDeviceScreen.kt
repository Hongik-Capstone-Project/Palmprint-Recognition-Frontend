package com.example.palmprint_recognition.ui.admin.device_management

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 *  AddDeviceScreen
 *
 * - identifier, memo 입력 후 "추가" 버튼 클릭 → 새 디바이스 등록 API 호출
 * - 성공 시 새 디바이스의 id 를 상위(navController)로 전달하여
 *   DeviceDetailScreen 으로 이동할 수 있게 한다.
 *
 * @param onAddSuccess 새 디바이스 등록 성공 시 (deviceId) -> Unit 실행
 */
@Composable
fun AddDeviceScreen(
    onAddSuccess: (Int) -> Unit,
    viewModel: AddDeviceViewModel = hiltViewModel()
) {
    // ViewModel 의 API 호출 상태 구독
    val uiState by viewModel.addDeviceState.collectAsStateWithLifecycle()

    AddDeviceContent(
        uiState = uiState,
        onAddClick = { identifier, memo ->
            viewModel.registerDevice(identifier, memo)
        },
        onAddSuccess = { deviceId ->
            onAddSuccess(deviceId)
        }
    )
}

/**
 * AddDeviceContent (UI Only)
 *
 * - identifier / memo 입력 필드
 * - API 성공 시 onAddSuccess() 자동 호출
 * - UI 테스트를 위해 Preview 가능
 */
@Composable
fun AddDeviceContent(
    uiState: UiState<*> = UiState.Loading,
    onAddClick: (String, String) -> Unit,
    onAddSuccess: (Int) -> Unit
) {
    // 입력 필드 상태
    var identifier by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "디바이스 정보 추가",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(24.dp))

        /** identifier 입력 */
        Text("identifier")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = identifier,
            onValueChange = { identifier = it },
            placeholder = { Text("식별자를 입력하세요") }
        )
        Spacer(Modifier.height(16.dp))

        /** memo 입력 */
        Text("memo")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = memo,
            onValueChange = { memo = it },
            placeholder = { Text("메모를 입력하세요") }
        )
        Spacer(Modifier.height(24.dp))

        /** 에러 메시지 표시 */
        if (uiState is UiState.Error) {
            Text(
                text = uiState.message ?: "오류가 발생했습니다.",
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(12.dp))
        }

        /** 추가 버튼 */
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (identifier.isBlank()) return@Button
                onAddClick(identifier, memo)
            }
        ) {
            Text("추가")
        }

        /** 성공 시 Detail 화면 이동 */
        LaunchedEffect(uiState) {
            if (uiState is UiState.Success) {
                val device = uiState.data
                if (device is DeviceInfo) {
                    onAddSuccess(device.id)
                }
            }
        }
    }
}

/**
 * Preview
 */
@Preview(showBackground = true)
@Composable
fun PreviewAddDeviceContent() {
    AddDeviceContent(
        uiState = UiState.Loading,
        onAddClick = { _, _ -> },
        onAddSuccess = {}
    )
}
