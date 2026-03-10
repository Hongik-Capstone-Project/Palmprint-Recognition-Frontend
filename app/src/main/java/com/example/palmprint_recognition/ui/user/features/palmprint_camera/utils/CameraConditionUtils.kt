package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition

/**
 * 개별 조건을 하나의 최종 촬영 상태로 합치는 유틸
 */

/**
 * 거리 조건과 blur 조건을 합쳐 최종 촬영 상태를 결정한다.
 *
 * 우선순위
 * 1. TOO_FAR / TOO_CLOSE
 * 2. TOO_BLURRY
 * 3. READY
 *
 * @param ratioCondition 거리 관련 상태
 * @param blurCondition blur 관련 상태
 * @return 최종 촬영 가능 상태
 */
fun resolveCameraCaptureCondition(
    ratioCondition: CameraCaptureCondition,
    blurCondition: CameraCaptureCondition
): CameraCaptureCondition {
    return when {
        ratioCondition == CameraCaptureCondition.TOO_FAR -> CameraCaptureCondition.TOO_FAR
        ratioCondition == CameraCaptureCondition.TOO_CLOSE -> CameraCaptureCondition.TOO_CLOSE
        blurCondition == CameraCaptureCondition.TOO_BLURRY -> CameraCaptureCondition.TOO_BLURRY
        else -> CameraCaptureCondition.READY
    }
}

/**
 * 최종 촬영 상태를 사용자 메시지로 변환한다.
 *
 * @param condition 최종 촬영 가능 상태
 * @return 사용자 안내 문구
 */
fun toCameraConditionMessage(
    condition: CameraCaptureCondition
): String {
    return when (condition) {
        CameraCaptureCondition.TOO_FAR -> "손바닥을 더 가까이 맞춰주세요."
        CameraCaptureCondition.TOO_CLOSE -> "손바닥을 조금 멀리 해주세요."
        CameraCaptureCondition.TOO_BLURRY -> "손을 잠시 멈춰주세요. 초점이 흐립니다."
        CameraCaptureCondition.READY -> "촬영 가능한 상태입니다."
    }
}