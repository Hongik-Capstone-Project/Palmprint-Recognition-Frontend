package com.example.palmprint_recognition.ui.admin.features.device_management.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.palmprint_recognition.data.model.AdminInstitution
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.ui.admin.features.device_management.viewmodel.DeviceDetailViewModel
import com.example.palmprint_recognition.ui.admin.navigation.AdminRoutes
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.Header
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.core.state.UiState

/**
 * 디바이스 상세 Screen
 *
 * @param deviceId 조회할 디바이스 ID
 * @param navController 뒤로가기 처리용 NavController
 * @param onDeleteClick 삭제 버튼 클릭 이벤트
 * @param viewModel DeviceDetailViewModel
 */
@Composable
fun DeviceDetailScreen(
    deviceId: Int,
    navController: NavController,
    onDeleteClick: () -> Unit,
    viewModel: DeviceDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(deviceId) {
        viewModel.loadDevice(deviceId)
    }

    when (val ui = state) {
        UiState.Idle -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("디바이스 정보를 준비 중...")
            }
        }

        UiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("오류 발생: ${ui.message}")
            }
        }

        is UiState.Success -> {
            DeviceDetailContent(
                device = ui.data,
                onDeleteClick = onDeleteClick
            )
        }
    }

    BackHandler {
        navController.navigate(AdminRoutes.DEVICE_LIST) {
            popUpTo(AdminRoutes.DEVICE_LIST) { inclusive = true }
        }
    }
}

/**
 * 디바이스 상세 UI
 */
@Composable
private fun DeviceDetailContent(
    device: DeviceInfo,
    onDeleteClick: () -> Unit
) {
    RootLayoutWeighted(
        headerWeight = 2f,
        bodyWeight = 7f,
        footerWeight = 1f,
        header = {
            HeaderContainer()
        },
        body = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                DeviceInfoFieldSection(
                    device = device,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        footer = {
            Footer {
                SingleCenterButton(
                    text = "삭제",
                    onClick = onDeleteClick
                )
            }
        }
    )
}

/**
 * 디바이스 정보 필드 Section (읽기 전용)
 */
@Composable
private fun DeviceInfoFieldSection(
    device: DeviceInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "디바이스 정보")

        LabeledField(
            label = "device_id",
            value = device.id.toString(),
            onValueChange = {},
            readOnly = true,
            enabled = false
        )

        LabeledField(
            label = "기관명",
            value = device.institution.name, // 변경 (institutionName -> institution.name)
            onValueChange = {},
            readOnly = true,
            enabled = false
        )

        LabeledField(
            label = "위치",
            value = device.location,
            onValueChange = {},
            readOnly = true,
            enabled = false
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDeviceDetailContent() {
    DeviceDetailContent(
        device = DeviceInfo(
            id = 1,
            createdAt = "2025-12-06T09:55:50.741Z",
            institution = AdminInstitution(
                id = 10,
                createdAt = "2025-12-01T00:00:00.000Z",
                name = "홍익대학교",
                address = null
            ),
            location = "T동 3층"
        ),
        onDeleteClick = {}
    )
}
