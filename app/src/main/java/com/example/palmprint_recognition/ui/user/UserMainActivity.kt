package com.example.palmprint_recognition.ui.user

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.palmprint_recognition.R

class UserMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    private fun initViews() {
        val textUserName = findViewById<TextView>(R.id.textUserName)
        val textUserEmail = findViewById<TextView>(R.id.textUserEmail)

        val buttonManageAgency = findViewById<Button>(R.id.buttonManageAgency)
        val buttonManagePayment = findViewById<Button>(R.id.buttonManagePayment)
        val buttonRegisterPalm = findViewById<Button>(R.id.buttonRegisterPalm)
        val buttonDeletePalm = findViewById<Button>(R.id.buttonDeletePalm)
        val buttonViewHistory = findViewById<Button>(R.id.buttonViewHistory)
        val buttonGuideline = findViewById<Button>(R.id.buttonGuideline)

        // TextView로 수정
        val buttonLogout = findViewById<TextView>(R.id.buttonLogout)
        val buttonWithdraw = findViewById<TextView>(R.id.buttonWithdraw)

        // TODO: ViewModel 연동
        textUserName.text = "OO"
        textUserEmail.text = "email@example.com"

        // TODO: 각 버튼 클릭 이벤트 연결 예정
    }
}
