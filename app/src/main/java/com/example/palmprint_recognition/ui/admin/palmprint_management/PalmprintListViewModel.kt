package com.example.palmprint_recognition.ui.admin.palmprint_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.PalmprintInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * PalmprintListViewModel
 *
 * - 특정 사용자(userId)의 손바닥 정보 리스트 API 호출
 * - 상태(UiState) 관리 → 화면(PalmprintListScreen)에 전달
 */
@HiltViewModel
class PalmprintListViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    /** 손바닥 리스트 상태를 저장하는 StateFlow */
    private val _palmprintState =
        MutableStateFlow<UiState<List<PalmprintInfo>>>(UiState.Loading)
    val palmprintState = _palmprintState.asStateFlow()

    /**
     * 손바닥 정보 목록 조회 함수
     */
    fun loadPalmprints(userId: Int) {
        viewModelScope.launch {
            _palmprintState.value = UiState.Loading

            try {
                val response = repository.getUserPalmprints(userId)
                _palmprintState.value = UiState.Success(response.data)

            } catch (e: Exception) {
                _palmprintState.value = UiState.Error(
                    e.message ?: "손바닥 목록 조회 중 오류가 발생했습니다."
                )
            }
        }
    }
}
