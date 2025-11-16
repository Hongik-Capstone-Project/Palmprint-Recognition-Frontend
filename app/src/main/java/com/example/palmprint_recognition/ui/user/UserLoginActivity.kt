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
import com.example.palmprint_recognition.ui.user.UserSignUpActivity
import com.example.palmprint_recognition.ui.user.modules.SignUpCompleteActivity

/**
 * UC-1: 로그인 화면
 *
 * 사용자가 이메일/비밀번호를 입력하고 로그인 버튼을 누르는 화면입니다.
 * 현재 단계에서는 서버 로그인 API 연동 전이므로,
 * 버튼 클릭 시 입력값 검증 및 테스트용 이동만 처리합니다.
 *
 * 이후 단계에서 ViewModel(onLoginClicked)과 Repository(loginUser) 연동 예정입니다.
 */
class UserLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_login)

        // 기존 자동 생성된 시스템 UI 패딩 코드
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
     * UC-1의 입력 필드/버튼을 초기화하고 클릭 이벤트를 연결합니다.
     */
    private fun initViews() {
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonGoSignUp = findViewById<Button>(R.id.buttonGoSignUp)

        // 로그인 버튼 클릭
        buttonLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            // 입력 검증 추가 (UC-1 기본 조건)
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /**
             * 여기서 ViewModel의 onLoginClicked(email, password) 호출 예정
             *
             * 현재는 테스트 단계이므로 임시로 가입완료 화면으로 이동.
             */
            val intent = Intent(this, SignUpCompleteActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 → UC-2 (기본정보 입력 화면)
        buttonGoSignUp.setOnClickListener {
            val intent = Intent(this, UserSignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
