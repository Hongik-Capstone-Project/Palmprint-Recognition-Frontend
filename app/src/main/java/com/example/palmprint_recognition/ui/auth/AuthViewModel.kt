package com.example.palmprint_recognition.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.local.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isInitialized: Boolean = false,
    val isLoggedIn: Boolean = false,
    val role: String? = null,   // "ADMIN" or "USER"
    val name: String? = null,
    val email: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val prefs: PreferenceManager
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    init {
        refreshAuthState()
    }

    fun refreshAuthState() {
        viewModelScope.launch {
            val profile = prefs.getProfile()
            _authState.value = AuthState(
                isInitialized = true,
                isLoggedIn = prefs.isLoggedIn(), // (권장) 토큰 존재 + 만료 전
                role = profile?.role,            // Repository에서 "ADMIN"/"USER"로 저장됨
                name = profile?.name,
                email = profile?.email
            )
        }
    }

    // (선택) 화면에서 로그아웃 버튼 붙일 때 유용
    fun logoutLocal() {
        viewModelScope.launch {
            prefs.clearAllAuth()
            refreshAuthState()
        }
    }
}
