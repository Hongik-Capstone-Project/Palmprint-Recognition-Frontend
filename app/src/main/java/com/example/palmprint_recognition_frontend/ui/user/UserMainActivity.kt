package com.example.palmprint_recognition_frontend.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.palmprint_recognition_frontend.ui.common.AuthViewModel
import com.example.palmprint_recognition_frontend.ui.user.UserLoginActivity

/**
 * 사용자 메인 화면
 * 로그인 성공 후 진입하는 기본 홈 화면이며,
 * 로그아웃 버튼 클릭 시 AuthViewModel을 통해 로그아웃을 요청하고
 * 로그인 화면(UserLoginActivity)로 이동한다.
 */
class UserMainActivity : AppCompatActivity() {

    // ViewModel 선언 (DI 또는 Factory는 추후 구현)
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: setContentView(R.layout.activity_user_main)

        // TODO: authViewModel = ViewModelProvider...

        // TODO: 로그아웃 버튼 클릭 리스너 설정
        // logoutButton.setOnClickListener { onLogoutButtonClicked() }
    }

    /**
     * 로그아웃 버튼 클릭 시 호출되는 UI 트리거 함수
     *
     * ViewModel에게 로그아웃 로직을 위임하고,
     * 성공 시 로그인 화면(UserLoginActivity)로 이동한다.
     */
    private fun onLogoutButtonClicked() {
        // TODO: authViewModel.onLogoutClicked()

        // TODO: Observe Logout State
        // authViewModel.logoutState.observe(this) { state -> ... }

        // 로그인 화면으로 이동 (UI-Spec 기준)
        val intent = Intent(this, UserLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
