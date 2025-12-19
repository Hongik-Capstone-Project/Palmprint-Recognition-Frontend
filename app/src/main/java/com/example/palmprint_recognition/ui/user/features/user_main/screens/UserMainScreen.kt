package com.example.palmprint_recognition.ui.user.features.user_main.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.user.features.user_main.components.MainFooterButtonsSection
import com.example.palmprint_recognition.ui.user.features.user_main.components.MainManagementSection
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun UserMainScreen(
    onInstitutionManageClick: () -> Unit,
    onPaymentManageClick: () -> Unit,
    onRegisterPalmprintClick: () -> Unit,
    onDeletePalmprintClick: () -> Unit,
    onMyVerificationClick: () -> Unit,
    onHowToUseClick: () -> Unit,
    onSignOutClick: () -> Unit,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    val name = authState.name ?: "사용자"

    var showLogoutDialog by remember { mutableStateOf(false) }

    // 로그아웃 다이얼로그는 그대로 유지
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
                ) { Text("예") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("아니오") }
            }
        )
    }

    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = { HeaderContainer() },
        body = {
            MainManagementSection(
                userName = name,   // ⭐️ 여기서 name 전달
                onInstitutionManageClick = onInstitutionManageClick,
                onPaymentManageClick = onPaymentManageClick,
                onRegisterPalmprintClick = onRegisterPalmprintClick,
                onDeletePalmprintClick = onDeletePalmprintClick,
                onMyVerificationClick = onMyVerificationClick,
                onHowToUseClick = onHowToUseClick
            )
        },
        footer = {
            Footer {
                MainFooterButtonsSection(
                    onLogoutClick = { showLogoutDialog = true },
                    onSignOutClick = onSignOutClick
                )
            }
        }
    )
}
