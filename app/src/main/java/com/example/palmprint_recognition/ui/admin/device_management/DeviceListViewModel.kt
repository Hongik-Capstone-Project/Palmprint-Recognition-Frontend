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
 * DeviceListViewModel
 *
 * - AdminRepository 를 통해 디바이스 목록을 조회한다.
 * - UiState<List<DeviceInfo>> 형태로 상태를 관리하여 화면에 전달한다.
 */
@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    /**
     * 디바이스 목록의 상태를 저장하는 StateFlow
     * - Loading
     * - Success(List<DeviceInfo>)
     * - Error(message)
     */
    private val _deviceListState =
        MutableStateFlow<UiState<List<DeviceInfo>>>(UiState.Idle)

    val deviceListState = _deviceListState.asStateFlow()

    init {
        // ViewModel 생성 시 자동으로 목록 불러오기
        loadDeviceList()
    }

    /**
     * 디바이스 목록 조회 함수
     *
     * @param page 조회할 페이지
     * @param size 페이지당 항목 수
     */
    fun loadDeviceList(page: Int = 1, size: Int = 10) {
        viewModelScope.launch {
            _deviceListState.value = UiState.Loading

            try {
                val response = repository.getDeviceList(page, size)
                _deviceListState.value = UiState.Success(response.items)
            } catch (e: Exception) {
                _deviceListState.value = UiState.Error(
                    e.message ?: "디바이스 목록을 불러오는 중 오류 발생"
                )
            }
        }
    }
}
