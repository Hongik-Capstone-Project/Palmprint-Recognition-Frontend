package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.api.AuthApi
import com.example.palmprint_recognition.data.local.PreferenceManager
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.model.ErrorResponse
import com.example.palmprint_recognition.data.model.LoginRequest
import com.example.palmprint_recognition.data.model.LoginResponse
import com.example.palmprint_recognition.data.model.LogoutResponse
import com.example.palmprint_recognition.data.model.SignUpRequest
import com.example.palmprint_recognition.data.model.SignUpResponse
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val prefs: PreferenceManager,
    private val gson: Gson
) : AuthRepository {

    private fun parseError(e: HttpException): ApiException {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            ApiException(errorResponse)
        } catch (jsonException: Exception) {
            val errorMessage = e.response()?.message() ?: "An unknown error occurred"
            ApiException(ErrorResponse("parse_error", errorMessage))
        }
    }

    override suspend fun signup(name: String, email: String, password: String): SignUpResponse {
        return try {
            authApi.signup(SignUpRequest(name, email, password))
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun signout() {
        try {
            authApi.signout()
            prefs.clearTokens()
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            prefs.saveTokens(response.accessToken, response.refreshToken)
            response
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override suspend fun logout(): LogoutResponse {
        return try {
            val res = authApi.logout()
            prefs.clearTokens()
            res
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    override fun getSavedRole(): String? = prefs.getRole()

    override fun saveRole(role: String) {
        prefs.saveRole(role)
    }

    override fun isLoggedIn(): Boolean = prefs.isLoggedIn()
}