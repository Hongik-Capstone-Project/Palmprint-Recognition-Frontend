package com.example.palmprint_recognition.ui.demo.features.register.screens

import android.graphics.Bitmap
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
import androidx.compose.ui.platform.LocalContext
import com.example.palmprint_recognition.ui.demo.utils.prepareDemoPalmprintUploadImage
import com.example.palmprint_recognition.ui.demo.utils.saveDemoPalmprintUploadDebugImage
import timber.log.Timber
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraMode
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.config.CameraAnalysisConfig

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

    val context = LocalContext.current

    val isLoading = uiState is UiState.Loading
    val serverErrorMessage = (uiState as? UiState.Error)?.message

    if (isCameraOpened) {
        CameraScreen(
            cameraMode = CameraMode.REGISTER,
            isAutoCaptureEnabled = CameraAnalysisConfig.isAutoCaptureEnabled,
            onCaptured = { result ->
                capturedResult = result
                isCameraOpened = false
                localMessage =
                    if (CameraAnalysisConfig.isAutoCaptureEnabled) {
                        "손바닥 이미지가 자동 촬영되었습니다."
                    } else {
                        "손바닥 이미지가 촬영되었습니다."
                    }
            },
            onCancel = {
                isCameraOpened = false
            }
        )
        return
    }

    val previewBitmap = capturedResult?.croppedBitmap
    val canSubmit = name.isNotBlank() && previewBitmap != null && !isLoading

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
                    text = "이름을 입력한 뒤 가이드라인에 맞추어 손바닥을 촬영해주세요.",
                    color = Color(0xFF697077)
                )

                Spacer(modifier = Modifier.height(2.dp))

                LabeledField(
                    label = "이름",
                    value = name,
                    onValueChange = { name = it }
                )

                Spacer(modifier = Modifier.height(2.dp))

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

                        val uploadImage = prepareDemoPalmprintUploadImage(
                            bitmap = bitmap
                        )

                        if (uploadImage == null || uploadImage.base64.isBlank()) {
                            localMessage = "이미지 처리 중 오류가 발생했습니다."
                            return@SingleCenterButton
                        }

                        saveDemoPalmprintUploadDebugImage(
                            context = context,
                            uploadImage = uploadImage,
                            prefix = "register_upload"
                        )

                        Timber.tag("DemoPalmRegister").d(
                            "name=%s width=%d height=%d bytes=%d base64Length=%d sha256=%s",
                            name.trim(),
                            uploadImage.bitmap.width,
                            uploadImage.bitmap.height,
                            uploadImage.jpegBytes.size,
                            uploadImage.base64.length,
                            uploadImage.sha256
                        )

                        onRegister(
                            name.trim(),
                            uploadImage.base64
                        )
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

