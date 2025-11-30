package com.example.palmprint_recognition.ui.admin.user_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.UiState
import com.example.palmprint_recognition.data.model.AddUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * AddUserScreen 의 ViewModel.
 *
 * 역할:
 * - 이름, 이메일, 비밀번호를 입력받아 서버에 "새로운 유저 생성" 요청을 보낸다.
 * - API 호출의 로딩/성공/에러 상태를 UiState 로 관리한다.
 */
@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    /** 유저 추가 API 상태 저장 */
    private val _addUserState =
        MutableStateFlow<UiState<AddUserResponse>>(UiState.Loading)
    val addUserState = _addUserState.asStateFlow()

    /**
     * API 호출: 새로운 유저 추가
     *
     * @param name 사용자 이름
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     */
    fun addUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _addUserState.value = UiState.Loading

            try {
                val result = adminRepository.addUser(name, email, password)
                _addUserState.value = UiState.Success(result)

            } catch (e: Exception) {
                _addUserState.value = UiState.Error(
                    e.message ?: "유저 생성 중 오류가 발생했습니다."
                )
            }
        }
    }
}
