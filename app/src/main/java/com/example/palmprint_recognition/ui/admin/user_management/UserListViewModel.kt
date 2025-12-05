package com.example.palmprint_recognition.ui.admin.user_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.PaginationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UserListScreen + TableView 용 ViewModel
 * - 페이지네이션 기반
 * - 무한스크롤 지원
 */
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(PaginationUiState<AdminUserInfo>(isLoadingInitial = true))
    val uiState = _uiState.asStateFlow()

    private var currentPage = 1
    private val pageSize = 10

    init {
        loadNextPage()
    }

    /**
     * 다음 페이지 로딩 (무한스크롤)
     */
    fun loadNextPage() {

        // 이미 로딩 중이거나 더 이상 데이터가 없으면 요청하지 않음
        if (_uiState.value.isLoadingMore ||
            _uiState.value.isLoadingInitial ||
            !_uiState.value.hasMore
        ) return

        viewModelScope.launch {

            // 첫 페이지 로딩인지/추가 페이지 로딩인지 구별
            if (currentPage == 1) {
                _uiState.value = _uiState.value.copy(isLoadingInitial = true)
            } else {
                _uiState.value = _uiState.value.copy(isLoadingMore = true)
            }

            try {
                val response = adminRepository.getUserList(
                    page = currentPage,
                    size = pageSize
                )

                val newItems = response.items

                // 새로운 데이터 누적
                val updatedList = _uiState.value.items + newItems

                // 더 이상 데이터 없음 처리
                val hasMore = newItems.isNotEmpty()

                _uiState.value = _uiState.value.copy(
                    items = updatedList,
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    hasMore = hasMore
                )

                if (hasMore) {
                    currentPage++
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoadingInitial = false,
                    isLoadingMore = false,
                    errorMessage = e.message ?: "오류 발생",
                    hasMore = false
                )
            }
        }
    }
}
