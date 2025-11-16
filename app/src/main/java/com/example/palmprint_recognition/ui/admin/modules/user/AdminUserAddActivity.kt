package com.example.palmprint_recognition.ui.admin.modules.user

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.palmprint_recognition.R

/**
 * UC-19: 유저 정보 추가 화면
 *
 * - 유저 목록 페이지에서 "유저 정보 추가" 버튼 클릭 시 진입
 * - 현재는 입력값 검증 없이 finish()만 수행
 * - 이후 AdminRepository.createUser(...) 연동 예정
 */
class AdminUserAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_user_add)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    private fun initViews() {
        val editName = findViewById<EditText>(R.id.editNewUserName)
        val editEmail = findViewById<EditText>(R.id.editNewUserEmail)
        val editPassword = findViewById<EditText>(R.id.editNewUserPassword)
        val editPasswordConfirm = findViewById<EditText>(R.id.editNewUserPasswordConfirm)
        val checkboxIsAdmin = findViewById<CheckBox>(R.id.checkboxNewIsAdmin)
        val buttonAdd = findViewById<Button>(R.id.buttonAddUserConfirm)

        buttonAdd.setOnClickListener {
            // TODO: 입력값 검증 + AdminRepository.createUser(...) 연동 예정
            finish()
        }
    }
}
