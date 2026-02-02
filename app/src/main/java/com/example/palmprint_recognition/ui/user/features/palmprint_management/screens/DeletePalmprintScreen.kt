package com.example.palmprint_recognition.ui.user.features.palmprint_management.screens

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
import com.example.palmprint_recognition.ui.common.button.VerticalTwoButtons
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.common.screens.ResultScreen
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.user.features.palmprint_management.viewmodel.DeletePalmprintViewModel
import com.example.palmprint_recognition.ui.common.screens.ConfirmYesNoScreen

/**
 * 손바닥 정보 삭제 Screen
 *
 * - "예" → deletePalmprint() 호출 → 성공 시 CommonResultScreen 표시
 * - "아니오" → 이전 화면 복귀(onCancel)
 * - 성공 팝업의 "메인으로 돌아가기" → onGoMain
 */
@Composable
fun DeletePalmprintScreen(
    onGoMain: () -> Unit,
    onCancel: () -> Unit,
    viewModel: DeletePalmprintViewModel = hiltViewModel()
) {
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    // 성공이면 공용 팝업 화면으로 전환
    if (deleteState is UiState.Success) {
        ResultScreen(
            message = "손바닥 정보가 삭제되었습니다.",
            buttonText = "메인으로 돌아가기",
            onButtonClick = {
                viewModel.clearState()
                onGoMain()
            }
        )
        return
    }

    ConfirmYesNoScreen(
        message = "등록된 손바닥 정보를\n삭제하시겠습니까?",
        uiState = deleteState,
        onYesClick = { viewModel.deletePalmprint() },
        onNoClick = onCancel,
        errorMessage = "손바닥 정보 삭제 중 오류가 발생했습니다."
    )
}
