package com.example.palmprint_recognition.ui.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.palmprint_recognition.R
import com.example.palmprint_recognition.ui.admin.AdminLoginActivity
import com.example.palmprint_recognition.ui.user.modules.SignUpCompleteActivity
import com.example.palmprint_recognition.ui.user.UserSignUpActivity

/**
 * UC-1: 사용자 로그인 화면
 *
 * - 이메일/비밀번호 입력
 * - 로그인 버튼 클릭 시 입력 검증 수행
 * - 회원가입 화면 이동
 * - 관리자 로그인 화면 이동 (UC-17)
 *
 * 이후 단계에서 AuthViewModel 연동하여
 * 실제 로그인 API 처리 예정.
 */
class UserLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_login)

        // 시스템 UI 패딩 처리
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    /**
     * UI 초기화 함수
     *
     * - 모든 입력 필드(EditText) 초기화
     * - 로그인/회원가입/관리자 로그인 버튼 이벤트 연결
     */
    private fun initViews() {
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonGoSignUp = findViewById<Button>(R.id.buttonGoSignUp)

        // 추가된 관리자 로그인 버튼
        val buttonGoAdminLogin = findViewById<Button>(R.id.buttonGoAdminLogin)

        /**
         * 로그인 버튼 클릭 처리
         *
         * 이후 ViewModel.onLoginClicked(email, password) 로 교체 예정.
         */
        buttonLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, getString(R.string.login_error_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 로그인 성공 → UserMainActivity 이동
            val intent = Intent(this, UserMainActivity::class.java)
            startActivity(intent)

        }

        /**
         * 회원가입 버튼 → UC-2 (기본 정보 입력)
         */
        buttonGoSignUp.setOnClickListener {
            val intent = Intent(this, UserSignUpActivity::class.java)
            startActivity(intent)
        }

        /**
         * 관리자 로그인 버튼 → AdminLoginActivity (UC-17)
         */
        buttonGoAdminLogin.setOnClickListener {
            val intent = Intent(this, AdminLoginActivity::class.java)
            startActivity(intent)
        }
    }
}
