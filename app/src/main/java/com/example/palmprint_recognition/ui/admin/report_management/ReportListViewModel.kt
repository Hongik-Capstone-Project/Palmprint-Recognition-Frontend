package com.example.palmprint_recognition.ui.admin.report_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.PaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 신고 내역 목록 + 무한 스크롤용 ViewModel
 */
@HiltViewModel
class ReportListViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(PaginationUiState<ReportInfo>(isLoadingInitial = true))
    val uiState = _uiState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 10

    init {
        loadNextPage()
    }

    /**
     * 무한스크롤 기반 페이지 로딩
     */
    fun loadNextPage() {
        val state = _uiState.value

        // 이미 로딩중이거나 더 이상 로드할 데이터 없음
        if (state.isLoadingInitial || state.isLoadingMore || !state.hasMore) return

        viewModelScope.launch {

            if (currentPage == 1) {
                _uiState.value = state.copy(isLoadingInitial = true)
            } else {
                _uiState.value = state.copy(isLoadingMore = true)
            }

            try {
                val response = repository.getReportList(
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
                    errorMessage = e.message,
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    hasMore = false
                )
            }
        }
    }
}
