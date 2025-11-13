package com.example.palmprint_recognition_frontend.ui.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.palmprint_recognition_frontend.ui.common.AuthViewModel

/**
 * 유저 로그인 화면 Activity
 *
 * 이메일과 비밀번호를 입력받고 로그인 요청을 트리거한다.
 */
class UserLoginActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: setContentView(...) 및 ViewBinding 설정
        // TODO: viewModel 초기화
        // TODO: 버튼 클릭 리스너 등록
    }

    /**
     * 로그인 버튼 클릭 시 호출되는 콜백 함수
     *
     * @param email 사용자가 입력한 이메일
     * @param password 사용자가 입력한 비밀번호
     */
    private fun onLoginButtonClicked(email: String, password: String) {
        // View에서 ViewModel의 onLoginClicked를 호출
        // viewModel.onLoginClicked(email, password)
    }

    /**
     * 회원가입 링크 클릭 시 호출되는 콜백 함수.
     *
     * 회원가입 화면으로 네비게이션한다.
     */
    private fun onSignupTextClicked() {
        // startActivity(Intent(this, SignUpActivity::class.java))
        // TODO: Intent로 SignUpActivity 이동
    }

}
