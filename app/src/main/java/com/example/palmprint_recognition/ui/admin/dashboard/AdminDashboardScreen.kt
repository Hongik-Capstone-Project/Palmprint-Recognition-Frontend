package com.example.palmprint_recognition.ui.admin.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 관리자 대시보드 화면.
 *
 * - 이 화면은 기능 테스트를 위한 임시 UI이다.
 * - 지금은 단순히 각 관리 화면으로 이동하는 버튼들을 만들어놓았다.
 *
 * @param onUserManagementClick   사용자 관리 화면 이동 콜백
 * @param onDeviceManagementClick 디바이스 관리 화면 이동 콜백
 * @param onReportManagementClick 신고내역 관리 화면 이동 콜백
 */
@Composable
fun AdminDashboardScreen(
    onUserManagementClick: () -> Unit,
    onDeviceManagementClick: () -> Unit,
    onReportManagementClick: () -> Unit,
    onVerificationManagementClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "관리자 대시보드",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onUserManagementClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("사용자 관리")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onDeviceManagementClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("디바이스 관리")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onReportManagementClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("신고내역 관리")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onVerificationManagementClick,
            modifier = Modifier.fillMaxWidth()
        ) { Text("인증 내역 조회") }
    }
}
