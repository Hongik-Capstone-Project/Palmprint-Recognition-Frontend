package com.example.palmprint_recognition.ui.user.features.palmprint_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.PalmInfo
import com.example.palmprint_recognition.data.repository.UserRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeletePalmprintViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // 1) 목록 상태
    private val _palmsState = MutableStateFlow<UiState<List<PalmInfo>>>(UiState.Idle)
    val palmsState = _palmsState.asStateFlow()

    // 2) 삭제 액션 상태 (개별/전체 공통)
    private val _actionState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val actionState = _actionState.asStateFlow()

    private var inFlightList = false
    private var inFlightAction = false

    fun loadMyPalms(force: Boolean = false) {
        if (inFlightList) return
        if (!force && _palmsState.value is UiState.Success) return  // 이미 있으면 재호출 방지

        viewModelScope.launch {
            inFlightList = true
            _palmsState.value = UiState.Loading

            runCatching {
                userRepository.getMyPalms()
            }.onSuccess { res ->
                _palmsState.value = UiState.Success(res.palms)
            }.onFailure { e ->
                _palmsState.value = UiState.Error(e.message ?: "손바닥 목록 조회 중 오류가 발생했습니다.")
            }

            inFlightList = false
        }
    }

    fun deletePalm(palmId: Int) {
        if (inFlightAction) return

        viewModelScope.launch {
            inFlightAction = true
            _actionState.value = UiState.Loading

            runCatching {
                userRepository.deletePalm(palmId) // ✅ 개별 삭제 API
            }.onSuccess {
                _actionState.value = UiState.Success(Unit)
                // 삭제 후 목록 최신화 (삭제했을 때는 GET 허용)
                loadMyPalms(force = true)
            }.onFailure { e ->
                _actionState.value = UiState.Error(e.message ?: "손바닥 삭제 중 오류가 발생했습니다.")
            }

            inFlightAction = false
        }
    }

    fun deleteAllPalms() {
        if (inFlightAction) return

        viewModelScope.launch {
            inFlightAction = true
            _actionState.value = UiState.Loading

            runCatching {
                userRepository.deleteAllMyPalms() // ✅ 전체 삭제 API
            }.onSuccess {
                _actionState.value = UiState.Success(Unit)
                // 전체 삭제 후 목록 최신화
                loadMyPalms(force = true)
            }.onFailure { e ->
                _actionState.value = UiState.Error(e.message ?: "전체 삭제 중 오류가 발생했습니다.")
            }

            inFlightAction = false
        }
    }

    fun clearActionState() {
        _actionState.value = UiState.Idle
    }
}
