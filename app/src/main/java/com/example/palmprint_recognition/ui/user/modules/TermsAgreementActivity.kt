package com.example.palmprint_recognition.ui.user.modules

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.palmprint_recognition.R

/**
 * 약관 동의 화면.
 *
 * 사용자가 약관에 동의하면 회원가입 완료 화면(SignUpCompleteActivity)로 이동한다.
 * 동의하지 않으면 이전 화면(UserSignUpActivity)으로 돌아간다.
 */
class TermsAgreementActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_terms_agreement)

        applyWindowInsets()
        setupButtons()
    }

    /**
     * 시스템 UI(상단바/하단바) 패딩을 적용한다.
     */
    private fun applyWindowInsets()
    {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * "동의" / "동의 안 함" 버튼 클릭 이벤트를 설정한다.
     */
    private fun setupButtons()
    {
        val buttonAgree = findViewById<Button>(R.id.buttonAgree)
        val buttonDisagree = findViewById<Button>(R.id.buttonDisagree)

        // 약관 동의 → 가입 완료 화면 이동
        buttonAgree.setOnClickListener {
            navigateToSignUpComplete()
        }

        // 약관 비동의 → 회원가입 화면으로 돌아감
        buttonDisagree.setOnClickListener {
            finish() // 현재 Activity만 종료 → 이전 화면 복귀
        }
    }

    /**
     * 가입 완료 화면으로 이동한다.
     */
    private fun navigateToSignUpComplete()
    {
        val intent = Intent(this, SignUpCompleteActivity::class.java)
        startActivity(intent)
    }
}
