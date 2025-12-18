package com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteUserViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _deleteState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteState = _deleteState.asStateFlow()

    fun deleteUser(userId: Int) {
        // 중복 클릭 방지
        if (_deleteState.value is UiState.Loading) return

        viewModelScope.launch {
            _deleteState.value = UiState.Loading
            try {
                // 204 No Content -> 성공
                adminRepository.deleteUser(userId)
                _deleteState.value = UiState.Success(Unit)
            } catch (e: ApiException) {
                _deleteState.value = UiState.Error(e.errorResponse.message)
            } catch (e: Exception) {
                _deleteState.value = UiState.Error(e.message ?: "유저 삭제 중 오류가 발생했습니다.")
            }
        }
    }

    fun clearState() {
        _deleteState.value = UiState.Idle
    }
}
