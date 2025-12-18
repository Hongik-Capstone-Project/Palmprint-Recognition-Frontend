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
                isLoggedIn = prefs.isLoggedIn(),
                role = profile?.role,
                name = profile?.name,
                email = profile?.email
            )
        }
    }

    fun logoutLocal() {
        viewModelScope.launch {
            prefs.clearAllAuth()
            refreshAuthState()
        }
    }
}
