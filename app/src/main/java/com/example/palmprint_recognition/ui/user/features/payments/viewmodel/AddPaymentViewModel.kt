package com.example.palmprint_recognition.ui.user.features.payments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.PaymentMethod
import com.example.palmprint_recognition.data.repository.UserRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPaymentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _addPaymentState =
        MutableStateFlow<UiState<PaymentMethod>>(UiState.Idle)
    val addPaymentState = _addPaymentState.asStateFlow()

    fun addPayment(cardName: String, cardId: String) {
        viewModelScope.launch {
            _addPaymentState.value = UiState.Loading

            runCatching {
                userRepository.addPaymentMethod(cardName = cardName, cardId = cardId)
            }.onSuccess { result ->
                _addPaymentState.value = UiState.Success(result)
            }.onFailure { e ->
                _addPaymentState.value =
                    UiState.Error(e.message ?: "결제 수단 추가 중 오류가 발생했습니다.")
            }
        }
    }

    fun clearState() {
        _addPaymentState.value = UiState.Idle
    }
}
