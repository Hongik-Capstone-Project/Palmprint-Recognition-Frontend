package com.example.palmprint_recognition.ui.demo.features.verify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.DemoRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DemoVerifySuccessUi(
    val message: String,
    val matched: Boolean,
    val name: String? = null,
    val similarityScore: Double? = null
)

@HiltViewModel
class DemoVerifyViewModel @Inject constructor(
    private val demoRepository: DemoRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<DemoVerifySuccessUi>>(UiState.Idle)
    val state = _state.asStateFlow()

    private var inFlight = false

    fun verifyDemoPalmprint(base64: String) {
        if (inFlight) return

        viewModelScope.launch {
            inFlight = true
            _state.value = UiState.Loading

            runCatching {
                val response = demoRepository.verifyDemoPalmprint(
                    palmprintData = base64
                )

                val successMessage = if (response.matched) {
                    buildString {
                        append("인증 성공")
                        response.name?.let { append("\n이름: $it") }
                        response.similarityScore?.let {
                            append("\n유사도: ${"%.2f".format(it)}")
                        }
                    }
                } else {
                    "일치하는 손바닥 정보를 찾지 못했습니다."
                }

                DemoVerifySuccessUi(
                    message = successMessage,
                    matched = response.matched,
                    name = response.name,
                    similarityScore = response.similarityScore
                )
            }.onSuccess { ui ->
                _state.value = UiState.Success(ui)
            }.onFailure { e ->
                _state.value = UiState.Error(e.message ?: "손바닥 인증 중 오류가 발생했습니다.")
            }

            inFlight = false
        }
    }

    fun clearState() {
        inFlight = false
        _state.value = UiState.Idle
    }
}