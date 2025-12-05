package com.example.palmprint_recognition.ui.admin.common

data class PaginationUiState<T>(
    val items: List<T> = emptyList(),
    val isLoadingInitial: Boolean = false,   // 첫 페이지 로딩
    val isLoadingMore: Boolean = false,      // 무한 스크롤 중 로딩
    val errorMessage: String? = null,
    val hasMore: Boolean = true              // 더 불러올 데이터 존재 여부
)
