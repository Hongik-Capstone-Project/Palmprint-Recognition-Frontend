package com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark

import androidx.compose.ui.geometry.Rect
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

private const val HAND_TOO_FAR_HEIGHT_RATIO = 0.70f
private const val HAND_TOO_CLOSE_HEIGHT_RATIO = 0.92f
private const val HAND_TILT_THRESHOLD_DEGREES = 19f

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
        return CameraCaptureCondition.HAND_NOT_DETECTED
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


/**
 * 손 랜드마크 기반 tilt 점수를 계산한다.
 *
 * 기준
 * - 5번 index MCP와 17번 pinky MCP를 잇는 선의 각도를 사용한다
 * - 손바닥이 정면으로 곧게 있으면 이 선은 대체로 가로에 가깝다
 *
 * @param landmarks 손 랜드마크 목록
 * @return 가로선 기준 기울어진 각도, 계산 불가 시 null
 */
fun calculateHandTiltDegrees(
    landmarks: List<HandLandmarkPoint>
): Float? {
    val indexMcp = landmarks.getOrNull(5) ?: return null
    val pinkyMcp = landmarks.getOrNull(17) ?: return null

    val dx = pinkyMcp.x - indexMcp.x
    val dy = pinkyMcp.y - indexMcp.y

    if (dx == 0f && dy == 0f) {
        return null
    }

    val radians = atan2(
        y = dy.toDouble(),
        x = dx.toDouble()
    )

    val degrees = radians * 180.0 / PI

    return degrees.toFloat()
}

/**
 * 손 랜드마크 기반으로 손바닥 기울기 조건을 판정한다.
 *
 * @param landmarks 손 랜드마크 목록
 * @return tilt 관련 촬영 조건
 */
fun evaluateHandTiltCondition(
    landmarks: List<HandLandmarkPoint>
): CameraCaptureCondition {
    val tiltDegrees = calculateHandTiltDegrees(
        landmarks = landmarks
    ) ?: return CameraCaptureCondition.HAND_NOT_DETECTED

    return if (abs(tiltDegrees) > HAND_TILT_THRESHOLD_DEGREES) {
        CameraCaptureCondition.TILT_BAD
    } else {
        CameraCaptureCondition.READY
    }
}