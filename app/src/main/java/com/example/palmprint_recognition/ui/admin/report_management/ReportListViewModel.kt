package com.example.palmprint_recognition.ui.admin.report_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ReportListViewModel
 *
 * - 신고 내역 목록을 API(AdminRepository.getReportList)로 부터 가져온다.
 * - UI 에 표시할 데이터를 UiState 형태로 관리한다.
 * - ReportListScreen 은 ViewModel 내부 세부 로직을 모르며,
 *   화면 렌더링에 필요한 상태만 구독한다.
 */
@HiltViewModel
class ReportListViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    /**
     * StateFlow → Composable 이 관찰
     * Idle: 최초 기본 상태
     * Loading: API 호출 중
     * Success: 결과 수신
     * Error: 오류 발생
     */
    private val _reportListState =
        MutableStateFlow<UiState<List<ReportInfo>>>(UiState.Idle)
    val reportListState = _reportListState.asStateFlow()

    init {
        // 화면 진입 시 자동으로 불러오기
        loadReportList()
    }

    /**
     * 신고 내역 목록 조회 API 요청
     *
     * @param page 페이지 번호
     * @param size 페이지 당 항목 수
     */
    fun loadReportList(
        page: Int = 1,
        size: Int = 10
    ) {
        viewModelScope.launch {
            _reportListState.value = UiState.Loading

            try {
                val response = repository.getReportList(page, size)
                _reportListState.value = UiState.Success(response.items)

            } catch (e: Exception) {
                _reportListState.value =
                    UiState.Error(e.message ?: "신고 내역을 불러오는 중 오류가 발생했습니다.")
            }
        }
    }
}
