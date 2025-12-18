package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.api.AuthApi
import com.example.palmprint_recognition.data.local.JwtUtils
import com.example.palmprint_recognition.data.local.PreferenceManager
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.model.ErrorResponse
import com.example.palmprint_recognition.data.model.JwtPayload
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

    // ----------------------------
    // Error parsing
    // ----------------------------
    private fun parseError(e: HttpException): ApiException {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            ApiException(errorResponse)
        } catch (_: Exception) {
            val errorMessage = e.response()?.message() ?: "An unknown error occurred"
            ApiException(ErrorResponse("parse_error", errorMessage))
        }
    }

    private fun normalizeRole(serverRole: String): String {
        return if (serverRole.lowercase() == "admin") "ADMIN" else "USER"
    }

    // ----------------------------
    // Signup
    // ----------------------------
    override suspend fun signup(name: String, email: String, password: String): SignUpResponse {
        return try {
            authApi.signup(SignUpRequest(name, email, password))
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    // ----------------------------
    // Login
    // ----------------------------
    override suspend fun login(email: String, password: String): LoginResponse {
        return try {
            val response = authApi.login(LoginRequest(email, password))

            // 1) 토큰 저장 (+ 만료시각 계산 저장)
            prefs.saveTokens(
                access = response.accessToken,
                refresh = response.refreshToken,
                expiresInSeconds = response.expiresIn
            )

            // 2) JWT 디코딩 → payload 파싱
            val payload = JwtUtils.decodePayloadAs<JwtPayload>(response.accessToken)

            // 3) profile 추출 + role 정규화
            val role = normalizeRole(payload.user.role)

            // 4) profile 저장 (프로필 카드용)
            prefs.saveProfile(
                userId = payload.user.id,
                name = payload.user.name,
                email = payload.user.email,
                role = role
            )

            response
        } catch (e: HttpException) {
            throw parseError(e)
        } catch (e: Exception) {
            // JWT 파싱 실패 등 (token format 문제 등)
            throw ApiException(ErrorResponse("jwt_parse_error", e.message ?: "JWT parse error"))
        }
    }

    // ----------------------------
    // Logout
    // ----------------------------
    override suspend fun logout(): LogoutResponse {
        // 현재 설계: 서버 로그아웃 미사용 → 로컬에서만 정리
        prefs.clearAllAuth()
        return LogoutResponse(
            status = "ok",
            message = "Logged out locally"
        )
    }


    override suspend fun signout() {
        try {
            authApi.signout()
            prefs.clearAllAuth()
        } catch (e: HttpException) {
            throw parseError(e)
        }
    }

    // ----------------------------
    // Local state
    // ----------------------------
    override fun getSavedRole(): String? = prefs.getRole()

    override fun saveRole(role: String) {
        // 이제 직접 호출할 필요 없음 (login()에서 자동 저장)
        // 남겨두고 싶으면 no-op 처리
    }

    override fun isLoggedIn(): Boolean = prefs.isLoggedIn()
}
