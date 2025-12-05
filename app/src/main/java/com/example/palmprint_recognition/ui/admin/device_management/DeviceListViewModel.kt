package com.example.palmprint_recognition.ui.admin.device_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.DeviceInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.PaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * DeviceList + TableView 용 ViewModel (무한스크롤 지원)
 */
@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(PaginationUiState<DeviceInfo>(isLoadingInitial = true))
    val uiState = _uiState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 10

    init {
        loadNextPage()
    }

    /**
     * 디바이스 목록 불러오기 (무한스크롤)
     */
    fun loadNextPage() {
        val state = _uiState.value

        if (state.isLoadingInitial || state.isLoadingMore || !state.hasMore) return

        viewModelScope.launch {

            if (currentPage == 1) {
                _uiState.value = state.copy(isLoadingInitial = true)
            } else {
                _uiState.value = state.copy(isLoadingMore = true)
            }

            try {
                val response = repository.getDeviceList(
                    page = currentPage,
                    size = pageSize
                )

                val newItems = response.items
                val updatedList = state.items + newItems
                val hasMore = newItems.isNotEmpty()

                _uiState.value = state.copy(
                    items = updatedList,
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    hasMore = hasMore
                )

                if (hasMore) currentPage++

            } catch (e: Exception) {
                _uiState.value = state.copy(
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    errorMessage = e.message ?: "오류 발생",
                    hasMore = false
                )
            }
        }
    }
}
