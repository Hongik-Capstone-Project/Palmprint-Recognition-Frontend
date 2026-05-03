package com.example.palmprint_recognition.ui.user.features.palmprint_management.screens

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.common.screens.ResultScreen
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCapturedResult
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.screens.CameraScreen
import com.example.palmprint_recognition.ui.user.features.palmprint_management.components.AddSquareIcon
import com.example.palmprint_recognition.ui.user.features.palmprint_management.viewmodel.RegisterPalmprintViewModel
import java.io.ByteArrayOutputStream

/**
 * 손바닥 등록 화면
 *
 * 기능
 * - 커스텀 카메라 화면 진입
 * - 촬영 결과 미리보기 표시
 * - crop된 이미지를 Base64로 변환하여 등록 요청
 * - 등록 성공 시 결과 화면 표시
 *
 * @param onGoMain 메인 화면 이동 콜백
 * @param viewModel 손바닥 등록 ViewModel
 */
@Composable
fun RegisterPalmprintScreen(
    onGoMain: () -> Unit,
    viewModel: RegisterPalmprintViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    if (uiState is UiState.Success) {
        val successState = (uiState as UiState.Success).data

        ResultScreen(
            message = successState.message,
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
        onRegister = viewModel::registerPalmprint
    )
}

/**
 * 손바닥 등록 화면 본문
 *
 * 역할
 * - 카메라 표시 여부 제어
 * - 촬영 결과 보관
 * - 등록 버튼 클릭 처리
 *
 * @param uiState 등록 요청 상태
 * @param onRegister Base64 등록 요청 콜백
 */
@Composable
private fun RegisterPalmprintContent(
    uiState: UiState<*>,
    onRegister: (String) -> Unit
) {
    var capturedResult by remember { mutableStateOf<CameraCapturedResult?>(null) }
    var localMessage by remember { mutableStateOf<String?>(null) }
    var isCameraOpened by remember { mutableStateOf(false) }

    val isLoading = uiState is UiState.Loading
    val serverErrorMessage = (uiState as? UiState.Error)?.message

    if (isCameraOpened) {
        CameraScreen(
            onCaptured = { result ->
                capturedResult = result
                isCameraOpened = false
                localMessage = "이미지 촬영에 성공했습니다"
            },
            onCancel = {
                isCameraOpened = false
            }
        )
        return
    }

    val previewBitmap = capturedResult?.croppedBitmap
    val canSubmit = previewBitmap != null && !isLoading

    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = {
            HeaderContainer()
        },
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
                    color = Color(0xFF697077)
                )

                RegisterPalmprintCaptureBox(
                    bitmap = previewBitmap,
                    onClickCapture = {
                        localMessage = null
                        isCameraOpened = true
                    }
                )

                localMessage?.let { message ->
                    Text(
                        text = message,
                        color = Color.DarkGray
                    )
                }

                serverErrorMessage?.let { message ->
                    Text(
                        text = message,
                        color = Color.Red
                    )
                }

                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        },
        footer = {
            Footer {
                SingleCenterButton(
                    text = "등록하기",
                    enabled = canSubmit,
                    onClick = {
                        localMessage = null

                        val bitmap = capturedResult?.croppedBitmap
                        if (bitmap == null) {
                            localMessage = "손바닥 이미지를 먼저 촬영해주세요."
                            return@SingleCenterButton
                        }

                        val resizedBitmap = resizeBitmapKeepingRatio(
                            bitmap = bitmap,
                            maxWidth = 720
                        )

                        val base64 = bitmapToBase64Jpeg(resizedBitmap)
                        if (base64.isBlank()) {
                            localMessage = "이미지 처리 중 오류가 발생했습니다."
                            return@SingleCenterButton
                        }

                        Log.d("PalmRegister", "Base64 length=${base64.length}")
                        onRegister(base64)
                    }
                )
            }
        }
    )
}

/**
 * 손바닥 촬영 결과 미리보기 박스
 *
 * @param bitmap 표시할 Bitmap
 * @param onClickCapture 카메라 열기 콜백
 */
@Composable
private fun RegisterPalmprintCaptureBox(
    bitmap: Bitmap?,
    onClickCapture: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(379.dp)
            .border(1.dp, Color(0xFF697077))
            .clickable { onClickCapture() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AddSquareIcon()
                Spacer(modifier = Modifier.height(14.dp))
                Text(text = "손바닥 촬영하기")
            }
        } else {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "captured palmprint",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


/**
 * Bitmap을 비율 유지하며 축소한다.
 *
 * @param bitmap 원본 Bitmap
 * @param maxWidth 최대 너비
 * @return 축소된 Bitmap
 */
private fun resizeBitmapKeepingRatio(
    bitmap: Bitmap,
    maxWidth: Int
): Bitmap {
    if (bitmap.width <= maxWidth) {
        return bitmap
    }

    val ratio = maxWidth.toFloat() / bitmap.width.toFloat()
    val targetHeight = (bitmap.height * ratio).toInt().coerceAtLeast(1)

    return Bitmap.createScaledBitmap(
        bitmap,
        maxWidth,
        targetHeight,
        true
    )
}

/**
 * Bitmap을 JPEG Base64 문자열로 변환한다.
 *
 * @param bitmap 변환할 Bitmap
 * @return Base64 문자열
 */
private fun bitmapToBase64Jpeg(
    bitmap: Bitmap
): String {
    val outputStream = ByteArrayOutputStream()

    val isSuccess = bitmap.compress(
        Bitmap.CompressFormat.JPEG,
        90,
        outputStream
    )

    if (!isSuccess) {
        return ""
    }

    val byteArray = outputStream.toByteArray()

    return Base64.encodeToString(
        byteArray,
        Base64.NO_WRAP
    )
}