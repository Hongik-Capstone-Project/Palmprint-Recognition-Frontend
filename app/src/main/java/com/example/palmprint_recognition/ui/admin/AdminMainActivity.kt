package com.example.palmprint_recognition.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.palmprint_recognition.R
import com.example.palmprint_recognition.ui.admin.dashboard.AdminDashboardActivity


/**
 * UC-18 / 관리자 메인 화면
 *
 * - 기본 UI 구성은 UserMainActivity와 동일
 * - 관리자 전용 기능: "관리자 대시보드로 이동" 버튼 추가
 */
class AdminMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    /**
     * UI 초기화
     * - 관리자 대시보드 이동 버튼 이벤트 설정
     */
    private fun initViews() {
        val buttonAdminDashboard = findViewById<Button>(R.id.buttonGoAdminDashboard)

        buttonAdminDashboard?.setOnClickListener {
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
        }
    }
}
