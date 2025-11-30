package com.example.palmprint_recognition.ui.admin.palmprint_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.model.PalmprintUploadResponse
import com.example.palmprint_recognition.data.repository.AdminRepository
import com.example.palmprint_recognition.ui.admin.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * UploadPalmprintViewModel
 *
 * - 카메라로 촬영된 이미지 파일을 받아 API(uploadPalmprint) 호출.
 * - 업로드 성공 → PalmprintListScreen 이동 트리거 제공.
 */
@HiltViewModel
class UploadPalmprintViewModel @Inject constructor(
    private val repository: AdminRepository
) : ViewModel() {

    private val _uploadState = MutableStateFlow<UiState<PalmprintUploadResponse>>(UiState.Idle)
    val uploadState = _uploadState.asStateFlow()

    /**
     * 손바닥 이미지 업로드
     */
    fun uploadPalmprint(imageFile: File) {
        viewModelScope.launch {
            _uploadState.value = UiState.Loading

            try {
                val result = repository.uploadPalmprint(imageFile)
                _uploadState.value = UiState.Success(result)

            } catch (e: Exception) {
                _uploadState.value = UiState.Error(
                    e.message ?: "손바닥 업로드 중 오류가 발생했습니다."
                )
            }
        }
    }
}
