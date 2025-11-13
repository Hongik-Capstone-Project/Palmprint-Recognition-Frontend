package com.example.palmprint_recognition_frontend.data.api

import com.example.palmprint_recognition_frontend.data.model.LoginRequest
import com.example.palmprint_recognition_frontend.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 인증 관련 REST API 인터페이스
 */
interface AuthApi {

    /**
     * 로그인 API 엔드포인트
     *
     * @param request 이메일과 비밀번호를 포함한 로그인 요청 바디
     * @return 로그인 응답
     */
    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse


    /**
     * 회원가입 API 엔드포인트
     *
     * @param request 이름, 이메일, 비밀번호를 포함한 회원가입 요청 바디
     * @return 회원가입 응답
     */
    @POST("/api/auth/signup")
    suspend fun signup(
        @Body request: SignUpRequest
    ): SignUpResponse


    /**
     * 로그아웃 API 엔드포인트
     *
     * @return 성공 여부 메시지
     */
    @DELETE("/api/auth/logout")
    suspend fun logout(): LogoutResponse

}
