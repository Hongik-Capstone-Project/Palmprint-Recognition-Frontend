package com.example.palmprint_recognition.ui.user.features.palmprint_camera.status

/**
 * 카메라 프리뷰 실시간 분석 상태
 *
 * 역할
 * - ImageAnalysis가 분석한 최신 blur 상태를 저장한다
 * - landmark 조건은 CameraScreen에서 별도 계산한다
 *
 * @property isAnalyzing 현재 프레임 분석이 진행 중인지 여부
 * @property blurScore 최근 프레임의 blur score
 * @property blurCondition blur 관련 상태
 */
data class CameraRealtimeState(
    val isAnalyzing: Boolean = false,
    val blurScore: Float? = null,
    val blurCondition: CameraCaptureCondition = CameraCaptureCondition.READY
)