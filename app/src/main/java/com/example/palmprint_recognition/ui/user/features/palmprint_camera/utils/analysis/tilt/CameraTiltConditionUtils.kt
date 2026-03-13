package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.tilt

import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition

/**
 * tilt 상태 판단 유틸
 *
 * 역할
 * - tilt score를 기반으로 촬영 가능 상태를 판단한다
 */

/**
 * 기울기 점수를 바탕으로 tilt 상태를 판단한다.
 *
 * @param tiltScore 기울기 점수
 * @param tiltOffsetThreshold 기울기 허용 기준값
 * @return TILT_BAD / READY
 */
fun evaluateTiltCondition(
    tiltScore: Float,
    tiltOffsetThreshold: Float
): CameraCaptureCondition {
    return if (tiltScore > tiltOffsetThreshold) {
        CameraCaptureCondition.TILT_BAD
    } else {
        CameraCaptureCondition.READY
    }
}