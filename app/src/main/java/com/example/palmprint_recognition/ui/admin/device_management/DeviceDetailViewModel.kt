package com.example.palmprint_recognition.ui.admin.device_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * DeviceDetailViewModel
 *
 * - 특정 디바이스 정보 조회(getDeviceById)
 * - 디바이스 정보 수정(updateDevice)
 * - UI 에 전달할 UiState(DeviceInfo) 유지
 */
@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    /** 디바이스 상세 정보 + 업데이트 결과 저장 */
    private val _state = MutableStateFlow<UiState<DeviceInfo>>(UiState.Loading)
    val state = _state.asStateFlow()

    /**
     * 특정 디바이스 조회
     */
    fun loadDevice(deviceId: Int) {
        viewModelScope.launch {
            _state.value = UiState.Loading

            try {
                val device = repository.getDeviceById(deviceId)
                _state.value = UiState.Success(device)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "디바이스 정보를 불러오는 중 오류 발생")
            }
        }
    }

    /**
     * 디바이스 정보 수정
     */
    fun updateDevice(deviceId: Int, identifier: String?, memo: String?) {
        viewModelScope.launch {
            _state.value = UiState.Loading

            try {
                val updated = repository.updateDevice(deviceId, identifier, memo)
                _state.value = UiState.Success(updated)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "디바이스 수정 오류 발생")
            }
        }
    }
}
