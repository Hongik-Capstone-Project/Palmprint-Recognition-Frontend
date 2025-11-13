package com.example.palmprint_recognition_frontend.data.model

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
 * @property status 성공/실패 상태
 * @property userId 유저 식별자
 * @property accessToken 액세스 토큰
 * @property refreshToken 리프레시 토큰
 */
data class LoginResponse(
    val status: String,
    val userId: String,
    val accessToken: String,
    val refreshToken: String
)

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
 * 회원가입 응답 데이터 클래스
 *
 * @property status 성공/실패 상태
 * @property message 서버가 반환하는 메시지
 */
data class SignUpResponse(
    val status: String,
    val message: String
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

