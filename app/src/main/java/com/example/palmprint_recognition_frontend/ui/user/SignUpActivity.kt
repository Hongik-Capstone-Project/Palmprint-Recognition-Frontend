package com.example.palmprint_recognition_frontend.ui.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.palmprint_recognition_frontend.ui.common.AuthViewModel

/**
 * 회원가입 화면 Activity
 *
 * 이름, 이메일, 비밀번호, 비밀번호 확인을 입력받아
 * 회원가입 요청을 트리거한다.
 */
class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: setContentView(...) 및 ViewBinding 설정
        // TODO: viewModel 초기화
        // TODO: 버튼 클릭 리스너 등록
    }

    /**
     * 회원가입 버튼 클릭 시 호출되는 콜백 함수.
     *
     * @param name 사용자가 입력한 이름
     * @param email 사용자가 입력한 이메일
     * @param password 사용자가 입력한 비밀번호
     * @param confirmPassword 비밀번호 확인 값
     */
    private fun onSignUpButtonClicked(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        // viewModel.onSignupClicked(name, email, password, confirmPassword)
    }

    /**
     * 로그인 화면으로 돌아가는 텍스트 클릭 시 호출되는 콜백 함수
     */
    private fun onBackToLoginClicked() {
        // TODO: UserLoginActivity로 이동
    }
}
