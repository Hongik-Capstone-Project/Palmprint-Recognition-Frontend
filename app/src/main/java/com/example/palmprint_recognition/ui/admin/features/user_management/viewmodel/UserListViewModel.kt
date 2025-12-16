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

/**
 * 유저 목록 ViewModel
 * - 페이지네이션 기반 목록 조회
 * - 무한 스크롤 지원
 */
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaginationUiState<AdminUserInfo>())
    val uiState = _uiState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 10

    init {
        refresh()
    }

    /**
     * 다음 페이지 로딩 (무한스크롤)
     */
    fun loadNextPage() {
        val state = _uiState.value

        // 첫 페이지 로딩 중이거나, 추가 로딩 중이면 중복 요청 방지
        if (state.isLoadingInitial || state.isLoadingMore) return

        // 더 이상 데이터가 없으면 요청하지 않음
        if (!state.hasMore) return

        viewModelScope.launch {
            val isFirstPage = currentPage == 1

            _uiState.value = if (isFirstPage) {
                state.copy(isLoadingInitial = true, errorMessage = null)
            } else {
                state.copy(isLoadingMore = true, errorMessage = null)
            }

            runCatching {
                adminRepository.getUserList(page = currentPage, size = pageSize)
            }.onSuccess { response ->
                val newItems = response.items
                val updatedList = if (isFirstPage) newItems else _uiState.value.items + newItems
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

    /**
     * 목록 새로고침
     */
    fun refresh() {
        currentPage = 1
        _uiState.value = PaginationUiState(isLoadingInitial = false, hasMore = true)
        loadNextPage()
    }
}
