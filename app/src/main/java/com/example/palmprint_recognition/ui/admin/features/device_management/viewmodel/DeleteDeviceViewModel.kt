package com.example.palmprint_recognition.ui.admin.features.device_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteDeviceViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _deleteState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteState = _deleteState.asStateFlow()

    fun deleteDevice(deviceId: Int) {
        // 중복 삭제 방지
        if (_deleteState.value is UiState.Loading) return

        viewModelScope.launch {
            _deleteState.value = UiState.Loading

            runCatching {
                adminRepository.deleteDevice(deviceId)
            }.onSuccess {
                _deleteState.value = UiState.Success(Unit)
            }.onFailure { e ->
                _deleteState.value =
                    UiState.Error(e.message ?: "디바이스 삭제 중 오류가 발생했습니다.")
            }
        }
    }

    fun clearState() {
        _deleteState.value = UiState.Idle
    }
}
