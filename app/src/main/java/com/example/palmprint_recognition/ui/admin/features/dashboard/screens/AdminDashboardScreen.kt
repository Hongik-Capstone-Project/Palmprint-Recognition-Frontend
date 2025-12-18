package com.example.palmprint_recognition.ui.admin.features.dashboard.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.admin.features.dashboard.components.DashboardFooterButtonsSection
import com.example.palmprint_recognition.ui.admin.features.dashboard.components.DashboardManagementSection
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayout

@Composable
fun AdminDashboardScreen(
    onUserManageClick: () -> Unit,
    onDeviceManageClick: () -> Unit,
    onReportManageClick: () -> Unit,
    onVerificationClick: () -> Unit,
    authViewModel: AuthViewModel //추가
) {
    //val authViewModel: AuthViewModel = hiltViewModel()

    var showLogoutDialog by remember { mutableStateOf(false) }

    // ✅ 로그아웃 확인 다이얼로그
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "로그아웃") },
            text = { Text(text = "정말 로그아웃 하시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        // ✅ 로컬 로그아웃 → authState 갱신 → AppNavHost가 Auth 그래프로 교체
                        authViewModel.logoutLocal()
                    }
                ) {
                    Text("예")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("아니오")
                }
            }
        )
    }

    RootLayout(
        headerWeight = 2f,
        bodyWeight = 5f,
        footerWeight = 3f,
        sectionGapWeight = 0.4f,

        header = { HeaderContainer() },

        body = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                DashboardManagementSection(
                    onUserManageClick = onUserManageClick,
                    onDeviceManageClick = onDeviceManageClick,
                    onReportManageClick = onReportManageClick
                )
            }
        },

        footer = {
            Footer {
                DashboardFooterButtonsSection(
                    onVerificationClick = onVerificationClick,
                    onLogoutClick = {
                        // ✅ 여기서 바로 logoutLocal() 하지 말고, 다이얼로그 먼저
                        showLogoutDialog = true
                    }
                )
            }
        }
    )
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun PreviewAdminDashboardScreen() {
//    AdminDashboardScreen(
//        onUserManageClick = {},
//        onDeviceManageClick = {},
//        onReportManageClick = {},
//        onVerificationClick = {}
//    )
//}
