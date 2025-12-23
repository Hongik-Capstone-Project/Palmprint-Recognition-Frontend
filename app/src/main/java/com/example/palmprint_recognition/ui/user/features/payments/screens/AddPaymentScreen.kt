package com.example.palmprint_recognition.ui.user.features.payments.screens

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
import com.example.palmprint_recognition.ui.user.features.payments.viewmodel.AddPaymentViewModel

/**
 * 결제 수단 추가 Screen
 * @param onAddSuccess 추가 성공 시 PaymentList로 복귀(업데이트된 리스트 표시)
 */
@Composable
fun AddPaymentScreen(
    onAddSuccess: () -> Unit,
    onCancel: () -> Unit = onAddSuccess,
    viewModel: AddPaymentViewModel = hiltViewModel()
) {
    val uiState by viewModel.addPaymentState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        val success = uiState as? UiState.Success
        if (success != null) {
            onAddSuccess()
            viewModel.clearState()
        }
    }

    AddPaymentContent(
        uiState = uiState,
        onAddPayment = { cardName, cardId ->
            viewModel.addPayment(cardName, cardId)
        },
        onCancel = onCancel
    )
}

@Composable
private fun AddPaymentContent(
    uiState: UiState<*> = UiState.Idle,
    onAddPayment: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var cardName by remember { mutableStateOf("") }
    var cardId by remember { mutableStateOf("") }

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
                AddPaymentFieldSection(
                    cardName = cardName,
                    cardId = cardId,
                    onCardNameChange = { cardName = it },
                    onCardIdChange = { cardId = it }
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

                        val name = cardName.trim()
                        val id = cardId.trim()

                        if (name.isBlank()) {
                            localErrorMessage = "card_name을 입력해주세요."
                            return@SingleCenterButton
                        }
                        if (id.isBlank()) {
                            localErrorMessage = "card_id를 입력해주세요."
                            return@SingleCenterButton
                        }

                        onAddPayment(name, id)
                    }
                )
                // 필요하면 취소 버튼 추가 가능
                // Spacer(Modifier.height(8.dp))
                // SingleCenterButton(text = "취소", onClick = onCancel)
            }
        }
    )
}

@Composable
private fun AddPaymentFieldSection(
    cardName: String,
    cardId: String,
    onCardNameChange: (String) -> Unit,
    onCardIdChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "결제 수단 추가")

        LabeledField(
            label = "card_name",
            value = cardName,
            onValueChange = onCardNameChange
        )

        LabeledField(
            label = "card_id",
            value = cardId,
            onValueChange = onCardIdChange
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewAddPaymentContent() {
    AddPaymentContent(
        uiState = UiState.Idle,
        onAddPayment = { _, _ -> },
        onCancel = {}
    )
}
