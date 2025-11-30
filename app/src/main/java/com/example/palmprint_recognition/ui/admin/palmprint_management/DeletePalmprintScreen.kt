package com.example.palmprint_recognition.ui.admin.palmprint_management

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.admin.common.UiState

/**
 * DeletePalmprintScreen
 *
 * - 특정 palmprintId 삭제 여부를 묻는 확인 화면
 * - "예" → 삭제 API 호출 → onDeleteSuccess()
 * - "아니오" → onCancel()
 *
 * @param userId          해당 손바닥 정보의 user_id
 * @param palmprintId     삭제할 palmprint의 id
 * @param onDeleteSuccess 삭제 성공 후 호출 (PalmprintListScreen 이동)
 * @param onCancel        취소 클릭 시 이전 화면으로 이동
 */
@Composable
fun DeletePalmprintScreen(
    userId: Int,
    palmprintId: Int,
    onDeleteSuccess: () -> Unit,
    onCancel: () -> Unit,
    viewModel: DeletePalmprintViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    // 삭제 성공 시 화면 이동
    if (uiState is UiState.Success) {
        onDeleteSuccess()
    }

    DeletePalmprintContent(
        uiState = uiState,
        onConfirm = {
            viewModel.deletePalmprint(userId, palmprintId)
        },
        onCancel = onCancel
    )
}

/**
 * DeletePalmprintContent (UI 전담)
 */
@Composable
fun DeletePalmprintContent(
    uiState: UiState<*>,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "해당 손바닥 정보를\n삭제하시겠습니까?",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(40.dp))

        // API 로딩 시 Progress 표시
        if (uiState is UiState.Loading) {
            CircularProgressIndicator()
            Spacer(Modifier.height(20.dp))
        }

        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("예")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("아니오")
        }
    }
}

/**
 * Preview
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDeletePalmprintContent() {
    DeletePalmprintContent(
        uiState = UiState.Idle,
        onConfirm = {},
        onCancel = {}
    )
}
