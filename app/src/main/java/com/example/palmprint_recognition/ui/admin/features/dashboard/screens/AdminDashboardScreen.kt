package com.example.palmprint_recognition.ui.admin.features.dashboard.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.admin.features.dashboard.components.DashboardFooterButtonsSection
import com.example.palmprint_recognition.ui.admin.features.dashboard.components.DashboardManagementSection
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable

@Composable
fun AdminDashboardScreen(
    onUserManageClick: () -> Unit,
    onDeviceManageClick: () -> Unit,
    onReportManageClick: () -> Unit,
    onVerificationClick: () -> Unit,
    authViewModel: AuthViewModel
) {
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
                        authViewModel.logoutLocal()
                    }
                ) {
                    Text("예")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("아니오")
                }
            }
        )
    }

    RootLayoutScrollable(
        sectionGap = 12.dp,

        // ===============================
        // HEADER
        // ===============================
        header = {
            HeaderContainer()
        },

        // ===============================
        // BODY
        // ===============================
        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                DashboardManagementSection(
                    onUserManageClick = onUserManageClick,
                    onDeviceManageClick = onDeviceManageClick,
                    onReportManageClick = onReportManageClick
                )
            }
        },

        // ===============================
        // FOOTER
        // ===============================
        footer = {
            Footer {
                DashboardFooterButtonsSection(
                    onVerificationClick = onVerificationClick,
                    onLogoutClick = {
                        // 바로 로그아웃 X → 다이얼로그 표시
                        showLogoutDialog = true
                    }
                )
            }
        }
    )
}
