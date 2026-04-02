package com.example.palmprint_recognition.ui.demo.features.register.screens

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
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
import com.example.palmprint_recognition.ui.common.field.LabeledField
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.common.screens.ResultScreen
import com.example.palmprint_recognition.ui.core.state.UiState
import com.example.palmprint_recognition.ui.demo.features.register.viewmodel.DemoRegisterViewModel
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.screens.CameraScreen
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCapturedResult
import com.example.palmprint_recognition.ui.user.features.palmprint_management.components.AddSquareIcon
import java.io.ByteArrayOutputStream

/**
 * 데모 손바닥 등록 화면
 *
 * 기능
 * - 이름 입력
 * - 카메라 촬영
 * - crop된 이미지를 Base64로 변환
 * - 데모 등록 API 호출
 * - 성공 시 결과 화면 표시
 */
@Composable
fun DemoRegisterScreen(
    onGoHome: () -> Unit,
    onBack: () -> Unit = {},
    viewModel: DemoRegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    if (uiState is UiState.Success) {
        val successState = (uiState as UiState.Success).data

        ResultScreen(
            message = successState.message,
            buttonText = "처음으로 돌아가기",
            onButtonClick = {
                viewModel.clearState()
                onGoHome()
            }
        )
        return
    }

    DemoRegisterContent(
        uiState = uiState,
        onRegister = viewModel::registerDemoPalmprint,
        onBack = onBack
    )
}

/**
 * 데모 손바닥 등록 화면 본문
 */
@Composable
private fun DemoRegisterContent(
    uiState: UiState<*>,
    onRegister: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var capturedResult by remember { mutableStateOf<CameraCapturedResult?>(null) }
    var isCameraOpened by remember { mutableStateOf(false) }
    var localMessage by remember { mutableStateOf<String?>(null) }

    val isLoading = uiState is UiState.Loading
    val serverErrorMessage = (uiState as? UiState.Error)?.message

    if (isCameraOpened) {
        CameraScreen(
            onCaptured = { result ->
                capturedResult = result
                isCameraOpened = false
                localMessage = buildCaptureSummaryMessage(result)
            },
            onCancel = {
                isCameraOpened = false
                if (capturedResult == null) {
                    localMessage = "촬영이 취소되었습니다."
                }
            }
        )
        return
    }

    val previewBitmap = capturedResult?.croppedBitmap
    val canSubmit = name.isNotBlank() && previewBitmap != null && !isLoading

    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                HeaderContainer()

                Spacer(modifier = Modifier.height(12.dp))

                LabeledField(
                    label = "이름",
                    value = name,
                    onValueChange = { name = it }
                )
            }
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
                    text = "전시용 데모 등록 화면입니다.\n이름을 입력한 뒤 가이드라인에 맞추어 손바닥을 촬영해주세요.",
                    color = Color(0xFF697077)
                )

                DemoRegisterCaptureBox(
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
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
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

                        if (name.isBlank()) {
                            localMessage = "이름을 입력해주세요."
                            return@SingleCenterButton
                        }

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

                        Log.d("DemoPalmRegister", "name=$name, Base64 length=${base64.length}")
                        onRegister(name.trim(), base64)
                    }
                )
            }
        }
    )
}

/**
 * 촬영 결과 미리보기 박스
 */
@Composable
private fun DemoRegisterCaptureBox(
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
 * 촬영 결과 요약 메시지
 */
private fun buildCaptureSummaryMessage(
    result: CameraCapturedResult
): String {
    return "촬영 성공: ${result.croppedBitmap.width} x ${result.croppedBitmap.height}, " +
            "ratio=${"%.1f".format(result.analysisState.ratio)}, " +
            "blur=${"%.1f".format(result.analysisState.blurScore)}, " +
            "tilt=${"%.3f".format(result.analysisState.tiltScore)}"
}

/**
 * Bitmap을 비율 유지하며 축소
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
 * Bitmap을 JPEG Base64 문자열로 변환
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