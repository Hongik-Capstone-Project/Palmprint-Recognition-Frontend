package com.example.palmprint_recognition.ui.admin.features.device_management.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.admin.features.device_management.viewmodel.RegisterDeviceViewModel
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.Header
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.core.state.UiState

/**
 * 디바이스 등록 Screen
 * @param onAddSuccess 디바이스 등록 성공 시 newDeviceId 전달
 */
@Composable
fun RegisterDeviceScreen(
    onAddSuccess: (Int) -> Unit,
    viewModel: RegisterDeviceViewModel = hiltViewModel()
) {
    val uiState by viewModel.registerDeviceState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        val success = uiState as? UiState.Success
        if (success != null) {
            onAddSuccess(success.data.id)
            viewModel.clearState()
        }
    }

    RegisterDeviceContent(
        uiState = uiState,
        onRegisterDevice = { institutionId, location ->
            viewModel.registerDevice(institutionId, location)
        }
    )
}

@Composable
private fun RegisterDeviceContent(
    uiState: UiState<*> = UiState.Idle,
    onRegisterDevice: (Int, String) -> Unit
) {
    var institutionIdText by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    var localErrorMessage by remember { mutableStateOf<String?>(null) }

    val isLoading = uiState is UiState.Loading
    val serverErrorMessage = (uiState as? UiState.Error)?.message

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
                RegisterDeviceFieldSection(
                    institutionIdText = institutionIdText,
                    location = location,
                    onInstitutionIdChange = { institutionIdText = it },
                    onLocationChange = { location = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                localErrorMessage?.let {
                    Text(text = it)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                serverErrorMessage?.let {
                    Text(text = it)
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
                    text = "추가",
                    onClick = {
                        localErrorMessage = null

                        val institutionId = institutionIdText.trim().toIntOrNull()
                        if (institutionId == null) {
                            localErrorMessage = "institution_id는 숫자만 입력해주세요."
                            return@SingleCenterButton
                        }

                        if (location.isBlank()) {
                            localErrorMessage = "위치를 입력해주세요."
                            return@SingleCenterButton
                        }

                        onRegisterDevice(institutionId, location.trim())
                    }
                )
            }
        }
    )
}

@Composable
private fun RegisterDeviceFieldSection(
    institutionIdText: String,
    location: String,
    onInstitutionIdChange: (String) -> Unit,
    onLocationChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "디바이스 정보 추가")

        LabeledField(
            label = "institution_id",
            value = institutionIdText,
            onValueChange = onInstitutionIdChange
        )

        LabeledField(
            label = "위치",
            value = location,
            onValueChange = onLocationChange
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewRegisterDeviceContent() {
    RegisterDeviceContent(
        uiState = UiState.Idle,
        onRegisterDevice = { _, _ -> }
    )
}
