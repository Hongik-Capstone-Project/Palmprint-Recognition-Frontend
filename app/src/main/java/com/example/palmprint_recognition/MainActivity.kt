package com.example.palmprint_recognition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.palmprint_recognition.ui.navigation.AppNavHost
import com.example.palmprint_recognition.ui.common.theme.PalmprintRecognitionTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * ===========================================================================
 * MainActivity
 * 앱 전체의 시작점
 *
 * - Hilt 초기화
 * - NavHostController 생성 (AppNavHost 내부에서 처리)
 * - AppNavHost를 호출 → 로그인 / 관리자 / 사용자 네비게이션 분기
 * ===========================================================================
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PalmprintRecognitionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // 앱 전체 네비게이션 Root
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
