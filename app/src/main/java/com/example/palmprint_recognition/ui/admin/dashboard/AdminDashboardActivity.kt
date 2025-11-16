package com.example.palmprint_recognition.ui.admin.dashboard

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.palmprint_recognition.R

/**
 * UC-19: 관리자 대시보드 화면
 *
 * - 관리자 기능(유저 관리, 디바이스 관리, Palm 데이터 관리, 통계)로 이동하는 허브 화면
 * - 추후 UC-20~22에서 실제 이동 로직 추가 예정
 */
class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)

        applySystemBarInsets()
        initViews()
    }

    /**
     * 시스템 UI(상단 status bar 등) 패딩 적용
     */
    private fun applySystemBarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * UI 초기화 및 클릭 리스너 연결
     *
     * - 현재 단계는 화면 이동 없이 토스트 or 로그만 출력 (UC-20~22에서 구현)
     */
    private fun initViews() {
        val buttonManageUsers = findViewById<Button>(R.id.buttonManageUsers)
        val buttonManagePalms = findViewById<Button>(R.id.buttonManagePalms)
        val buttonManageDevice = findViewById<Button>(R.id.buttonManageDevice)
        val buttonStats = findViewById<Button>(R.id.buttonStats)

        // 추후 UC-20~22에서 Activity 이동 연결 예정
        buttonManageUsers.setOnClickListener {
            // TODO: startActivity(Intent(this, UserManageActivity::class.java))
        }

        buttonManagePalms.setOnClickListener {
            // TODO: startActivity(Intent(this, PalmManageActivity::class.java))
        }

        buttonManageDevice.setOnClickListener {
            // TODO: startActivity(Intent(this, DeviceManageActivity::class.java))
        }

        buttonStats.setOnClickListener {
            // TODO: startActivity(Intent(this, StatsDashboardActivity::class.java))
        }
    }
}
