package com.example.palmprint_recognition.ui.admin.features.report_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.ReportInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportDetailViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    companion object {
        val STATUS_OPTIONS = listOf("처리 전", "승인", "기각")
        private val STATUS_API_VALUES = listOf("pending", "approved", "rejected")
    }

    private val _reportState = MutableStateFlow<UiState<ReportInfo>>(UiState.Idle)
    val reportState = _reportState.asStateFlow()

    private val _selectedStatusIndex = MutableStateFlow(0)
    val selectedStatusIndex = _selectedStatusIndex.asStateFlow()

    private val _saveState = MutableStateFlow<UiState<ReportInfo>>(UiState.Idle)
    val saveState = _saveState.asStateFlow()

    fun loadReport(reportId: Int) {
        viewModelScope.launch {
            _reportState.value = UiState.Loading

            runCatching {
                adminRepository.getReportById(reportId)
            }.onSuccess { report ->
                _reportState.value = UiState.Success(report)
                _selectedStatusIndex.value = apiStatusToIndex(report.status)
            }.onFailure { e ->
                _reportState.value = UiState.Error(e.message ?: "오류가 발생했습니다.")
            }
        }
    }

    fun onSelectStatus(index: Int) {
        if (index in STATUS_OPTIONS.indices) {
            _selectedStatusIndex.value = index
        }
    }

    fun saveStatus(reportId: Int) {
        // ✅ 저장 중이면 중복 요청 방지
        if (_saveState.value is UiState.Loading) return

        viewModelScope.launch {
            _saveState.value = UiState.Loading

            val idx = _selectedStatusIndex.value
            val statusForApi = STATUS_API_VALUES.getOrNull(idx) ?: STATUS_API_VALUES.first()

            runCatching {
                adminRepository.updateReportStatus(reportId = reportId, status = statusForApi)
            }.onSuccess { updated ->
                _reportState.value = UiState.Success(updated)
                _saveState.value = UiState.Success(updated)
                // 선택값도 서버 응답 기준으로 동기화(안전)
                _selectedStatusIndex.value = apiStatusToIndex(updated.status)
            }.onFailure { e ->
                _saveState.value = UiState.Error(e.message ?: "상태 저장 중 오류가 발생했습니다.")
            }
        }
    }

    fun clearSaveState() {
        _saveState.value = UiState.Idle
    }

    private fun apiStatusToIndex(status: String?): Int {
        return when (status?.lowercase()) {
            "pending" -> 0
            "approved" -> 1
            "rejected" -> 2
            else -> 0
        }
    }
}
