package com.example.palmprint_recognition.ui.admin.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.VerificationRecord
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * VerificationListViewModel
 *
 * 역할
 * - 인증 내역 리스트 조회 API 호출
 * - 서버 데이터를 UiState 로 변환하여 UI 에 전달
 *
 */
@HiltViewModel
class VerificationListViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    /** UI 상태를 나타내는 StateFlow */
    private val _verificationState =
        MutableStateFlow<UiState<List<VerificationRecord>>>(UiState.Idle)
    val verificationState: StateFlow<UiState<List<VerificationRecord>>> = _verificationState

    /**
     * 인증 내역 리스트 API 호출
     */
    fun fetchVerificationList() {
        viewModelScope.launch {
            _verificationState.value = UiState.Loading

            try {
                val response = repository.getVerificationList()
                _verificationState.value = UiState.Success(response.items)

            } catch (e: Exception) {
                _verificationState.value = UiState.Error(e.message)
            }
        }
    }
}
