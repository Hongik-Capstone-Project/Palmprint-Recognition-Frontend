package com.example.palmprint_recognition.ui.user.features.palmprint_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.UserRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterPalmprintSuccessUi(
    val message: String = "손바닥 등록을 완료했어요!",
    val totalCount: Int? = null
)

@HiltViewModel
class RegisterPalmprintViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<RegisterPalmprintSuccessUi>>(UiState.Idle)
    val state = _state.asStateFlow()

    private var inFlight = false

    fun registerPalmprint(base64: String) {
        if (inFlight) return

        viewModelScope.launch {
            inFlight = true
            _state.value = UiState.Loading

            runCatching {
                // 1) 등록(POST)
                val registerRes = userRepository.registerPalmprint(base64)

                // 2) 등록 직후 최신 개수 반영을 위해 GET 1번 호출
                //    (서버 호출 최소화 조건에서도 “등록했을 때”는 호출 허용)
                val palmsRes = userRepository.getMyPalms()

                RegisterPalmprintSuccessUi(
                    message = registerRes.message.ifBlank { "손바닥 등록을 완료했어요!" },
                    totalCount = palmsRes.totalCount
                )
            }.onSuccess { ui ->
                _state.value = UiState.Success(ui)
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
