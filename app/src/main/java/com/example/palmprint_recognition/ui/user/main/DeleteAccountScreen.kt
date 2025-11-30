package com.example.palmprint_recognition.ui.user.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.admin.common.UiState
import com.example.palmprint_recognition.ui.auth.AuthViewModel

/**
 * DeleteAccountScreen
 *
 * - “정말 회원탈퇴하시겠습니까?” 확인
 * - 탈퇴 성공 → onDeleteSuccess()
 */
@Composable
fun DeleteAccountScreen(
    onDeleteSuccess: () -> Unit,
    onCancel: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val deleteState by viewModel.deleteAccountState.collectAsStateWithLifecycle()

    when (deleteState) {

        UiState.Idle, UiState.Loading -> {
            DeleteAccountContent(
                isLoading = deleteState is UiState.Loading,
                onConfirm = { viewModel.onDeleteAccountClick() },
                onCancel = onCancel
            )
        }

        is UiState.Error -> {
            val msg = (deleteState as UiState.Error).message ?: "회원탈퇴 실패"
            Text(msg, color = MaterialTheme.colorScheme.error)
        }

        is UiState.Success -> {
            LaunchedEffect(Unit) {
                viewModel.resetDeleteAccountState()
                onDeleteSuccess()
            }
        }
    }
}

@Composable
fun DeleteAccountContent(
    isLoading: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text("정말로 회원탈퇴 하시겠습니까?")

            Spacer(Modifier.height(20.dp))

            if (isLoading) {
                CircularProgressIndicator()
                Spacer(Modifier.height(20.dp))
            }

            Row {
                Button(onClick = onConfirm, modifier = Modifier.weight(1f)) {
                    Text("예")
                }
                Spacer(Modifier.width(12.dp))
                Button(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("아니오")
                }
            }
        }
    }
}
