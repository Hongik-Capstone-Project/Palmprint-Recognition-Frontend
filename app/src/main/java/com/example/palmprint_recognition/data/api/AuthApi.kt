package com.example.palmprint_recognition.data.api

import com.example.palmprint_recognition.data.model.LoginRequest
import com.example.palmprint_recognition.data.model.LoginResponse
import com.example.palmprint_recognition.data.model.LogoutResponse
import com.example.palmprint_recognition.data.model.SignUpRequest
import com.example.palmprint_recognition.data.model.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.DELETE

/**
 * 인증 관련 REST API 인터페이스
 */
interface AuthApi {

    /**
     * 회원가입 API 엔드포인트
     *
     * @param request 이름, 이메일, 비밀번호를 포함한 회원가입 요청 바디
     * @return 회원가입 응답
     */
    @POST("/api/users")
    suspend fun signup(
        @Body request: SignUpRequest
    ): SignUpResponse

    /*
    회원탈퇴 api
     */
    @DELETE("/api/users/me")
    suspend fun signout(): Unit

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
     * 로그아웃 API 엔드포인트
     *
     * @return 성공 여부 메시지
     */
    @DELETE("/api/auth/logout")
    suspend fun logout(): LogoutResponse

}