package com.example.palmprint_recognition.ui.admin.device_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * DeleteDeviceViewModel
 *
 * - deleteDevice(deviceId) API 호출
 * - 성공 시 UiState.Success(Unit)
 * - 실패 시 UiState.Error(message)
 */
@HiltViewModel
class DeleteDeviceViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    private val _deleteState =
        MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val deleteState = _deleteState.asStateFlow()

    /**
     * 디바이스 삭제 API 호출
     */
    fun deleteDevice(deviceId: Int) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            try {
                repository.deleteDevice(deviceId)
                _deleteState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _deleteState.value = UiState.Error(
                    e.message ?: "디바이스 삭제 중 오류가 발생했습니다."
                )
            }
        }
    }
}
