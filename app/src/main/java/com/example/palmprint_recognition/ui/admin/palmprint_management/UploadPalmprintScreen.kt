package com.example.palmprint_recognition.ui.admin.palmprint_management

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.palmprint_recognition.ui.admin.common.UiState
import java.io.File

/**
 * UploadPalmprintScreen
 *
 * - 카메라 앱 실행 → 손바닥 촬영
 * - 촬영된 이미지를 File 형태로 ViewModel로 전달해 업로드
 * - 업로드 성공 → PalmprintListScreen 이동(onUploadSuccess)
 *
 * @param userId Palmprint 를 등록할 사용자 ID
 * @param onUploadSuccess 업로드 성공 시 Navigation 에게 알려주기 위한 콜백
 * @param onCancel 뒤로가기(촬영 취소) 콜백
 */
@Composable
fun UploadPalmprintScreen(
    userId: Int,
    onUploadSuccess: () -> Unit,
    onCancel: () -> Unit = {},
    viewModel: UploadPalmprintViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uploadState.collectAsStateWithLifecycle()

    // 촬영한 사진을 임시 저장할 File
    var imageFile by remember { mutableStateOf<File?>(null) }

    /**
     * 카메라 앱 실행 후 결과 받는 Launcher
     */
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            // 촬영 성공 → 파일 존재 시 업로드
            imageFile?.let { file ->
                viewModel.uploadPalmprint(file)
            }
        }
    }

    /**
     * 카메라 실행 함수
     */
    fun openCamera() {
        // 임시 파일 생성
        val photoFile = File.createTempFile("palmprint_", ".jpg", context.externalCacheDir)
        imageFile = photoFile

        // FileProvider 로 URI 생성
        val photoUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )

        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri)

        cameraLauncher.launch(intent)
    }

    /**
     * UI Content 호출
     */
    UploadPalmprintContent(
        uiState = uiState,
        onCaptureClick = { openCamera() },
        onCancel = onCancel
    )

    /**
     * 업로드 성공 → Navigation 콜백 실행
     */
    if (uiState is UiState.Success) {
        onUploadSuccess()
    }
}

/**
 * UploadPalmprintContent (UI 전담)
 *
 * - Preview 가능
 * - 카메라 촬영 버튼 UI만 담당
 */
@Composable
fun UploadPalmprintContent(
    uiState: UiState<*>,
    onCaptureClick: () -> Unit,
    onCancel: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCCCCCC)) // 회색 배경(카메라 화면 대체)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        // 로딩 상태 표시
        when (uiState) {
            UiState.Loading -> {
                CircularProgressIndicator()
                Spacer(Modifier.height(20.dp))
            }

            is UiState.Error -> {
                Text(
                    text = uiState.message ?: "오류가 발생했습니다.",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(20.dp))
            }

            else -> {}
        }

        // 촬영 버튼
        Button(
            onClick = onCaptureClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
        ) {
            Text("촬영")
        }

        // 취소 버튼 (선택사항)
        /*
        Button(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("취소")
        }
        */
    }
}

/**
 * Preview
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewUploadPalmprintContent() {
    UploadPalmprintContent(
        uiState = UiState.Idle,
        onCaptureClick = {},
        onCancel = {}
    )
}
