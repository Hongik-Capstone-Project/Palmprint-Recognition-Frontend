package com.example.palmprint_recognition.ui.user.features.user_main.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.user.features.user_main.components.MainFooterButtonsSection
import com.example.palmprint_recognition.ui.user.features.user_main.components.MainManagementSection
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.palmprint_recognition.ui.user.features.user_main.viewmodel.UserMainViewModel

@Composable
fun UserMainScreen(
    onInstitutionManageClick: () -> Unit,
    onPaymentManageClick: () -> Unit,
    onRegisterPalmprintClick: () -> Unit,
    onDeletePalmprintClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onHowToUseClick: () -> Unit,
    onSignOutClick: () -> Unit,
    authViewModel: AuthViewModel,
    viewModel: UserMainViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    val name = authState.name ?: "사용자"

    val palmStatus by viewModel.palmStatus.collectAsStateWithLifecycle()

    // 메인 화면 진입 시 손바닥 상태 조회
    LaunchedEffect(Unit) {
        viewModel.refreshPalmprintStatus()
    }

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


    //  등록 여부에 따른 subtitle 결정
    val palmSubtitle = when (palmStatus.isRegistered) {
        true -> "현재 손바닥이 정상적으로 등록되어있어요."
        false -> "등록된 손바닥 정보가 없습니다."
        null -> "손바닥 등록 상태를 불러오는 중입니다."
    }


    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = { HeaderContainer() },
        body = {
            MainManagementSection(
                userName = name,   // 여기서 name 전달
                palmSubtitle = palmSubtitle, // 추가
                onInstitutionManageClick = onInstitutionManageClick,
                onPaymentManageClick = onPaymentManageClick,
                onRegisterPalmprintClick = onRegisterPalmprintClick,
                onDeletePalmprintClick = onDeletePalmprintClick,
                onMyVerificationClick = onHistoryClick,
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
