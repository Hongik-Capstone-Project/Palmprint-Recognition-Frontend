package com.example.palmprint_recognition.ui.admin.features.device_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<DeviceInfo>>(UiState.Idle)
    val state = _state.asStateFlow()

    fun loadDevice(deviceId: Int) {
        // 연속 호출 방지(특히 recomposition/빠른 탭)
        if (_state.value is UiState.Loading) return

        viewModelScope.launch {
            _state.value = UiState.Loading

            runCatching {
                adminRepository.getDeviceById(deviceId)
            }.onSuccess { result ->
                _state.value = UiState.Success(result)
            }.onFailure { e ->
                _state.value = UiState.Error(e.message ?: "디바이스 정보를 불러오는 중 오류가 발생했습니다.")
            }
        }
    }
}
