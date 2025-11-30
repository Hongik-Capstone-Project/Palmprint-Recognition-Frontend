package com.example.palmprint_recognition.ui.admin.palmprint_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * DeletePalmprintViewModel
 *
 * - deletePalmprint(userId, palmprintId) API 호출
 * - UiState(Loading / Success / Error) 관리
 */
@HiltViewModel
class DeletePalmprintViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val state = _state.asStateFlow()

    /**
     * 손바닥 삭제 요청
     */
    fun deletePalmprint(userId: Int, palmprintId: Int) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                repository.deletePalmprint(userId, palmprintId)
                _state.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _state.value = UiState.Error(
                    e.message ?: "손바닥 정보 삭제 중 오류가 발생했습니다."
                )
            }
        }
    }
}
