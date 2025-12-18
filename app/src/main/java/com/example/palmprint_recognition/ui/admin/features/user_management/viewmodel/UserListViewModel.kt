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
     * 첫 로드/새로고침
     */
    fun refresh() {
        currentPage = 1
        _uiState.value = PaginationUiState(
            items = emptyList(),
            isLoadingInitial = true,
            isLoadingMore = false,
            hasMore = true,
            errorMessage = null
        )
        loadNextPage()
    }

    /**
     * 다음 페이지 로딩 (무한스크롤)
     */
    fun loadNextPage() {
        val state = _uiState.value

        // 로딩 중이면 중복 요청 방지
        if (state.isLoadingInitial || state.isLoadingMore) return

        // 더 이상 데이터가 없으면 요청하지 않음
        if (!state.hasMore) return

        val isFirstPage = currentPage == 1

        _uiState.value = if (isFirstPage) {
            state.copy(isLoadingInitial = true, errorMessage = null)
        } else {
            state.copy(isLoadingMore = true, errorMessage = null)
        }

        viewModelScope.launch {
            runCatching {
                adminRepository.getUserList(page = currentPage, size = pageSize)
            }.onSuccess { response ->
                val newItems = response.items
                val merged = if (isFirstPage) newItems else state.items + newItems

                // pages 기준: currentPage < pages 이면 다음 페이지 존재
                val hasMore = currentPage < response.pages

                _uiState.value = _uiState.value.copy(
                    items = merged,
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    hasMore = hasMore,
                    errorMessage = null
                )

                if (hasMore) currentPage++
            }.onFailure { e ->
                // 에러라고 해서 hasMore=false로 막아버리면 "재시도"가 불가능해짐
                _uiState.value = _uiState.value.copy(
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    errorMessage = e.message ?: "오류 발생",
                    // hasMore는 그대로 유지 (재시도 가능)
                    hasMore = state.hasMore
                )
            }
        }
    }
}
