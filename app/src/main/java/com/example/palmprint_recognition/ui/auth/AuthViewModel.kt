package com.example.palmprint_recognition.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.AuthRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ============================================================================
 * AuthViewModel
 * - 로그인 / 회원가입 / 탈퇴 / 인증 상태 관리
 * ============================================================================
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    /* ---------------------- 로그인 상태 ---------------------- */
    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState = _loginState.asStateFlow()

    /* ---------------------- 회원가입 상태 ---------------------- */
    private val _signUpState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val signUpState = _signUpState.asStateFlow()

    /* ---------------------- 탈퇴 상태 ---------------------- */
    private val _deleteAccountState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val deleteAccountState = _deleteAccountState.asStateFlow()

    /* ---------------------- 앱 전체 인증 상태 ---------------------- */
    private val _authState = MutableStateFlow(
        AuthState(
            isLoggedIn = authRepository.isLoggedIn(),
            role = authRepository.getSavedRole()
        )
    )
    val authState = _authState.asStateFlow()

    /**
     * 로그인 API
     */
    fun onLoginClick(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = UiState.Loading

            try {
                authRepository.login(email, password)

                val role = if (email.contains("admin")) "ADMIN" else "USER"
                authRepository.saveRole(role)

                _authState.value = AuthState(
                    isLoggedIn = true,
                    role = role
                )
                _loginState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _loginState.value = UiState.Error(
                    e.message ?: "로그인 중 오류가 발생했습니다."
                )
            }
        }
    }

    /* ---------------------- 리셋 ---------------------- */
    fun resetLoginState() { _loginState.value = UiState.Idle }

    /**
     * 회원가입 API
     */
    fun onSignUpClick(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpState.value = UiState.Loading

            try {
                authRepository.signup(name, email, password)
                _signUpState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _signUpState.value = UiState.Error(
                    e.message ?: "회원가입 중 오류가 발생했습니다."
                )
            }
        }
    }

    fun resetSignUpState() { _signUpState.value = UiState.Idle }

    /**
     * 회원탈퇴 API
     */
    fun onDeleteAccountClick() {
        viewModelScope.launch {
            _deleteAccountState.value = UiState.Loading

            try {
                authRepository.signout()

                _authState.value = AuthState(isLoggedIn = false, role = null)

                _deleteAccountState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _deleteAccountState.value = UiState.Error(
                    e.message ?: "회원탈퇴 중 오류가 발생했습니다."
                )
            }
        }
    }

    fun resetDeleteAccountState() { _deleteAccountState.value = UiState.Idle }
}

/**
 * 앱 전체 인증 상태
 */
data class AuthState(
    val isLoggedIn: Boolean = false,
    val role: String? = null
)
