package com.example.palmprint_recognition.ui.user.features.user_main.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.user.features.user_main.components.MainFooterButtonsSection
import com.example.palmprint_recognition.ui.user.features.user_main.components.MainManagementSection
import com.example.palmprint_recognition.ui.user.features.user_main.viewmodel.UserMainViewModel
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun UserMainScreen(
    onInstitutionManageClick: () -> Unit,
    onPaymentManageClick: () -> Unit,
    onRegisterPalmprintClick: () -> Unit,
    onDeletePalmprintClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onHowToUseClick: () -> Unit,
    onLogoutClick: () -> Unit,     // 추가
    onSignOutClick: () -> Unit,
    authViewModel: AuthViewModel,  // (이건 name 표시 때문에 유지)
    viewModel: UserMainViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val name = authState.name ?: "사용자"
    val palmStatus by viewModel.palmStatus.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refreshPalmprintStatus()
    }

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
                userName = name,
                palmSubtitle = palmSubtitle,
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
                    onLogoutClick = onLogoutClick,   // 이제 navigate만
                    onSignOutClick = onSignOutClick
                )
            }
        }
    )
}
