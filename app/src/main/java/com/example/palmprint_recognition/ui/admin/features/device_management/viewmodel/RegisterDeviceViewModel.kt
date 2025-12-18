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
class RegisterDeviceViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _registerDeviceState = MutableStateFlow<UiState<DeviceInfo>>(UiState.Idle)
    val registerDeviceState = _registerDeviceState.asStateFlow()

    fun registerDevice(id: Int, institutionName: String, location: String) {
        viewModelScope.launch {
            _registerDeviceState.value = UiState.Loading

            runCatching {
                adminRepository.registerDevice(
                    id = id,
                    institutionName = institutionName,
                    location = location
                )
            }.onSuccess { result ->
                _registerDeviceState.value = UiState.Success(result)
            }.onFailure { e ->
                _registerDeviceState.value =
                    UiState.Error(e.message ?: "디바이스 등록 중 오류가 발생했습니다.")
            }
        }
    }

    fun clearState() {
        _registerDeviceState.value = UiState.Idle
    }
}
