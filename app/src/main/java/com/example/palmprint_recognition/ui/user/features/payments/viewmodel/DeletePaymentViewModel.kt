package com.example.palmprint_recognition.ui.user.features.payments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.UserRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeletePaymentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _deleteState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteState = _deleteState.asStateFlow()

    private var inFlight = false

    fun deletePaymentMethod(paymentMethodId: Int) {
        if (inFlight) return

        viewModelScope.launch {
            inFlight = true
            _deleteState.value = UiState.Loading

            runCatching {
                userRepository.deletePaymentMethod(paymentMethodId)
            }.onSuccess {
                _deleteState.value = UiState.Success(Unit)
            }.onFailure { e ->
                _deleteState.value =
                    UiState.Error(e.message ?: "결제 수단 삭제 중 오류가 발생했습니다.")
            }

            inFlight = false
        }
    }

    fun clearState() {
        inFlight = false
        _deleteState.value = UiState.Idle
    }
}
