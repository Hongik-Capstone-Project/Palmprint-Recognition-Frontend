package com.example.palmprint_recognition.data.repository

import com.example.palmprint_recognition.data.api.AuthApi
import com.example.palmprint_recognition.data.model.ApiException
import com.example.palmprint_recognition.data.model.ErrorResponse
import com.example.palmprint_recognition.data.model.LoginRequest
import com.example.palmprint_recognition.data.model.LoginResponse
import com.example.palmprint_recognition.data.model.LogoutResponse
import com.example.palmprint_recognition.data.model.SignUpRequest
import com.example.palmprint_recognition.data.model.SignUpResponse
import com.google.gson.Gson
import retrofit2.HttpException
import com.example.palmprint_recognition.data.local.PreferenceManager
import javax.inject.Inject


/**
 * 인증 관련 API 호출을 담당하는 Repository
 * API 호출 시 발생하는 예외를 [ApiException]으로 변환하여 throw 합니다.
 */
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val prefs: PreferenceManager,
    private val gson: Gson
) {

    /**
     * 회원가입 API를 호출한다.
     * 성공 시 [SignUpResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param name 사용자 이름
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @return 서버에서 받은 회원가입 결과 응답
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun signup(
        name: String,
        email: String,
        password: String
    ): SignUpResponse {
        return try {
            authApi.signup(SignUpRequest(name, email, password))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 회원탈퇴 api 호출
     * 성공 시 Unit을 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun signout() {
        try {
            authApi.signout()
            prefs.clearTokens()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 로그인 API를 호출한다.
     * 성공 시 [LoginResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @param email 로그인에 사용할 이메일
     * @param password 로그인에 사용할 비밀번호
     * @return 서버에서 받은 로그인 응답 데이터
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            val response = authApi.login(LoginRequest(email, password))
            // 토큰 저장
            prefs.saveTokens(response.accessToken, response.refreshToken)
            response
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /**
     * 로그아웃 API를 호출한다.
     * 성공 시 [LogoutResponse]를 반환하고, 실패 시 [ApiException]을 throw 합니다.
     *
     * @return 로그아웃 응답 결과
     * @throws ApiException API 호출 실패 시 발생
     */
    suspend fun logout(): LogoutResponse {
        return try {
            val res = authApi.logout()
            prefs.clearTokens()
            res
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            throw ApiException(errorResponse)
        }
    }

    /** 저장된 역할 가져오기 */
    fun getSavedRole(): String? = prefs.getRole()

    /** 역할 저장 */
    fun saveRole(role: String) {
        prefs.saveRole(role)
    }

    /** 로그인 여부 */
    fun isLoggedIn(): Boolean = prefs.isLoggedIn()

    private fun parseError(e: HttpException): ApiException {
        val errJson = e.response()?.errorBody()?.string()
        val error = gson.fromJson(errJson, ErrorResponse::class.java)
        return ApiException(error)
    }
}