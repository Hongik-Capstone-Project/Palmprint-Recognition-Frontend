package com.example.palmprint_recognition.ui.user.modules

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.palmprint_recognition.R
import com.example.palmprint_recognition.ui.user.UserLoginActivity

/**
 * 회원가입 완료 화면.
 *
 * 회원가입 절차가 모두 끝난 후 사용자에게 완료 메시지를 보여주는 화면이다.
 * 사용자가 로그인 버튼을 누르면 UserLoginActivity로 이동하며,
 * 뒤로가기 버튼을 눌러 다시 이 화면에 돌아오는 것을 방지하기 위해
 * Task 스택을 초기화한다.
 */
class SignUpCompleteActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_complete)

        applyWindowInsets()
        setupButton()
    }

    /**
     * 시스템 UI(상단바·하단바) 영역에 맞춰 패딩을 적용한다.
     */
    private fun applyWindowInsets()
    {
        val root = findViewById<android.view.View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
    }

    /**
     * '로그인 하기' 버튼 클릭 리스너를 설정한다.
     */
    private fun setupButton()
    {
        val buttonGoLogin = findViewById<Button>(R.id.buttonGoLogin)

        buttonGoLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    /**
     * 로그인 화면(UserLoginActivity)으로 이동하며,
     * 뒤로가기 시 다시 이 화면으로 돌아오지 않도록 Task 스택을 초기화한다.
     */
    private fun navigateToLogin()
    {
        val intent = Intent(this, UserLoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        finish()
    }
}
