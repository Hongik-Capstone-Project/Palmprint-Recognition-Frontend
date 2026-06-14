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
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private const val KEY_PALM_CHANGED = "palm_changed"


@Composable
fun UserMainScreen(
    navController: NavController,
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

    // 1) 최초 진입 시 1회만 (ViewModel이 hasLoadedOnce로 막음)
    LaunchedEffect(Unit) {
        viewModel.refreshPalmCount(force = false)
    }

    // 2) 등록/삭제 후에만 refresh 하도록 플래그 감지
    val palmChangedFlow = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow(KEY_PALM_CHANGED, false)

    val palmChanged by (palmChangedFlow ?: kotlinx.coroutines.flow.flowOf(false))
        .collectAsStateWithLifecycle(initialValue = false)

    LaunchedEffect(palmChanged) {
        if (palmChanged) {
            // 플래그 소비(다음에 또 안 뜨게)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set(KEY_PALM_CHANGED, false)

            // 등록/삭제 후에만 강제 GET
            viewModel.refreshPalmCount(force = true)
        }
    }


    val palmSubtitle = "손바닥 정보를 등록하고 인증을 진행하세요"

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
                onMyVerificationClick = onHistoryClick
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
