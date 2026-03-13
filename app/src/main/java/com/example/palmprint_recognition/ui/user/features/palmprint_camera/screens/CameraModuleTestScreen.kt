package com.example.palmprint_recognition.ui.user.features.palmprint_camera.screens

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.palmprint_recognition.ui.common.button.SingleCenterButton
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCapturedResult
import com.example.palmprint_recognition.ui.user.features.palmprint_management.components.AddSquareIcon
import java.io.ByteArrayOutputStream

/**
 * 카메라 모듈 테스트 화면
 *
 * 목적
 * - 서버 요청 없이 CameraScreen 동작을 확인한다
 * - 촬영 결과 Bitmap 표시를 확인한다
 * - Base64 변환이 정상 동작하는지 로그로 확인한다
 * - blur / ratio / tilt 분석 결과를 화면에서 확인한다
 */
@Composable
fun CameraModuleTestScreen() {
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var capturedResult by remember { mutableStateOf<CameraCapturedResult?>(null) }
    var isCameraOpened by remember { mutableStateOf(false) }
    var debugMessage by remember { mutableStateOf("아직 촬영된 이미지가 없습니다.") }

    if (isCameraOpened) {
        CameraScreen(
            onCaptured = { result ->
                capturedBitmap = result.croppedBitmap
                capturedResult = result
                isCameraOpened = false

                debugMessage =
                    "촬영 성공: ${result.croppedBitmap.width} x ${result.croppedBitmap.height}"

                Log.d(
                    "CameraModuleTest",
                    "ratio=${result.analysisState.ratio}, " +
                            "blur=${result.analysisState.blurScore}, " +
                            "tilt=${result.analysisState.tiltScore}, " +
                            "condition=${result.analysisState.condition}"
                )
            },
            onCancel = {
                isCameraOpened = false
                debugMessage = "촬영이 취소되었습니다."
            }
        )
        return
    }

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
                Text(text = "카메라 모듈 테스트")
                Text(
                    text = "서버 없이 카메라 프리뷰, 가이드라인, crop 결과를 확인하는 화면입니다.",
                    color = Color(0xFF697077)
                )

                CameraTestPreviewBox(
                    bitmap = capturedBitmap,
                    onOpenCamera = {
                        isCameraOpened = true
                    }
                )

                Text(
                    text = debugMessage,
                    color = Color.DarkGray
                )

                capturedResult?.let { result ->
                    CameraConditionDebugSection(
                        result = result
                    )
                }
            }
        },
        footer = {
            Footer {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SingleCenterButton(
                        text = "카메라 열기",
                        onClick = {
                            isCameraOpened = true
                        }
                    )

                    SingleCenterButton(
                        text = "Base64 변환 테스트",
                        enabled = capturedBitmap != null,
                        onClick = {
                            val bitmap = capturedBitmap ?: return@SingleCenterButton
                            val base64 = bitmapToBase64Jpeg(bitmap)
                            debugMessage = "Base64 length = ${base64.length}"

                            Log.d(
                                "CameraModuleTest",
                                "Base64 length = ${base64.length}"
                            )
                        }
                    )
                }
            }
        }
    )
}

/**
 * 촬영 결과 미리보기 박스
 *
 * @param bitmap 촬영된 Bitmap
 * @param onOpenCamera 카메라 열기 콜백
 */
@Composable
private fun CameraTestPreviewBox(
    bitmap: Bitmap?,
    onOpenCamera: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "촬영 결과 확인")

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .border(1.dp, Color(0xFF697077))
                .clickable { onOpenCamera() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (bitmap == null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AddSquareIcon()
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = "카메라 테스트 시작")
                }
            } else {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "camera test result",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

/**
 * 촬영 후 분석 결과를 화면에 표시한다.
 *
 * @param result 카메라 촬영 결과
 */
@Composable
private fun CameraConditionDebugSection(
    result: CameraCapturedResult
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = "촬영 분석 결과")
        Text(text = "ratio: ${"%.2f".format(result.analysisState.ratio)}")
        Text(text = "blur: ${"%.2f".format(result.analysisState.blurScore)}")
        Text(text = "tilt: ${"%.4f".format(result.analysisState.tiltScore)}")
        Text(text = "ratioCondition: ${result.analysisState.ratioCondition}")
        Text(text = "blurCondition: ${result.analysisState.blurCondition}")
        Text(text = "tiltCondition: ${result.analysisState.tiltCondition}")
        Text(text = "finalCondition: ${result.analysisState.condition}")
        Text(text = "message: ${result.analysisState.message}")
    }
}

/**
 * Bitmap을 JPEG Base64 문자열로 변환한다
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

    if (!isSuccess) return ""

    val byteArray = outputStream.toByteArray()

    return Base64.encodeToString(
        byteArray,
        Base64.NO_WRAP
    )
}