package com.example.palmprint_recognition.ui.admin.features.verification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.VerificationRecord
import com.example.palmprint_recognition.data.model.VerificationSummaryResponse
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.PaginationUiState
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    // 상단 요약
    private val _summaryState =
        MutableStateFlow<UiState<VerificationSummaryResponse>>(UiState.Loading)
    val summaryState = _summaryState.asStateFlow()

    // 하단 목록(페이지네이션)
    private val _listState = MutableStateFlow(PaginationUiState<VerificationRecord>())
    val listState = _listState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 10

    init {
        refresh()
    }

    fun refresh() {
        loadSummary()

        currentPage = 1
        _listState.value = PaginationUiState(
            items = emptyList(),
            isLoadingInitial = false,
            isLoadingMore = false,
            errorMessage = null,
            hasMore = true
        )
        loadNextPage()
    }

    private fun loadSummary() {
        viewModelScope.launch {
            _summaryState.value = UiState.Loading

            runCatching {
                adminRepository.getVerificationSummary()
            }.onSuccess { summary ->
                _summaryState.value = UiState.Success(summary)
            }.onFailure { e ->
                _summaryState.value = UiState.Error(e.message ?: "요약 정보를 불러오지 못했습니다.")
            }
        }
    }

    fun loadNextPage() {
        val state = _listState.value
        if (state.isLoadingInitial || state.isLoadingMore) return
        if (!state.hasMore) return

        viewModelScope.launch {
            val isFirstPage = currentPage == 1

            _listState.value = if (isFirstPage) {
                _listState.value.copy(isLoadingInitial = true, errorMessage = null)
            } else {
                _listState.value.copy(isLoadingMore = true, errorMessage = null)
            }

            runCatching {
                adminRepository.getVerificationList(page = currentPage, size = pageSize)
            }.onSuccess { response ->
                val newItems = response.items
                val updatedList =
                    if (isFirstPage) newItems else _listState.value.items + newItems

                val hasMore = currentPage < response.pages

                _listState.value = _listState.value.copy(
                    items = updatedList,
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    hasMore = hasMore,
                    errorMessage = null
                )

                if (hasMore) currentPage++
            }.onFailure { e ->
                _listState.value = _listState.value.copy(
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    errorMessage = e.message ?: "오류 발생",
                    hasMore = false
                )
            }
        }
    }
}
