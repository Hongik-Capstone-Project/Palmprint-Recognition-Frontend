package com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark

import androidx.compose.ui.geometry.Rect
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.config.CameraAnalysisConfig
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

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

    if (guideRect.height <= 0f) {
        return CameraCaptureCondition.HAND_NOT_DETECTED
    }

    val handHeightRatio = handRect.height / guideRect.height

    return when {
        handHeightRatio < CameraAnalysisConfig.HAND_TOO_FAR_HEIGHT_RATIO -> {
            CameraCaptureCondition.TOO_FAR
        }

        handHeightRatio > CameraAnalysisConfig.HAND_TOO_CLOSE_HEIGHT_RATIO -> {
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

    return (radians * 180.0 / PI).toFloat()
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

    return if (abs(tiltDegrees) > CameraAnalysisConfig.HAND_TILT_THRESHOLD_DEGREES) {
        CameraCaptureCondition.TILT_BAD
    } else {
        CameraCaptureCondition.READY
    }
}

/**
 * 자동촬영용 조건을 판정한다.
 *
 * 일반 READY보다 더 엄격한 기준을 사용한다.
 *
 * @param handHeightRatio 손 높이 비율
 * @param handTiltDegrees 손 기울기 각도
 * @param finalCondition 현재 최종 촬영 조건
 * @return 자동촬영 가능 여부
 */
fun canAutoCaptureByLandmark(
    handHeightRatio: Float?,
    handTiltDegrees: Float?,
    finalCondition: CameraCaptureCondition
): Boolean {
    if (finalCondition != CameraCaptureCondition.READY) {
        return false
    }

    val ratio = handHeightRatio ?: return false
    val tilt = handTiltDegrees ?: return false

    val isGoodRatio =
        ratio >= CameraAnalysisConfig.AUTO_CAPTURE_MIN_HAND_RATIO &&
                ratio <= CameraAnalysisConfig.AUTO_CAPTURE_MAX_HAND_RATIO

    val isGoodTilt =
        abs(tilt) <= CameraAnalysisConfig.AUTO_CAPTURE_MAX_TILT_DEGREES

    return isGoodRatio && isGoodTilt
}