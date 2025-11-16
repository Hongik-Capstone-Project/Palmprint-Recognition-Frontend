package com.example.palmprint_recognition.ui.admin

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

/**
 * UC-17/18: 관리자 로그인 화면
 *
 * - 관리자 이메일/비밀번호 입력
 * - 입력 검증 처리 (공백 검사)
 * - 로그인 버튼 클릭 시 AdminMainActivity로 이동 (테스트 단계)
 *
 * 추후 AdminViewModel + API 연동 예정
 */
class AdminLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_login)

        // 시스템 UI 패딩 자동 적용
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
     * - 입력 필드(EditText) 세팅
     * - 로그인 버튼 이벤트 등록
     */
    private fun initViews() {
        val editEmail = findViewById<EditText>(R.id.editAdminEmail)
        val editPassword = findViewById<EditText>(R.id.editAdminPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonAdminLogin)

        /**
         * 관리자 로그인 버튼 클릭 이벤트
         * - 빈 입력 검증
         * - 성공 가정 후 AdminMainActivity 이동
         */
        buttonLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            // 공백 검증 (유저 로그인과 동일하게 strings.xml 메시지 사용)
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, getString(R.string.login_error_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 현재는 네비게이션 테스트용: 관리자 메인 화면 이동
            val intent = Intent(this, AdminMainActivity::class.java)
            startActivity(intent)
        }
    }
}
