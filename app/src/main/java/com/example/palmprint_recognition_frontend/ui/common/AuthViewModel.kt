package com.example.palmprint_recognition_frontend.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition_frontend.data.repository.AuthRepository

/**
 * 인증 관련 화면에서 공통으로 사용하는 ViewModel
 *
 * 로그인, 회원가입 등 인증 플로우를 관리한다.
 */
class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    /**
     * 로그인 버튼 클릭 시 호출되는 함수
     *
     * @param email 사용자가 입력한 이메일
     * @param password 사용자가 입력한 비밀번호
     */
    fun onLoginClicked(email: String, password: String) {
        // TODO: viewModelScope.launch { authRepository.login(email, password) ... }
    }


    /**
     * 회원가입 버튼 클릭 시 호출되는 함수
     *
     * @param name 사용자가 입력한 이름
     * @param email 사용자가 입력한 이메일
     * @param password 사용자가 입력한 비밀번호
     * @param confirmPassword 사용자가 입력한 비밀번호 확인 값
     */
    fun onSignupClicked(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        // TODO: viewModelScope.launch { authRepository.signup(...) }
    }


    /**
     * 로그아웃 버튼 클릭 시 호출되는 함수
     *
     * 앱 저장 정보 삭제, 서버 로그아웃 API 호출 등을 트리거한다.
     */
    fun onLogoutClicked() {
        // TODO: viewModelScope.launch { repository.logout() }
    }

}
