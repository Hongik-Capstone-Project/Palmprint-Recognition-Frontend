package com.example.palmprint_recognition.ui.user.features.sign_out.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.palmprint_recognition.ui.common.button.VerticalTwoButtons
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.common.screens.ResultScreen
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.user.features.sign_out.viewmodel.SignOutViewModel
import com.example.palmprint_recognition.ui.common.screens.ConfirmYesNoScreen


/**
 * 회원탈퇴 확인 Screen
 *
 * - 성공 시 ResultScreen을 보여주고,
 *   버튼 누르면 전역 authState 갱신 → AppNavHost가 Auth 그래프로 자동 분기
 */
@Composable
fun SignOutScreen(
    onCancel: () -> Unit,
    authViewModel: AuthViewModel,
    viewModel: SignOutViewModel = hiltViewModel()
) {
    val signOutState by viewModel.signOutState.collectAsStateWithLifecycle()

    // 성공이면 "결과 화면"을 먼저 보여준다 (LaunchedEffect 안에서 UI 호출 X)
    if (signOutState is UiState.Success) {
        ResultScreen(
            message = "회원 탈퇴가 완료되었습니다.",
            buttonText = "처음 화면으로 돌아가기",
            onButtonClick = {
                // 1) 상태 초기화(다음에 다시 들어왔을 때 Success가 남아있지 않게)
                viewModel.clearState()

                // 2) 전역 상태 갱신 → AppNavHost가 로그인 화면(Auth 그래프)로 분기
                // logoutLocal()은 clearAllAuth + refreshAuthState라 가장 안전
                authViewModel.logoutLocal()
                // 또는 authViewModel.refreshAuthState() 만 해도 되지만,
                // 혹시를 대비해 logoutLocal() 추천
            }
        )
        return
    }

    ConfirmYesNoScreen(
        message = "정말 탈퇴하시겠습니까?",
        uiState = signOutState,
        onYesClick = { viewModel.deleteAccount() },
        onNoClick = onCancel,
        errorMessage = "회원탈퇴 중 오류가 발생했습니다."
    )
}
