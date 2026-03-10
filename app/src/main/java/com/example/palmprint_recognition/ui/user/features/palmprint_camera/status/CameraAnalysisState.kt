package com.example.palmprint_recognition.ui.user.features.palmprint_camera.status

/**
 * 촬영 이미지 분석 결과 상태
 *
 * @property ratio 원본 대비 가이드 crop 영역 비율
 * @property blurScore 선명도 점수
 * @property condition 최종 촬영 가능 상태
 */
data class CameraAnalysisState(
    val ratio: Float,
    val blurScore: Float,
    val condition: CameraCaptureCondition
)