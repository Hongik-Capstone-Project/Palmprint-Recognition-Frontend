package com.example.palmprint_recognition.ui.admin.user_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.AdminUserInfo
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UserDetailViewModel
 *
 * 역할
 * - 특정 사용자 상세 정보를 조회한다.
 * - 사용자 정보를 수정한다(updateUser API).
 * - 화면에 전달할 UiState(Loading/Success/Error)를 관리한다.
 */
@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    /** 사용자 상세 + 수정 결과를 담는 상태 */
    private val _state = MutableStateFlow<UiState<AdminUserInfo>>(UiState.Loading)
    val state = _state.asStateFlow()

    /**
     * 특정 사용자 정보 조회 요청
     */
    fun loadUser(userId: Int) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val result = repository.getUserById(userId)
                _state.value = UiState.Success(result)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "오류가 발생했습니다.")
            }
        }
    }

    /**
     * 사용자 정보 수정 요청
     */
    fun updateUser(userId: Int, name: String?, email: String?) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val updated = repository.updateUser(userId, name, email)
                _state.value = UiState.Success(updated)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message)
            }
        }
    }
}
