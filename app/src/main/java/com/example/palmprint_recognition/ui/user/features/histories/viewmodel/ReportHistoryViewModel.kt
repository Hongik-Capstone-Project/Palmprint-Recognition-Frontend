package com.example.palmprint_recognition.ui.user.features.histories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.ReportResponse
import com.example.palmprint_recognition.data.repository.UserRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportHistoryViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _reportState = MutableStateFlow<UiState<ReportResponse>>(UiState.Idle)
    val reportState = _reportState.asStateFlow()

    // 서버에서 409(Already reported)로 판단되면 true
    private val _alreadyReported = MutableStateFlow(false)
    val alreadyReported = _alreadyReported.asStateFlow()

    private var inFlight = false

    fun report(logId: String, reason: String) {
        if (inFlight) return
        if (_alreadyReported.value) return

        viewModelScope.launch {
            inFlight = true
            _reportState.value = UiState.Loading

            runCatching {
                userRepository.reportVerification(logId = logId, reason = reason)
            }.onSuccess { result ->
                _reportState.value = UiState.Success(result)
            }.onFailure { e ->
                val msg = e.message ?: "신고 중 오류가 발생했습니다."

                // 아주 단순하게: 서버의 409 메시지/문구에 따라 alreadyReported 처리
                // (ApiException을 더 자세히 파싱할 수 있으면 그쪽으로 개선 가능)
                val isAlready = msg.contains("Already reported", ignoreCase = true) ||
                        msg.contains("409", ignoreCase = true) ||
                        msg.contains("already", ignoreCase = true) && msg.contains("report", ignoreCase = true)

                if (isAlready) {
                    _alreadyReported.value = true
                }

                _reportState.value = UiState.Error(msg)
            }

            inFlight = false
        }
    }

    fun clearState() {
        inFlight = false
        _reportState.value = UiState.Idle
        // alreadyReported는 유지 (이미 신고된 상태면 화면 내내 비활성 유지)
    }
}
