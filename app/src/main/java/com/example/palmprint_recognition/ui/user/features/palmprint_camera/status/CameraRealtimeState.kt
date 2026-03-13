package com.example.palmprint_recognition.ui.user.features.palmprint_camera.status

/**
 * 카메라 프리뷰 실시간 분석 상태
 *
 * 역할
 * - ImageAnalysis가 분석한 최신 상태를 저장한다
 * - CameraScreen에서 상태 문구와 디버그 정보를 표시할 때 사용한다
 * - blur / ratio / tilt 개별 결과와 최종 상태를 함께 보관한다
 *
 * @property isAnalyzing 현재 프레임 분석이 진행 중인지 여부
 * @property blurScore 최근 프레임의 blur score
 * @property ratioEstimate 최근 프레임의 ratio 추정값
 * @property tiltScore 최근 프레임의 tilt score
 * @property ratioCondition 거리 관련 상태
 * @property blurCondition blur 관련 상태
 * @property tiltCondition tilt 관련 상태
 * @property condition 현재 촬영 가능 상태
 * @property message 사용자에게 보여줄 안내 문구
 */
data class CameraRealtimeState(
    val isAnalyzing: Boolean = false,
    val blurScore: Float? = null,
    val ratioEstimate: Float? = null,
    val tiltScore: Float? = null,
    val ratioCondition: CameraCaptureCondition = CameraCaptureCondition.TOO_FAR,
    val blurCondition: CameraCaptureCondition = CameraCaptureCondition.TOO_BLURRY,
    val tiltCondition: CameraCaptureCondition = CameraCaptureCondition.READY,
    val condition: CameraCaptureCondition = CameraCaptureCondition.TOO_FAR,
    val message: String = "손바닥을 가이드에 맞춰주세요."
)