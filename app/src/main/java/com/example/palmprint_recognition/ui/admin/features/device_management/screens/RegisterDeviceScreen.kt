package com.example.palmprint_recognition.ui.admin.features.device_management.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.ui.admin.features.device_management.viewmodel.RegisterDeviceViewModel
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.core.state.UiState

/**
 * 디바이스 등록 Screen
 * @param onAddSuccess 디바이스 등록 성공 시 deviceId 전달
 */
@Composable
fun RegisterDeviceScreen(
    onAddSuccess: (Int) -> Unit,
    viewModel: RegisterDeviceViewModel = hiltViewModel()
) {
    val uiState by viewModel.registerDeviceState.collectAsStateWithLifecycle()

    // ✅ 성공 시 1회만 콜백 실행 + 상태 초기화
    LaunchedEffect(uiState) {
        val success = uiState as? UiState.Success<DeviceInfo>
        if (success != null) {
            onAddSuccess(success.data.id)
            viewModel.clearState()
        }
    }

    RegisterDeviceContent(
        uiState = uiState,
        onSubmit = { idText, institutionName, location ->
            viewModel.submit(idText, institutionName, location)
        }
    )
}

/**
 * UI Only (Preview 가능)
 * - 입력값 상태는 UI가 들고,
 * - 검증/요청은 ViewModel로 위임
 */
@Composable
internal fun RegisterDeviceContent(
    uiState: UiState<DeviceInfo> = UiState.Idle,
    onSubmit: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var idText by remember { mutableStateOf("") }
    var institutionName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val isLoading = uiState is UiState.Loading
    val errorMessage = (uiState as? UiState.Error)?.message

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
                RegisterDeviceFieldSection(
                    idText = idText,
                    institutionName = institutionName,
                    location = location,
                    enabled = !isLoading,
                    onIdChange = { idText = it },
                    onInstitutionNameChange = { institutionName = it },
                    onLocationChange = { location = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!errorMessage.isNullOrBlank()) {
                    Text(text = errorMessage)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        },

        footer = {
            Footer {
                SingleCenterButton(
                    text = if (isLoading) "추가 중..." else "추가",
                    onClick = {
                        if (!isLoading) {
                            onSubmit(idText, institutionName, location)
                        }
                    }
                )
            }
        }
    )
}

@Composable
private fun RegisterDeviceFieldSection(
    idText: String,
    institutionName: String,
    location: String,
    enabled: Boolean,
    onIdChange: (String) -> Unit,
    onInstitutionNameChange: (String) -> Unit,
    onLocationChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "디바이스 정보 추가")

        LabeledField(
            label = "device_id",
            value = idText,
            onValueChange = onIdChange,
            enabled = enabled
        )

        LabeledField(
            label = "기관명",
            value = institutionName,
            onValueChange = onInstitutionNameChange,
            enabled = enabled
        )

        LabeledField(
            label = "위치",
            value = location,
            onValueChange = onLocationChange,
            enabled = enabled
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewRegisterDeviceContent() {
    RegisterDeviceContent(
        uiState = UiState.Idle,
        onSubmit = { _, _, _ -> }
    )
}
