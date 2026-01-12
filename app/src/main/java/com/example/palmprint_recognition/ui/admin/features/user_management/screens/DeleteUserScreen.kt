package com.example.palmprint_recognition.ui.admin.features.user_management.screens

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
import com.example.palmprint_recognition.ui.admin.features.user_management.viewmodel.DeleteUserViewModel
import com.example.palmprint_recognition.ui.common.button.VerticalTwoButtons
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.common.screens.ConfirmYesNoScreen


/**
 * 유저 삭제 Screen
 *
 * - "예" → deleteUser() 호출 → 성공 시 USER_LIST로 이동(onConfirmDelete)
 * - "아니오" → USER_DETAIL로 복귀(onCancel)
 */
@Composable
fun DeleteUserScreen(
    userId: Int,
    onConfirmDelete: () -> Unit,
    onCancel: () -> Unit,
    viewModel: DeleteUserViewModel = hiltViewModel()
) {
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()
    val isSuccess = deleteState is UiState.Success

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onConfirmDelete()
            viewModel.clearState()
        }
    }


    ConfirmYesNoScreen(
        message = "해당 유저 정보를\n삭제하시겠습니까?",
        uiState = deleteState,
        onYesClick = { viewModel.deleteUser(userId) },
        onNoClick = onCancel,
        errorMessage = "삭제 중 오류가 발생했습니다."
    )
}
