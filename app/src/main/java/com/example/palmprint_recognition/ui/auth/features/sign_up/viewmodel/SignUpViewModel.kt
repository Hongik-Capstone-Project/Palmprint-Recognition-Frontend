package com.example.palmprint_recognition.ui.auth.features.sign_up.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.repository.AuthRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpFormState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _form = MutableStateFlow(SignUpFormState())
    val form: StateFlow<SignUpFormState> = _form

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val uiState: StateFlow<UiState<String>> = _uiState
    // Success<String> 에 userId를 담을 예정

    fun onNameChange(value: String) {
        _form.update { it.copy(name = value) }
    }

    fun onEmailChange(value: String) {
        _form.update { it.copy(email = value) }
    }

    fun onPasswordChange(value: String) {
        _form.update { it.copy(password = value) }
    }

    fun onConfirmPasswordChange(value: String) {
        _form.update { it.copy(confirmPassword = value) }
    }

    fun signUp() {
        val name = form.value.name.trim()
        val email = form.value.email.trim()
        val password = form.value.password
        val confirm = form.value.confirmPassword

        // ---- 기본 유효성 검사 ----
        if (name.isBlank()) {
            _uiState.value = UiState.Error("이름을 입력해주세요.")
            return
        }
        if (email.isBlank()) {
            _uiState.value = UiState.Error("이메일을 입력해주세요.")
            return
        }
        if (password.length < 8) {
            _uiState.value = UiState.Error("비밀번호는 8자 이상이어야 합니다.")
            return
        }
        if (password != confirm) {
            _uiState.value = UiState.Error("비밀번호가 일치하지 않습니다.")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val res = authRepository.signup(name, email, password)
                _uiState.value = UiState.Success(res.userId)
            } catch (e: ApiException) {
                // 서버가 409 Conflict 등에서 message를 주는 형태면 여기에 들어옴
                _uiState.value = UiState.Error(e.errorResponse.message)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "회원가입 중 오류가 발생했습니다.")
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}
