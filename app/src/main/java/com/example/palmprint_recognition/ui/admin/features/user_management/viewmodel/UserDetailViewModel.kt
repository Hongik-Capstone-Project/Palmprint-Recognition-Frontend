package com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.AdminUserDetail
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AdminUserDetail>>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun loadUser(userId: Int) {
        // 중복 로드 방지(원하면 제거 가능)
        if (_uiState.value is UiState.Loading) return

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val user = adminRepository.getUserById(userId)
                _uiState.value = UiState.Success(user)
            } catch (e: ApiException) {
                // 401/403/404 등 서버 메시지 그대로 노출
                _uiState.value = UiState.Error(e.errorResponse.message)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "오류가 발생했습니다.")
            }
        }
    }

    fun clearState() {
        _uiState.value = UiState.Idle
    }
}
