package com.example.palmprint_recognition.ui.admin.modules.user

import android.content.Intent
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
 * UC-19: 유저 정보 수정 화면
 *
 * - 유저 목록에서 특정 유저를 선택했을 때 진입
 * - 저장 버튼: (추후 서버 연동) 현재는 단순 finish()로 목록으로 복귀
 * - 삭제 버튼: 삭제 확인 팝업(AdminUserDeleteDialog)으로 이동
 */
class AdminUserEditActivity : AppCompatActivity() {

    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_user_edit)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    /**
     * 전달받은 유저 정보를 폼에 세팅하고 버튼 이벤트를 연결
     */
    private fun initViews() {

        userId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName") ?: ""
        val userEmail = intent.getStringExtra("userEmail") ?: ""

        val editName = findViewById<EditText>(R.id.editUserName)
        val editEmail = findViewById<EditText>(R.id.editUserEmail)
        val editPassword = findViewById<EditText>(R.id.editUserPassword)
        val editPalm = findViewById<EditText>(R.id.editUserPalmInfo)
        val editExtra = findViewById<EditText>(R.id.editUserExtraInfo)
        val checkboxIsAdmin = findViewById<CheckBox>(R.id.checkboxIsAdmin)

        val buttonSave = findViewById<Button>(R.id.buttonSaveUser)
        val buttonDelete = findViewById<Button>(R.id.buttonDeleteUser)

        // 값 세팅
        editName.setText(userName)
        editEmail.setText(userEmail)

        buttonSave.setOnClickListener {
            finish()
        }

        buttonDelete.setOnClickListener {
            val intent = Intent(this, AdminUserDeleteDialog::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }

}
