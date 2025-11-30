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
 * AddDeviceViewModel
 *
 * - registerDevice(identifier, memo) 실행하여 디바이스 등록
 * - API 결과를 UiState 형태로 화면에 전달
 */
@HiltViewModel
class AddDeviceViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    /** 디바이스 추가 상태 */
    private val _addDeviceState =
        MutableStateFlow<UiState<DeviceInfo>>(UiState.Loading)
    val addDeviceState = _addDeviceState.asStateFlow()

    /**
     * 새로운 디바이스 등록 API 호출
     */
    fun registerDevice(identifier: String, memo: String) {
        viewModelScope.launch {
            _addDeviceState.value = UiState.Loading

            try {
                val result = repository.registerDevice(identifier, memo)
                _addDeviceState.value = UiState.Success(result)

            } catch (e: Exception) {
                _addDeviceState.value = UiState.Error(
                    e.message ?: "디바이스 등록 중 오류가 발생했습니다."
                )
            }
        }
    }
}
