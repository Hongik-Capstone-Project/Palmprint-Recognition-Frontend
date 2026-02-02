package com.example.palmprint_recognition.ui.common.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.palmprint_recognition.ui.common.button.VerticalTwoButtons
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutWeighted
import com.example.palmprint_recognition.ui.core.state.UiState

@Composable
fun ConfirmYesNoScreen(
    message: String,
    uiState: UiState<Unit>,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier = Modifier,
    yesText: String = "예",
    noText: String = "아니오",
    retryText: String = "다시 시도",
    errorMessage: String = "오류가 발생했습니다."
) {
    RootLayoutWeighted(
        headerWeight = 2f,
        bodyWeight = 4f,
        footerWeight = 6f,

        header = {
            // 공용: 헤더 없음(구조 유지)
        },

        body = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp
                )
            }
        },

        footer = {
            Footer {
                ConfirmYesNoActionSection(
                    uiState = uiState,
                    onYesClick = onYesClick,
                    onNoClick = onNoClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 24.dp),
                    yesText = yesText,
                    noText = noText,
                    retryText = retryText,
                    errorMessage = errorMessage
                )
            }
        }
    )
}

@Composable
private fun ConfirmYesNoActionSection(
    uiState: UiState<Unit>,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    modifier: Modifier,
    yesText: String,
    noText: String,
    retryText: String,
    errorMessage: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when (uiState) {
            UiState.Idle -> {
                VerticalTwoButtons(
                    firstText = yesText,
                    secondText = noText,
                    onFirstClick = onYesClick,
                    onSecondClick = onNoClick
                )
            }

            UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Error -> {
                Text(text = uiState.message ?: errorMessage)
                VerticalTwoButtons(
                    firstText = retryText,
                    secondText = noText,
                    onFirstClick = onYesClick,
                    onSecondClick = onNoClick
                )
            }

            is UiState.Success -> {
                // 전환/분기는 각 Screen에서 처리 (공용 컴포넌트는 비움)
            }
        }
    }
}
