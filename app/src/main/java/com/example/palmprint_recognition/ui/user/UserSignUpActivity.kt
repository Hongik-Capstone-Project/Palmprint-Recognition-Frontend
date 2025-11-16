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
import com.example.palmprint_recognition.ui.user.modules.TermsAgreementActivity

/**
 * UC-2: 회원가입 기본정보 입력 화면
 *
 * 사용자가 이름, 이메일, 비밀번호를 입력하는 화면입니다.
 * 입력값 검증 후 UC-3(약관 동의 화면)으로 이동합니다.
 *
 * 이후 단계에서 ViewModel(onSignUpBasicInfoEntered)와 Repository(createUserRequest)
 * 연동될 예정입니다.
 */
class UserSignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_sign_up)

        // ⛔ 자동 생성된 Inset 패딩 적용 — 삭제 금지
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    /**
     * UI 요소 초기화 및 버튼 이벤트 설정 함수
     *
     * 회원가입 입력 필드 값을 가져와서 검증한 후
     * 약관 동의 화면(TermsAgreementActivity)으로 이동합니다.
     */
    private fun initViews() {
        val editName = findViewById<EditText>(R.id.editName)
        val editEmail = findViewById<EditText>(R.id.editSignUpEmail)
        val editPassword = findViewById<EditText>(R.id.editSignUpPassword)
        val editPasswordConfirm = findViewById<EditText>(R.id.editSignUpPasswordConfirm)
        val buttonNextTerms = findViewById<Button>(R.id.buttonNextTerms)

        buttonNextTerms.setOnClickListener {
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val passwordConfirm = editPasswordConfirm.text.toString()

            // -----------------------
            // 🔸 입력 검증 (UC-2 요구)
            // -----------------------
            if (name.isBlank() || email.isBlank() || password.isBlank() || passwordConfirm.isBlank()) {
                Toast.makeText(this, getString(R.string.signup_error_empty_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != passwordConfirm) {
                Toast.makeText(this, getString(R.string.signup_error_password_mismatch), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // -----------------------
            // 🔸 UC-3로 이동 (입력값 전달)
            // -----------------------
            val intent = Intent(this, TermsAgreementActivity::class.java).apply {
                putExtra("name", name)
                putExtra("email", email)
                putExtra("password", password)
            }

            startActivity(intent)
        }
    }
}
