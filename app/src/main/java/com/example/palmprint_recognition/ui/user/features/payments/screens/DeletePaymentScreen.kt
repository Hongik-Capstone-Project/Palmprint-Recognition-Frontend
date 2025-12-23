package com.example.palmprint_recognition.ui.user.features.payments.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.common.button.VerticalTwoButtons
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.user.features.payments.viewmodel.DeletePaymentViewModel

/**
 * 결제 수단 삭제 Screen
 *
 * - "예" → deletePaymentMethod() 호출 → 성공 시 PaymentList로 이동(onConfirmDelete)
 * - "아니오" → 이전 화면(PaymentList)로 복귀(onCancel)
 */
@Composable
fun DeletePaymentScreen(
    paymentMethodId: Int,
    onConfirmDelete: () -> Unit,
    onCancel: () -> Unit,
    viewModel: DeletePaymentViewModel = hiltViewModel()
) {
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()
    val isSuccess = deleteState is UiState.Success

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onConfirmDelete()
            viewModel.clearState()
        }
    }

    DeletePaymentContent(
        uiState = deleteState,
        onYesClick = { viewModel.deletePaymentMethod(paymentMethodId) },
        onNoClick = onCancel
    )
}

/**
 * UI Only (Preview 가능)
 */
@Composable
internal fun DeletePaymentContent(
    uiState: UiState<Unit>,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RootLayoutWeighted(
        headerWeight = 2f,
        bodyWeight = 4f,
        footerWeight = 6f,

        header = {
            // 헤더 없음 (구조 유지용)
        },

        body = {
            DeletePaymentMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        },

        footer = {
            Footer {
                DeletePaymentActionSection(
                    uiState = uiState,
                    onYesClick = onYesClick,
                    onNoClick = onNoClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 24.dp)
                )
            }
        }
    )
}

@Composable
private fun DeletePaymentMessageSection(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "해당 결제 수단을\n삭제하시겠습니까?",
            textAlign = TextAlign.Center,
            fontSize = 26.sp
        )
    }
}

@Composable
private fun DeletePaymentActionSection(
    uiState: UiState<Unit>,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when (uiState) {
            UiState.Idle -> {
                VerticalTwoButtons(
                    firstText = "예",
                    secondText = "아니오",
                    onFirstClick = onYesClick,
                    onSecondClick = onNoClick
                )
            }

            UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Error -> {
                Text(text = uiState.message ?: "결제 수단 삭제 중 오류가 발생했습니다.")
                VerticalTwoButtons(
                    firstText = "다시 시도",
                    secondText = "아니오",
                    onFirstClick = onYesClick,
                    onSecondClick = onNoClick
                )
            }

            is UiState.Success -> {
                // 화면 전환은 Screen의 LaunchedEffect에서 처리
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDeletePaymentContent() {
    DeletePaymentContent(
        uiState = UiState.Idle,
        onYesClick = {},
        onNoClick = {}
    )
}
