package com.example.palmprint_recognition.ui.admin.modules.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.palmprint_recognition.R

/**
 * UC-19: 유저 삭제 확인 팝업
 *
 * - "예" 클릭 시 실제 삭제 로직(추후 연동) 후 유저 목록 화면으로 이동
 * - "아니오" 클릭 시 단순 종료
 */
class AdminUserDeleteDialog : AppCompatActivity() {

    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_user_delete_dialog)

        userId = intent.getStringExtra("userId")

        initViews()
    }

    private fun initViews() {
        val buttonYes = findViewById<Button>(R.id.buttonDeleteYes)
        val buttonNo = findViewById<Button>(R.id.buttonDeleteNo)

        buttonYes.setOnClickListener {
            // TODO: AdminRepository.deleteUser(userId) 연동 예정

            val intent = Intent(this, AdminUserListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        buttonNo.setOnClickListener {
            finish()
        }
    }
}
