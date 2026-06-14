package com.example.palmprint_recognition.ui.demo.features.register.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.DemoRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DemoRegisterSuccessUi(
    val message: String = "손바닥 등록을 완료했어요!",
    val name: String = "",
    val userId: Int? = null,
    val palmId: Int? = null
)

@HiltViewModel
class DemoRegisterViewModel @Inject constructor(
    private val demoRepository: DemoRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<DemoRegisterSuccessUi>>(UiState.Idle)
    val state = _state.asStateFlow()

    private var inFlight = false

    fun registerDemoPalmprint(
        name: String,
        base64: String
    ) {
        if (inFlight) return

        viewModelScope.launch {
            inFlight = true
            _state.value = UiState.Loading

            /**
             * 전시/발표용 데모 모드
             * 서버가 닫혀 있으므로 실제 API 호출은 하지 않고,
             * 항상 등록 성공 결과를 표시합니다.
             */

            // 실제 서버 호출 부분 주석 처리
            /*
            runCatching {
                val response = demoRepository.registerDemoPalmprint(
                    name = name,
                    palmprintData = base64
                )

                DemoRegisterSuccessUi(
                    message = "${response.name} 님의 손바닥 등록을 완료했어요!",
                    name = response.name,
                    userId = response.userId,
                    palmId = response.palmId
                )
            }.onSuccess { ui ->
                _state.value = UiState.Success(ui)
            }.onFailure { e ->
                _state.value = UiState.Error(e.message ?: "손바닥 등록 중 오류가 발생했습니다.")
            }
            */

            // 발표용 성공 결과
            val demoName = "홍길동"

            _state.value = UiState.Success(
                DemoRegisterSuccessUi(
                    message = "${demoName} 님의 손바닥 등록을 완료했어요!",
                    name = demoName,
                    userId = 1,
                    palmId = 1
                )
            )

            inFlight = false
        }
    }

    fun clearState() {
        inFlight = false
        _state.value = UiState.Idle
    }
}