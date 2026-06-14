package com.example.palmprint_recognition.ui.demo.features.verify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmprint_recognition.data.repository.DemoRepository
import com.example.palmprint_recognition.ui.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import timber.log.Timber
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.config.CameraAnalysisConfig

data class DemoVerifySuccessUi(
    val message: String,
    val matched: Boolean,
    val name: String? = null,
    val similarityScore: Double? = null
)

@HiltViewModel
class DemoVerifyViewModel @Inject constructor(
    private val demoRepository: DemoRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<DemoVerifySuccessUi>>(UiState.Idle)
    val state = _state.asStateFlow()

    private var inFlight = false

    fun verifyDemoPalmprint(base64: String) {
        if (inFlight) return

        viewModelScope.launch {
            inFlight = true
            _state.value = UiState.Loading

            /**
             * 전시/발표용 데모 모드
             * 서버가 닫혀 있으므로 실제 API 호출은 하지 않고,
             * 항상 인증 성공 결과를 표시합니다.
             */

            // 실제 서버 호출 부분 주석 처리
            /*
            runCatching {
                val response = demoRepository.verifyDemoPalmprint(
                    palmprintData = base64
                )

                val successMessage = if (response.matched) {
                    buildString {
                        append("인증 성공")

                        response.name?.let { name ->
                            append("\n이름: $name")
                        }

                        if (CameraAnalysisConfig.isSimilarityScoreVisible) {
                            response.similarityScore?.let { score ->
                                append("\n유사도: ${"%.4f".format(score)}")
                            }
                        }
                    }
                } else {
                    buildString {
                        append("일치하는 손바닥 정보를 찾지 못했습니다.")

                        if (CameraAnalysisConfig.isSimilarityScoreVisible) {
                            response.similarityScore?.let { score ->
                                append("\n가장 높은 유사도: ${"%.4f".format(score)}")
                            } ?: append("\n유사도 정보가 없습니다.")
                        }
                    }
                }

                Timber.tag("DemoPalmVerify").d(
                    "matched=%s name=%s similarityScore=%s",
                    response.matched,
                    response.name,
                    response.similarityScore
                )

                DemoVerifySuccessUi(
                    message = successMessage,
                    matched = response.matched,
                    name = response.name,
                    similarityScore = response.similarityScore
                )
            }.onSuccess { ui ->
                _state.value = UiState.Success(ui)
            }.onFailure { e ->
                _state.value = UiState.Error(e.message ?: "손바닥 인증 중 오류가 발생했습니다.")
            }
            */

            // 발표용 성공 결과
            val demoName = "홍길동"
            val demoSimilarityScore = 0.9876

            val successMessage = buildString {
                append("인증 성공")
                append("\n이름: $demoName")

                if (CameraAnalysisConfig.isSimilarityScoreVisible) {
                    append("\n유사도: ${"%.4f".format(demoSimilarityScore)}")
                }
            }

            Timber.tag("DemoPalmVerify").d(
                "DEMO MODE - matched=%s name=%s similarityScore=%s",
                true,
                demoName,
                demoSimilarityScore
            )

            _state.value = UiState.Success(
                DemoVerifySuccessUi(
                    message = successMessage,
                    matched = true,
                    name = demoName,
                    similarityScore = demoSimilarityScore
                )
            )

            inFlight = false
        }
    }

    fun clearState() {
        inFlight = false
        _state.value = UiState.Idle
    }
}