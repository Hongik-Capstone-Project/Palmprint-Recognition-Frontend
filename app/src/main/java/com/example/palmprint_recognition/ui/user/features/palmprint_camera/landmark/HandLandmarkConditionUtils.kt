package com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark

import androidx.compose.ui.geometry.Rect
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition

private const val HAND_TOO_FAR_HEIGHT_RATIO = 0.65f
private const val HAND_TOO_CLOSE_HEIGHT_RATIO = 0.92f

/**
 * 손 랜드마크 기반 촬영 조건 판정 유틸리티
 *
 * 역할
 * - 손 bounding box와 가이드 영역을 비교한다
 * - 손이 너무 멀거나 가까운지 판단한다
 */

/**
 * 손 크기를 기준으로 거리 조건을 판정한다.
 *
 * @param handRect 손 랜드마크 bounding box
 * @param guideRect 기준 가이드 crop 영역
 * @return 거리 관련 촬영 조건
 */
fun evaluateHandSizeCondition(
    handRect: Rect?,
    guideRect: Rect
): CameraCaptureCondition {
    if (handRect == null) {
        return CameraCaptureCondition.TOO_FAR
    }

    val guideHeight = guideRect.height

    if (guideHeight <= 0f) {
        return CameraCaptureCondition.READY
    }

    val handHeightRatio = handRect.height / guideHeight

    return when {
        handHeightRatio < HAND_TOO_FAR_HEIGHT_RATIO -> {
            CameraCaptureCondition.TOO_FAR
        }

        handHeightRatio > HAND_TOO_CLOSE_HEIGHT_RATIO -> {
            CameraCaptureCondition.TOO_CLOSE
        }

        else -> {
            CameraCaptureCondition.READY
        }
    }
}

/**
 * 디버깅용 손 크기 비율을 계산한다.
 *
 * @param handRect 손 랜드마크 bounding box
 * @param guideRect 기준 가이드 crop 영역
 * @return guideRect 높이 대비 handRect 높이 비율
 */
fun calculateHandHeightRatio(
    handRect: Rect?,
    guideRect: Rect
): Float? {
    if (handRect == null || guideRect.height <= 0f) {
        return null
    }

    return handRect.height / guideRect.height
}