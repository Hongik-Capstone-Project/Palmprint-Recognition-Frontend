package com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.PaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaginationUiState<AdminUserInfo>())
    val uiState = _uiState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 10

    // 로딩 중복 방지용 (특히 TableView가 연속으로 onLoadMore를 쏘는 경우)
    private var isRequestInFlight = false

    init {
        refresh()
    }

    fun loadNextPage() {
        val state = _uiState.value

        if (isRequestInFlight) return
        if (state.isLoadingInitial || state.isLoadingMore) return
        if (!state.hasMore) return

        viewModelScope.launch {
            isRequestInFlight = true

            val isFirstPage = currentPage == 1
            _uiState.value = if (isFirstPage) {
                state.copy(isLoadingInitial = true, errorMessage = null)
            } else {
                state.copy(isLoadingMore = true, errorMessage = null)
            }

            runCatching {
                adminRepository.getUserList(page = currentPage, size = pageSize)
            }.onSuccess { response ->
                // items가 nullable로 바뀌는 경우까지 대비 (현재는 non-null일 가능성이 큼)
                val newItems = try {
                    @Suppress("UNCHECKED_CAST")
                    (response.items as? List<AdminUserInfo>).orEmpty()
                } catch (e: Exception) {
                    emptyList()
                }

                val updatedList = if (isFirstPage) newItems else _uiState.value.items + newItems

                // pages가 0이거나 이상한 값으로 오는 경우도 방어
                val totalPages = response.pages
                val hasMore = when {
                    totalPages > 0 -> currentPage < totalPages
                    else -> newItems.size >= pageSize // fallback
                }

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

            isRequestInFlight = false
        }
    }

    fun refresh() {
        currentPage = 1
        isRequestInFlight = false
        _uiState.value = PaginationUiState(
            items = emptyList(),
            isLoadingInitial = false,
            isLoadingMore = false,
            hasMore = true,
            errorMessage = null
        )
        loadNextPage()
    }
}
