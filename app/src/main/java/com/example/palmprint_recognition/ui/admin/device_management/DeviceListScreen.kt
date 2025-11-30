package com.example.palmprint_recognition.ui.admin.device_management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 * DeviceListScreen
 *
 * - ViewModel(DeviceListViewModel)을 연결하여 디바이스 목록을 불러온다.
 * - 상태(UiState)에 따라 로딩/에러/성공 UI를 DeviceListContent 에 전달한다.
 * - navigation 은 하지 않으며, 상위에서 전달된 콜백(onDeviceClick)에만 의존한다.
 *
 * @param onDeviceClick 디바이스 선택 시 호출되는 콜백
 * @param onAddDeviceClick 디바이스 추가 버튼 클릭 콜백
 */
@Composable
fun DeviceListScreen(
    onDeviceClick: (Int) -> Unit,
    onAddDeviceClick: () -> Unit,
    viewModel: DeviceListViewModel = hiltViewModel()
) {
    // ViewModel의 StateFlow → Compose 상태로 구독
    val state by viewModel.deviceListState.collectAsStateWithLifecycle()

    DeviceListContent(
        state = state,
        onDeviceClick = onDeviceClick,
        onAddDeviceClick = onAddDeviceClick
    )
}

/**
 * DeviceListContent
 *
 * - UI 그리기 전용 함수 → Preview 가능
 * - 상태(UiState<DeviceInfo>) 에 따라 화면을 다르게 표시한다.
 */
@Composable
fun DeviceListContent(
    state: UiState<List<DeviceInfo>>,
    onDeviceClick: (Int) -> Unit,
    onAddDeviceClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        /** -------------------------------
         *  화면 제목
         *  ------------------------------- */
        Text(
            text = "디바이스 목록",  // TODO: strings.xml 로 분리
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        /** -------------------------------
         *  상태(UiState)에 따라 화면 분기
         *  ------------------------------- */
        when (state) {
            UiState.Idle -> {
                // 초기 진입 / 아직 데이터 요청 전 상태
                Text("디바이스 목록을 준비하는 중...")
            }

            UiState.Loading -> {
                Text("불러오는 중...")
            }

            is UiState.Error -> {
                Text(
                    text = state.message ?: "오류가 발생했습니다.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            is UiState.Success -> {
                val deviceList = state.data

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(deviceList, key = { it.id }) { device ->
                        DeviceItemRow(
                            device = device,
                            onClick = { onDeviceClick(device.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        /** -------------------------------
         *  디바이스 추가 버튼
         *  ------------------------------- */
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddDeviceClick
        ) {
            Text("디바이스 추가")
        }
    }
}

/**
 * DeviceItemRow
 *
 * -디바이스 1개의 정보를 표시하는 단순 버튼 UI
 */
@Composable
fun DeviceItemRow(
    device: DeviceInfo,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(device.identifier, style = MaterialTheme.typography.bodyLarge)
            Text(device.memo, style = MaterialTheme.typography.bodySmall)
        }
    }
}

/**
 * Preview
 */
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PreviewDeviceListScreen() {
    val sampleDevices = listOf(
        DeviceInfo(1, "DEVICE-12345", "실험실용 센서 장치", "2025-03-01"),
        DeviceInfo(2, "DEVICE-67890", "사무실용 센서 장치", "2025-02-28")
    )

    DeviceListContent(
        state = UiState.Success(sampleDevices),
        onDeviceClick = {},
        onAddDeviceClick = {}
    )
}
