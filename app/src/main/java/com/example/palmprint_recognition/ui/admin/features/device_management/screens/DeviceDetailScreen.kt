package com.example.palmprint_recognition.ui.admin.features.device_management.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.ui.admin.features.device_management.viewmodel.DeviceDetailViewModel
import com.example.palmprint_recognition.ui.admin.navigation.AdminRoutes
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.core.state.UiState

/**
 * 디바이스 상세 Screen
 *
 * @param deviceId 조회할 디바이스 ID
 * @param navController 뒤로가기 처리용 NavController
 * @param onDeleteClick 삭제 버튼 클릭 이벤트
 */
@Composable
fun DeviceDetailScreen(
    deviceId: Int,
    navController: NavController,
    onDeleteClick: () -> Unit,
    viewModel: DeviceDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ✅ deviceId가 바뀔 때만 재조회
    LaunchedEffect(deviceId) {
        viewModel.loadDevice(deviceId)
    }

    DeviceDetailBody(
        uiState = state,
        onDeleteClick = onDeleteClick
    )

    // ✅ 뒤로가기: 디바이스 목록으로 스택 정리하며 복귀
    BackHandler {
        navController.navigate(AdminRoutes.DEVICE_LIST) {
            popUpTo(AdminRoutes.DEVICE_LIST) { inclusive = true }
            launchSingleTop = true
        }
    }
}

@Composable
private fun DeviceDetailBody(
    uiState: UiState<DeviceInfo>,
    onDeleteClick: () -> Unit
) {
    when (uiState) {
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
                Text(text = uiState.message ?: "디바이스 정보를 불러오지 못했습니다.")
            }
        }

        is UiState.Success -> {
            DeviceDetailContent(
                device = uiState.data,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

/**
 * UI Only (Preview 가능)
 */
@Composable
internal fun DeviceDetailContent(
    device: DeviceInfo,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RootLayout(
        headerWeight = 2f,
        bodyWeight = 7f,
        footerWeight = 1f,
        sectionGapWeight = 0.4f,

        header = { HeaderContainer() },

        body = {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top
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
            value = device.institutionName,
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


         LabeledField(
             label = "등록일시",
             value = device.createdAt,
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
            institutionName = "홍익대학교",
            location = "T동 3층",
            createdAt = "2025-12-06T09:55:50.741Z"
        ),
        onDeleteClick = {}
    )
}
