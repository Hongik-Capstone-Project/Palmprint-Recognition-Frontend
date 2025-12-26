package com.example.palmprint_recognition.ui.user.features.palmprint_management.screens

import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.screens.ResultScreen
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.user.features.palmprint_management.components.AddSquareIcon
import com.example.palmprint_recognition.ui.user.features.palmprint_management.viewmodel.RegisterPalmprintViewModel
import java.io.ByteArrayOutputStream

@Composable
fun RegisterPalmprintScreen(
    // “등록 완료 팝업”의 버튼을 눌렀을 때 이동(메인으로)
    onGoMain: () -> Unit,
    // 카메라 화면으로 이동을 Navigation에서 처리하고 싶으면 이 콜백 사용
    // 여기서는 간단히 "PalmCameraScreen을 같은 NavGraph에 두고 이동"하는 방식도 가능
    viewModel: RegisterPalmprintViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    // 성공이면 공용 팝업으로 대체 렌더링
    if (uiState is UiState.Success) {
        ResultScreen(
            message = "손바닥 등록을 완료했어요!",
            buttonText = "메인으로 돌아가기",
            onButtonClick = {
                viewModel.clearState()
                onGoMain()
            }
        )
        return
    }

    RegisterPalmprintContent(
        uiState = uiState,
        onRegister = { base64 -> viewModel.registerPalmprint(base64) },
        onCaptured = { /* 필요 시 로깅/처리 */ },
        onGoMain = onGoMain
    )
}

@Composable
private fun RegisterPalmprintContent(
    uiState: UiState<*>,
    onRegister: (String) -> Unit,
    onCaptured: (Bitmap) -> Unit,
    onGoMain: () -> Unit
) {
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var localErrorMessage by remember { mutableStateOf<String?>(null) }

    // 카메라 화면을 “이 화면 내부에서” 바로 띄우는 단순 방식 (Nav 없이)
    var showCamera by remember { mutableStateOf(false) }

    val isLoading = uiState is UiState.Loading
    val serverErrorMessage = (uiState as? UiState.Error)?.message

    if (showCamera) {
        CameraScreen(
            onCaptured = { bmp ->
                capturedBitmap = bmp
                onCaptured(bmp)
                showCamera = false
            },
            onCancel = { showCamera = false }
        )
        return
    }

    val canSubmit = (capturedBitmap != null) && !isLoading

    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = { HeaderContainer() },
        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = "손바닥 등록하기")
                Text(
                    text = "손바닥 인식의 정확성을 위해\n조명이 밝은 환경에서 가이드라인에 맞추어 촬영해주세요.",
                    color = Color(0xFF697077) // CSS CoolGray/60
                )

                CaptureBox(
                    bitmap = capturedBitmap,
                    onClickCapture = {
                        localErrorMessage = null
                        showCamera = true
                    }
                )

                localErrorMessage?.let { Text(text = it, color = Color.Red) }
                serverErrorMessage?.let { Text(text = it, color = Color.Red) }

                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) { CircularProgressIndicator() }
                }
            }
        },
        footer = {
            Footer {
                SingleCenterButton(
                    text = "등록하기",
                    enabled = canSubmit,
                    onClick = {
                        localErrorMessage = null

                        val bmp = capturedBitmap
                        if (bmp == null) {
                            localErrorMessage = "손바닥 이미지를 먼저 촬영해주세요."
                            return@SingleCenterButton
                        }

                        val base64 = bitmapToBase64Jpeg(bmp)
                        if (base64.isBlank()) {
                            localErrorMessage = "이미지 처리 중 오류가 발생했습니다."
                            return@SingleCenterButton
                        }

                        onRegister(base64)
                    }
                )
            }
        }
    )
}


@Composable
private fun CaptureBox(
    bitmap: Bitmap?,
    onClickCapture: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(379.dp)
            .border(1.dp, Color(0xFF697077))
            .clickable { onClickCapture() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap == null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AddSquareIcon()
                Spacer(Modifier.height(14.dp))
                Text(text = "손바닥 촬영하기")
            }
        } else {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "captured palm",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun bitmapToBase64Jpeg(bitmap: Bitmap): String {
    val baos = ByteArrayOutputStream()
    val ok = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
    if (!ok) return ""
    val bytes = baos.toByteArray()
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}
