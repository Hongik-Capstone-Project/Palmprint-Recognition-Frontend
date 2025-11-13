package com.example.palmprint_recognition_frontend.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.palmprint_recognition_frontend.ui.admin.modules.DeviceManageActivity
import com.example.palmprint_recognition_frontend.ui.admin.modules.UserManageActivity
import com.example.palmprint_recognition_frontend.ui.admin.modules.PalmManageActivity
import com.example.palmprint_recognition_frontend.ui.admin.modules.StatsDashboardActivity
import com.example.palmprint_recognition_frontend.ui.common.AuthViewModel
import com.example.palmprint_recognition_frontend.ui.admin.AdminLoginActivity

/**
 * 관리자 메인 대시보드 화면
 * 관리자는 로그인 후 이 화면에서 유저 관리, 디바이스 관리,
 * 손바닥 정보 관리, 통계 조회 등으로 이동할 수 있다.
 *
 * 로그아웃 버튼 클릭 시 AuthViewModel을 통해 서버 로그아웃을 요청하며
 * AdminLoginActivity로 이동한다.
 */
class AdminMainActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: setContentView(R.layout.activity_admin_main)

        // TODO: authViewModel = ViewModelProvider...

        // TODO: 로그아웃 버튼 이벤트 설정
        // logoutButton.setOnClickListener { onAdminLogoutButtonClicked() }

        // TODO: 각 메뉴 버튼 클릭 설정
        // btnUserManage.setOnClickListener { navigateToUserManage() }
        // btnDeviceManage.setOnClickListener { navigateToDeviceManage() }
        // btnPalmManage.setOnClickListener { navigateToPalmManage() }
        // btnStats.setOnClickListener { navigateToStats() }
    }

    /**
     * 관리자 로그아웃 클릭 시 호출되는 함수
     * ViewModel의 로그아웃 함수를 실행하고 로그인 화면으로 이동한다.
     */
    private fun onAdminLogoutButtonClicked() {
        // TODO: authViewModel.onLogoutClicked()

        val intent = Intent(this, AdminLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    /**
     * 유저 관리 화면 이동
     */
    private fun navigateToUserManage() {
        val intent = Intent(this, UserManageActivity::class.java)
        startActivity(intent)
    }

    /**
     * 디바이스 관리 화면 이동
     */
    private fun navigateToDeviceManage() {
        val intent = Intent(this, DeviceManageActivity::class.java)
        startActivity(intent)
    }

    /**
     * 손바닥 등록 정보 관리 화면 이동
     */
    private fun navigateToPalmManage() {
        val intent = Intent(this, PalmManageActivity::class.java)
        startActivity(intent)
    }

    /**
     * 통계 대시보드 화면 이동
     */
    private fun navigateToStats() {
        val intent = Intent(this, StatsDashboardActivity::class.java)
        startActivity(intent)
    }
}
