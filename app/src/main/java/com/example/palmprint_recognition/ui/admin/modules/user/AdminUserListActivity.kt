package com.example.palmprint_recognition.ui.admin.modules.user

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.palmprint_recognition.R

/**
 * UC-19: 유저 목록 페이지
 *
 * - 관리자 대시보드에서 진입
 * - 더미 유저 리스트를 화면에 표시
 * - 유저 아이템 클릭 시 수정 화면(AdminUserEditActivity)으로 이동
 * - "유저 정보 추가" 버튼 클릭 시 AdminUserAddActivity로 이동
 *
 * 추후에는 ViewModel/Repository와 연동하여 실제 유저 데이터 사용 예정.
 */
class AdminUserListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_user_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    /**
     * UI 초기화 및 더미 유저 목록 바인딩
     */
    private fun initViews() {
        val layoutUserList =
            findViewById<LinearLayout>(R.id.layoutUserListContainer)
        val buttonAddUser = findViewById<Button>(R.id.buttonAddUser)

        val dummyUsers = getDummyUsers()

        // 기존 뷰 초기화
        layoutUserList.removeAllViews()

        // 더미 유저 데이터를 TextView로 동적으로 추가
        dummyUsers.forEach { user ->
            val itemView = TextView(this).apply {
                text = "${user.name} (${user.email})"
                textSize = 16f
                setPadding(24, 24, 24, 24)
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 16
                }
                setBackgroundResource(R.drawable.bg_button_gray)
            }

            itemView.setOnClickListener {
                val intent = Intent(this, AdminUserEditActivity::class.java)
                intent.putExtra("userId", user.id)
                intent.putExtra("userName", user.name)
                intent.putExtra("userEmail", user.email)
                startActivity(intent)
            }

            layoutUserList.addView(itemView)
        }

        buttonAddUser.setOnClickListener {
            val intent = Intent(this, AdminUserAddActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * UC-19 구현용 더미 유저 데이터
     */
    private fun getDummyUsers(): List<UserListItem> {
        return listOf(
            UserListItem(id = "1", name = "Alice", email = "alice@example.com"),
            UserListItem(id = "2", name = "Bob", email = "bob@example.com")
        )
    }

    /**
     * 유저 목록 아이템 UI 모델
     */
    data class UserListItem(
        val id: String,
        val name: String,
        val email: String
    )
}
