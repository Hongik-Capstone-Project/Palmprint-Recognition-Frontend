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
import com.example.palmprint_recognition.ui.common.layout.RootLayout
import com.example.palmprint_recognition.ui.core.state.UiState

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
            // ✅ 전역 상태 갱신(필수) → AppNavHost가 즉시 Auth 그래프로 교체
            authViewModel.refreshAuthState()
            viewModel.clearState()
        }
    }

    LogoutContent(
        uiState = logoutState,
        onYesClick = { viewModel.logoutLocal() },
        onNoClick = onCancel
    )
}

/**
 * UI Only (Preview 가능)
 */
@Composable
internal fun LogoutContent(
    uiState: UiState<Unit>,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RootLayout(
        headerWeight = 2f,
        bodyWeight = 4f,
        footerWeight = 6f,
        sectionGapWeight = 0.4f,

        header = {
            // 헤더 없음 (구조 유지용)
        },

        body = {
            LogoutMessageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        },

        footer = {
            Footer {
                LogoutActionSection(
                    uiState = uiState,
                    onYesClick = onYesClick,
                    onNoClick = onNoClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 24.dp)
                )
            }
        }
    )
}

@Composable
private fun LogoutMessageSection(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "정말 로그아웃\n하시겠습니까?",
            textAlign = TextAlign.Center,
            fontSize = 26.sp
        )
    }
}

@Composable
private fun LogoutActionSection(
    uiState: UiState<Unit>,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when (uiState) {
            UiState.Idle -> {
                VerticalTwoButtons(
                    firstText = "예",
                    secondText = "아니오",
                    onFirstClick = onYesClick,
                    onSecondClick = onNoClick
                )
            }

            UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Error -> {
                Text(text = uiState.message ?: "로그아웃 중 오류가 발생했습니다.")
                VerticalTwoButtons(
                    firstText = "다시 시도",
                    secondText = "아니오",
                    onFirstClick = onYesClick,
                    onSecondClick = onNoClick
                )
            }

            is UiState.Success -> {
                // 화면 전환/분기는 Screen의 LaunchedEffect에서 처리
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewLogoutContent() {
    LogoutContent(
        uiState = UiState.Idle,
        onYesClick = {},
        onNoClick = {}
    )
}
