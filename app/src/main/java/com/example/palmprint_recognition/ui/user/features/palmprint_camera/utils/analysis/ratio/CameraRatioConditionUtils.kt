package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.ratio

import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition

/**
 * ratio 상태 판단 유틸
 *
 * 역할
 * - 중앙 손 점유율과 경계 접촉도를 기반으로 TOO_FAR, READY, TOO_CLOSE를 판단한다
 * - TOO_FAR를 우선 판정하여 멀리 있는 경우 TOO_CLOSE로 잘못 분류되지 않도록 한다
 */

/**
 * ratio 메트릭을 바탕으로 거리 관련 상태를 판단한다.
 *
 * @param centerOccupancyPercent 중앙 영역 손 후보 점유율
 * @param edgeTouchPercent ROI 경계 접촉 비율
 * @param tooFarThreshold 너무 멀다고 판단하는 중앙 점유율 하한값
 * @param tooCloseThreshold 너무 가깝다고 판단하는 경계 접촉 상한값
 * @return TOO_FAR, READY 또는 TOO_CLOSE
 */
fun evaluateRatioCondition(
    centerOccupancyPercent: Float,
    edgeTouchPercent: Float,
    tooFarThreshold: Float,
    tooCloseThreshold: Float
): CameraCaptureCondition {
    return when {
        centerOccupancyPercent < tooFarThreshold -> {
            CameraCaptureCondition.TOO_FAR
        }

        centerOccupancyPercent >= 70f && edgeTouchPercent > tooCloseThreshold -> {
            CameraCaptureCondition.TOO_CLOSE
        }

        else -> {
            CameraCaptureCondition.READY
        }
    }
}