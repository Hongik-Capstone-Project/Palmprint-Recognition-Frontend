package com.example.palmprint_recognition.ui.user.features.palmprint_camera.status

/**
 * 촬영 이미지 분석 결과 상태
 *
 * 역할
 * - 촬영 후 분석된 개별 지표와 최종 촬영 가능 상태를 함께 보관한다
 * - CameraScreen 및 추후 디버그 화면에서 재사용할 수 있다
 *
 * @property ratio 원본 대비 가이드 crop 영역 비율
 * @property blurScore 선명도 점수
 * @property tiltScore 기울기 점수
 * @property ratioCondition 거리 관련 상태
 * @property blurCondition 선명도 관련 상태
 * @property tiltCondition 기울기 관련 상태
 * @property condition 최종 촬영 가능 상태
 * @property message 사용자에게 보여줄 안내 문구
 */
data class CameraAnalysisState(
    val ratio: Float,
    val blurScore: Float,
    val tiltScore: Float,
    val ratioCondition: CameraCaptureCondition,
    val blurCondition: CameraCaptureCondition,
    val tiltCondition: CameraCaptureCondition,
    val condition: CameraCaptureCondition,
    val message: String
)