package com.example.palmprint_recognition.ui.admin.features.device_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.PaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaginationUiState<DeviceInfo>())
    val uiState = _uiState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 10

    init {
        refresh()
    }

    fun refresh() {
        currentPage = 1
        _uiState.value = PaginationUiState(
            items = emptyList(),
            isLoadingInitial = false,
            isLoadingMore = false,
            errorMessage = null,
            hasMore = true
        )
        loadNextPage()
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoadingInitial || state.isLoadingMore) return
        if (!state.hasMore) return

        val isFirstPage = currentPage == 1

        viewModelScope.launch {
            _uiState.value = if (isFirstPage) {
                state.copy(isLoadingInitial = true, errorMessage = null)
            } else {
                state.copy(isLoadingMore = true, errorMessage = null)
            }

            runCatching {
                adminRepository.getDeviceList(page = currentPage, size = pageSize)
            }.onSuccess { response ->
                val newItems = response.items
                val updatedItems =
                    if (isFirstPage) newItems
                    else _uiState.value.items + newItems

                val hasMore = currentPage < response.pages

                _uiState.value = _uiState.value.copy(
                    items = updatedItems,
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    hasMore = hasMore,
                    errorMessage = null
                )

                if (hasMore) currentPage++
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    errorMessage = e.message ?: "디바이스 목록 조회 중 오류가 발생했습니다."
                    // ✅ hasMore는 유지 (네트워크 오류 후 스크롤 재시도 가능)
                )
            }
        }
    }
}
