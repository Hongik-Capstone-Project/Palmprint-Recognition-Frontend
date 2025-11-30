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
 * ReportDetailViewModel
 *
 * - getReportById(reportId) 로 신고 상세 조회
 * - updateReportStatus(reportId, status) 로 신고 상태 업데이트
 * - UiState<ReportInfo> 로 화면 상태 관리
 */
@HiltViewModel
class ReportDetailViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    /** 현재 화면 상태 */
    private val _state = MutableStateFlow<UiState<ReportInfo>>(UiState.Idle)
    val state = _state.asStateFlow()

    /**
     * 신고 상세 조회
     */
    fun loadReport(reportId: Int) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val report = repository.getReportById(reportId)
                _state.value = UiState.Success(report)

            } catch (e: Exception) {
                _state.value = UiState.Error(
                    e.message ?: "신고 내역을 불러오는 중 오류 발생"
                )
            }
        }
    }

    /**
     * 신고 상태 업데이트
     *
     * @param status "approved" 또는 "rejected" 또는 "pending"
     */
    fun updateReportStatus(
        reportId: Int,
        status: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val result = repository.updateReportStatus(reportId, status)
                // 서버가 반환한 update 결과에 맞춰 새로운 상태 업데이트
                val updatedReport = result.report
                _state.value = UiState.Success(
                    ReportInfo(
                        id = updatedReport.id,
                        verificationLogId = updatedReport.verificationLogId,
                        reason = updatedReport.reason,
                        status = updatedReport.status,
                        user = updatedReport.user,
                        createdAt = updatedReport.updatedAt   // 갱신된 시각 사용
                    )
                )

                onSuccess()   // ← 여기에서 Navigation 이동

            } catch (e: Exception) {
                _state.value = UiState.Error(
                    e.message ?: "신고 상태 수정 중 오류 발생"
                )
            }
        }
    }
}
