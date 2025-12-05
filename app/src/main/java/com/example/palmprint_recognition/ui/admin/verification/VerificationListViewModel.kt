package com.example.palmprint_recognition.ui.admin.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.VerificationRecord
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.PaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * VerificationList 화면용 ViewModel
 * - 무한 스크롤 + PaginationUiState 기반
 */
@HiltViewModel
class VerificationListViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PaginationUiState<VerificationRecord>(isLoadingInitial = true)
    )
    val uiState = _uiState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 10

    init {
        loadNextPage()
    }

    /**
     * 무한스크롤 다음 페이지 로드
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
                val response = repository.getVerificationList(
                    page = currentPage,
                    size = pageSize
                )

                val newItems = response.items
                val combinedItems = state.items + newItems
                val hasMore = newItems.isNotEmpty()

                _uiState.value = state.copy(
                    items = combinedItems,
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
