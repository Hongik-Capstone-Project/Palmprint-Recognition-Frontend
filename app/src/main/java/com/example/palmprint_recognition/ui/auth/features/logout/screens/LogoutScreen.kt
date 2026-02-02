package com.example.palmprint_recognition.ui.auth.features.logout.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.auth.features.logout.viewmodel.LogoutViewModel
import com.example.palmprint_recognition.ui.common.button.VerticalTwoButtons
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.common.screens.ConfirmYesNoScreen

/**
 * 로그아웃 확인 Screen
 *
 * - "예" → 로컬 로그아웃 → 성공 시 AuthViewModel.refreshAuthState() → AppNavHost가 Login으로 분기
 * - "아니오" → 이전 화면으로 복귀(onCancel)
 */
@Composable
fun LogoutScreen(
    onCancel: () -> Unit,
    authViewModel: AuthViewModel,
    viewModel: LogoutViewModel = hiltViewModel()
) {
    val logoutState by viewModel.logoutState.collectAsStateWithLifecycle()

    LaunchedEffect(logoutState) {
        if (logoutState is UiState.Success) {
            // 전역 상태 갱신(필수) → AppNavHost가 즉시 Auth 그래프로 교체
            authViewModel.refreshAuthState()
            viewModel.clearState()
        }
    }

    ConfirmYesNoScreen(
        message = "정말 로그아웃\n하시겠습니까?",
        uiState = logoutState,
        onYesClick = { viewModel.logoutLocal() },
        onNoClick = onCancel,
        errorMessage = "로그아웃 중 오류가 발생했습니다."
    )
}
