package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.ratio

/**
 * 실시간 ratio 분석 결과
 *
 * @property ratioScore 화면 표시용 종합 점수
 * @property centerOccupancyPercent 중앙 ROI 내 손 후보 점유율
 * @property edgeTouchPercent ROI 경계 접촉 비율
 */
data class CameraRealtimeRatioMetrics(
    val ratioScore: Float,
    val centerOccupancyPercent: Float,
    val edgeTouchPercent: Float
)