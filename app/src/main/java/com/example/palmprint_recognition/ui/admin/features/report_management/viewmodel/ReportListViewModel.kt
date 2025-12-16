package com.example.palmprint_recognition.ui.admin.features.report_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.PaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportListViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaginationUiState<ReportInfo>())
    val uiState = _uiState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 10

    init {
        refresh()
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoadingInitial || state.isLoadingMore) return
        if (!state.hasMore) return

        viewModelScope.launch {
            val isFirstPage = currentPage == 1

            _uiState.value = if (isFirstPage) {
                _uiState.value.copy(isLoadingInitial = true, errorMessage = null)
            } else {
                _uiState.value.copy(isLoadingMore = true, errorMessage = null)
            }

            runCatching {
                adminRepository.getReportList(page = currentPage, size = pageSize)
            }.onSuccess { response ->
                val newItems = response.items
                val updatedList =
                    if (isFirstPage) newItems else _uiState.value.items + newItems

                val hasMore = currentPage < response.pages

                _uiState.value = _uiState.value.copy(
                    items = updatedList,
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
                    errorMessage = e.message ?: "오류 발생",
                    hasMore = false
                )
            }
        }
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
}
