package com.example.palmprint_recognition.ui.auth.features.login.state

data class AuthState(
    val isLoggedIn: Boolean = false,
    val role: String? = null, // "ADMIN" or "USER"
    val name: String? = null,
    val email: String? = null
)
