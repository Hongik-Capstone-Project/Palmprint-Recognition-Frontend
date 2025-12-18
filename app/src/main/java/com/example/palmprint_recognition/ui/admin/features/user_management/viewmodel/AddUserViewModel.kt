package com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.AddUserResponse
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AddUserResponse>>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun addUser(
        name: String,
        email: String,
        password: String,
        isAdmin: Boolean
    ) {
        // 중복 클릭 방지
        if (_uiState.value is UiState.Loading) return

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val res = adminRepository.addUser(
                    name = name.trim(),
                    email = email.trim(),
                    password = password,
                    isAdmin = isAdmin
                )
                _uiState.value = UiState.Success(res)
            } catch (e: ApiException) {
                // 서버 에러 메시지 우선 노출 (409/400 등)
                _uiState.value = UiState.Error(e.errorResponse.message)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "유저 생성 중 오류가 발생했습니다.")
            }
        }
    }

    fun clearState() {
        _uiState.value = UiState.Idle
    }
}
