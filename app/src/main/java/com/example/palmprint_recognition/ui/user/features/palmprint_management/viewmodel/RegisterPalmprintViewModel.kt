package com.example.palmprint_recognition.ui.user.features.palmprint_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.RegisterPalmprintResponse
import com.example.palmprint_recognition.data.repository.UserRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterPalmprintViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<RegisterPalmprintResponse>>(UiState.Idle)
    val state = _state.asStateFlow()

    private var inFlight = false

    fun registerPalmprint(base64: String) {
        if (inFlight) return

        viewModelScope.launch {
            inFlight = true
            _state.value = UiState.Loading

            runCatching {
                userRepository.registerPalmprint(base64)
            }.onSuccess { result ->
                _state.value = UiState.Success(result)
            }.onFailure { e ->
                _state.value = UiState.Error(e.message ?: "손바닥 등록 중 오류가 발생했습니다.")
            }

            inFlight = false
        }
    }

    fun clearState() {
        inFlight = false
        _state.value = UiState.Idle
    }
}
