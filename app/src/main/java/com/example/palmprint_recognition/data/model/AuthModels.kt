package com.example.palmprint_recognition.data.model

import com.google.gson.annotations.SerializedName

/**
 * 회원가입 요청 바디 데이터 클래스
 *
 * @property name 사용자 이름
 * @property email 사용자 이메일
 * @property password 사용자 비밀번호
 */
data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)

/**
 * 회원가입 성공 응답 데이터 클래스
 *
 * @property userId 생성된 유저의 식별자
 */
data class SignUpResponse(
    @SerializedName("user_id")
    val userId: String
)


/**
 * 로그인 요청 바디를 나타내는 데이터 클래스
 *
 * @property email 로그인에 사용할 이메일
 * @property password 로그인에 사용할 비밀번호
 */
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * 로그인 응답을 나타내는 데이터 클래스
 *
 * @property accessToken 액세스 토큰
 * @property refreshToken 리프레시 토큰
 * @property tokenType
 * @property expiresIn
 * @property refreshExpiresIn
 */
data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("expires_in")
    val expiresIn: Long,

    @SerializedName("refresh_expires_in")
    val refreshExpiresIn: Long? = null
)


/**
 * 로그아웃 응답 모델
 *
 * @property status API 수행 상태
 * @property message 로그아웃 결과 메시지
 */
data class LogoutResponse(
    val status: String,
    val message: String
)
