package com.example.palmprint_recognition.ui.user.features.histories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.PagedResponse
import com.example.palmprint_recognition.data.model.UserVerificationLog
import com.example.palmprint_recognition.data.repository.UserRepository
import com.example.palmprint_recognition.ui.core.state.PaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryListViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _listState = MutableStateFlow(PaginationUiState<UserVerificationLog>())
    val listState = _listState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 50

    init {
        refresh()
    }

    fun refresh() {
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

    fun loadNextPage() {
        val state = _listState.value
        if (state.isLoadingInitial || state.isLoadingMore) return
        if (!state.hasMore) return

        viewModelScope.launch {
            val isFirstPage = currentPage == 1

            _listState.value = if (isFirstPage) {
                state.copy(isLoadingInitial = true, errorMessage = null)
            } else {
                state.copy(isLoadingMore = true, errorMessage = null)
            }

            runCatching {
                userRepository.getUserVerifications(page = currentPage, size = pageSize)
            }.onSuccess { response: PagedResponse<UserVerificationLog> ->
                val newItems = response.items
                val updatedItems =
                    if (isFirstPage) newItems else _listState.value.items + newItems

                val hasMore = currentPage < response.pages

                _listState.value = _listState.value.copy(
                    items = updatedItems,
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
