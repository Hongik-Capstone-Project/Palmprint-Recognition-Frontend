package com.example.palmprint_recognition.ui.auth

object AuthRoutes {
    const val ROLE_SELECTION = "role_selection"
    const val LOGIN = "login/{role}"

    fun login(role: String) = "login/$role"

    const val SIGN_UP = "sign_up"
}
