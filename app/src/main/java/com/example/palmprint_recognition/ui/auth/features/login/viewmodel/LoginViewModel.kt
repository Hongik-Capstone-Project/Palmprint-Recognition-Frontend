package com.example.palmprint_recognition.ui.auth.features.login.viewmodel

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

data class LoginFormState(
    val email: String = "",
    val password: String = ""
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _form = MutableStateFlow(LoginFormState())
    val form: StateFlow<LoginFormState> = _form

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState: StateFlow<UiState<Unit>> = _loginState

    fun onEmailChange(value: String) {
        _form.update { it.copy(email = value) }
    }

    fun onPasswordChange(value: String) {
        _form.update { it.copy(password = value) }
    }

    fun login() {
        val email = form.value.email.trim()
        val password = form.value.password

        if (email.isBlank() || password.isBlank()) {
            _loginState.value = UiState.Error("이메일과 비밀번호를 입력해주세요.")
            return
        }

        viewModelScope.launch {
            _loginState.value = UiState.Loading
            try {
                // Repository에서 토큰 저장 + JWT 디코딩 + profile 저장까지 끝남(4단계)
                authRepository.login(email, password)

                // 여기서는 "성공"만 알림
                _loginState.value = UiState.Success(Unit)
            } catch (e: ApiException) {
                _loginState.value = UiState.Error(e.errorResponse.message)
            } catch (e: Exception) {
                _loginState.value = UiState.Error(e.message ?: "로그인 중 오류가 발생했습니다.")
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = UiState.Idle
    }
}
