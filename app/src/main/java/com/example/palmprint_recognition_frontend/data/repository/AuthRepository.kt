package com.example.palmprint_recognition_frontend.data.repository

import com.example.palmprint_recognition_frontend.data.api.AuthApi
import com.example.palmprint_recognition_frontend.data.model.LoginRequest
import com.example.palmprint_recognition_frontend.data.model.LoginResponse

/**
 * 인증 관련 API 호출을 담당하는 Repository
 */
class AuthRepository(
    private val authApi: AuthApi
) {

    /**
     * 로그인 API를 호출한다.
     *
     * @param email 로그인에 사용할 이메일
     * @param password 로그인에 사용할 비밀번호
     * @return 서버에서 받은 로그인 응답 데이터
     */
    suspend fun login(email: String, password: String): LoginResponse {
        return authApi.login(LoginRequest(email, password))
    }


    /**
     * 회원가입 API를 호출한다.
     *
     * @param name 사용자 이름
     * @param email 사용자 이메일
     * @param password 사용자 비밀번호
     * @return 서버에서 받은 회원가입 결과 응답
     */
    suspend fun signup(
        name: String,
        email: String,
        password: String
    ): SignUpResponse {
        return authApi.signup(SignUpRequest(name, email, password))
    }


    /**
     * 로그아웃 API를 호출한다.
     *
     * @return 로그아웃 응답 결과
     */
    suspend fun logout(): LogoutResponse {
        return authApi.logout()
    }

}
