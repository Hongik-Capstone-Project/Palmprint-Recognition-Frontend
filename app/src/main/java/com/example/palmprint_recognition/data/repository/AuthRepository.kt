package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.model.LoginResponse
import com.example.palmprint_recognition.data.model.LogoutResponse
import com.example.palmprint_recognition.data.model.SignUpResponse

/**
 * 인증 관련 기능에 대한 Repository 인터페이스 (설계도)
 */
interface AuthRepository {
    suspend fun signup(name: String, email: String, password: String): SignUpResponse

    suspend fun signout()

    suspend fun login(email: String, password: String): LoginResponse

    suspend fun logout(): LogoutResponse

    fun getSavedRole(): String?

    fun saveRole(role: String)

    fun isLoggedIn(): Boolean
}