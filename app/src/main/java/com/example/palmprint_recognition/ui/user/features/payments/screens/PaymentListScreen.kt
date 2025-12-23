package com.example.palmprint_recognition.ui.user.features.payments.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.data.model.PaymentMethod
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.common.table.TableColumn
import com.example.palmprint_recognition.ui.common.table.TableView
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.user.features.payments.viewmodel.PaymentListViewModel

@Composable
fun PaymentListScreen(
    onPaymentMethodClick: (Int) -> Unit, // paymentMethodId 전달 (DeletePayment으로)
    onAddPaymentClick: () -> Unit,
    viewModel: PaymentListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadPaymentMethods()
    }

    when (val ui = state) {
        UiState.Idle -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("결제 수단 정보를 준비 중...")
        }

        UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("오류 발생: ${ui.message}")
        }

        is UiState.Success -> {
            PaymentListContent(
                paymentMethods = ui.data,
                onPaymentMethodClick = onPaymentMethodClick,
                onAddPaymentClick = onAddPaymentClick
            )
        }
    }
}

@Composable
private fun PaymentListContent(
    paymentMethods: List<PaymentMethod>,
    onPaymentMethodClick: (Int) -> Unit,
    onAddPaymentClick: () -> Unit
) {
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
                PaymentListTableSectionAll(
                    paymentMethods = paymentMethods,
                    modifier = Modifier.fillMaxWidth(),
                    onPaymentMethodClick = onPaymentMethodClick
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        },

        footer = {
            Footer {
                SingleCenterButton(
                    text = "결제 수단 추가",
                    onClick = onAddPaymentClick
                )
            }
        }
    )
}

/**
 * 결제 수단 목록 테이블 Section (전부 출력 / 스크롤 없음)
 * - row 클릭 시 해당 결제 수단 삭제 화면으로 이동
 */
@Composable
private fun PaymentListTableSectionAll(
    paymentMethods: List<PaymentMethod>,
    modifier: Modifier = Modifier,
    onPaymentMethodClick: (Int) -> Unit
) {
    val columns = listOf(
        TableColumn(title = "card_name", weight = 1f),
        TableColumn(title = "card_id", weight = 2f)
    )

    val rows = paymentMethods.map { pm ->
        listOf(pm.cardName, pm.cardId)
    }

    Box(modifier = modifier) {
        TableView(
            title = "내 결제 수단 목록",
            columns = columns,
            rows = rows,
            hasMoreData = false,
            isLoading = false,
            modifier = Modifier.fillMaxWidth(),
            onRowClick = { index ->
                val paymentMethodId = paymentMethods.getOrNull(index)?.id ?: return@TableView
                onPaymentMethodClick(paymentMethodId)
            },
            onLoadMore = {}, // 호출되지 않음 (안전)
            scrollEnabled = false
        )
    }
}
