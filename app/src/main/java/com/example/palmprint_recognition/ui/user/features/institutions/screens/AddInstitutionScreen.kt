package com.example.palmprint_recognition.ui.user.features.institutions.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.user.features.institutions.viewmodel.AddInstitutionViewModel

@Composable
fun AddInstitutionScreen(
    onAddSuccess: () -> Unit,
    onCancel: () -> Unit = onAddSuccess,
    viewModel: AddInstitutionViewModel = hiltViewModel()
) {
    val uiState by viewModel.addInstitutionState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        val success = uiState as? UiState.Success
        if (success != null) {
            onAddSuccess()
            viewModel.clearState()
        }
    }

    AddInstitutionContent(
        uiState = uiState,
        onAddInstitution = { institutionId, institutionUserId ->
            viewModel.addInstitution(institutionId, institutionUserId)
        },
        onCancel = onCancel
    )
}

@Composable
private fun AddInstitutionContent(
    uiState: UiState<*> = UiState.Idle,
    onAddInstitution: (Int, String) -> Unit,
    onCancel: () -> Unit
) {
    var institutionIdText by remember { mutableStateOf("") }
    var institutionUserId by remember { mutableStateOf("") }

    var localErrorMessage by remember { mutableStateOf<String?>(null) }

    val isLoading = uiState is UiState.Loading
    val serverErrorMessage = (uiState as? UiState.Error)?.message

    RootLayoutScrollable(
        sectionGap = 12.dp,

        header = {
            HeaderContainer()
        },

        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                AddInstitutionFieldSection(
                    institutionIdText = institutionIdText,
                    institutionUserId = institutionUserId,
                    onInstitutionIdChange = { institutionIdText = it },
                    onInstitutionUserIdChange = { institutionUserId = it }
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

                        val instUserId = institutionUserId.trim()
                        if (instUserId.isBlank()) {
                            localErrorMessage = "institution_user_id를 입력해주세요."
                            return@SingleCenterButton
                        }

                        onAddInstitution(institutionId, instUserId)
                    }
                )
                // 취소 버튼 필요하면 추가
                // Spacer(Modifier.height(8.dp))
                // SingleCenterButton(text="취소", onClick=onCancel)
            }
        }
    )
}

@Composable
private fun AddInstitutionFieldSection(
    institutionIdText: String,
    institutionUserId: String,
    onInstitutionIdChange: (String) -> Unit,
    onInstitutionUserIdChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "내 인증기관 추가")

        LabeledField(
            label = "institution_id",
            value = institutionIdText,
            onValueChange = onInstitutionIdChange
        )

        LabeledField(
            label = "institution_user_id",
            value = institutionUserId,
            onValueChange = onInstitutionUserIdChange
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewAddInstitutionContent() {
    AddInstitutionContent(
        uiState = UiState.Idle,
        onAddInstitution = { _, _ -> },
        onCancel = {}
    )
}
