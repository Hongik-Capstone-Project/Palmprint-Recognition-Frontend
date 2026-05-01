package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis

import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition

/**
 * 카메라 조건 활성화 설정
 *
 * 역할
 * - 테스트 중 특정 조건을 켜고 끌 수 있게 한다
 * - blur / ratio / tilt 조건을 독립적으로 비활성화할 수 있다
 *
 * @property useRatioCondition 거리 조건 사용 여부
 * @property useBlurCondition blur 조건 사용 여부
 * @property useTiltCondition tilt 조건 사용 여부
 */
data class CameraConditionConfig(
    val useRatioCondition: Boolean = true,
    val useBlurCondition: Boolean = true,
    val useTiltCondition: Boolean = true
)

/**
 * 개별 조건을 하나의 최종 촬영 상태로 합친다.
 *
 * 우선순위
 * 1. TOO_FAR / TOO_CLOSE
 * 2. TOO_BLURRY
 * 3. TILT_BAD
 * 4. READY
 *
 * 조건 on/off
 * - config에서 false인 조건은 READY처럼 취급한다
 *
 * @param ratioCondition 거리 관련 상태
 * @param blurCondition blur 관련 상태
 * @param tiltCondition 기울기 관련 상태
 * @param config 조건 활성화 설정
 * @return 최종 촬영 가능 상태
 */
fun resolveCameraCaptureCondition(
    ratioCondition: CameraCaptureCondition,
    blurCondition: CameraCaptureCondition,
    tiltCondition: CameraCaptureCondition,
    config: CameraConditionConfig
): CameraCaptureCondition {
    val effectiveRatioCondition = if (config.useRatioCondition) {
        ratioCondition
    } else {
        CameraCaptureCondition.READY
    }

    val effectiveBlurCondition = if (config.useBlurCondition) {
        blurCondition
    } else {
        CameraCaptureCondition.READY
    }

    val effectiveTiltCondition = if (config.useTiltCondition) {
        tiltCondition
    } else {
        CameraCaptureCondition.READY
    }

    return when {
        effectiveRatioCondition == CameraCaptureCondition.HAND_NOT_DETECTED -> {
            CameraCaptureCondition.HAND_NOT_DETECTED
        }

        effectiveRatioCondition == CameraCaptureCondition.TOO_FAR -> {
            CameraCaptureCondition.TOO_FAR
        }

        effectiveRatioCondition == CameraCaptureCondition.TOO_CLOSE -> {
            CameraCaptureCondition.TOO_CLOSE
        }

        effectiveBlurCondition == CameraCaptureCondition.TOO_BLURRY -> {
            CameraCaptureCondition.TOO_BLURRY
        }

        effectiveTiltCondition == CameraCaptureCondition.TILT_BAD -> {
            CameraCaptureCondition.TILT_BAD
        }

        else -> {
            CameraCaptureCondition.READY
        }
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
        CameraCaptureCondition.HAND_NOT_DETECTED -> {
            "손바닥을 화면에 보여주세요."
        }

        CameraCaptureCondition.TOO_FAR -> {
            "손바닥을 더 가까이 맞춰주세요."
        }

        CameraCaptureCondition.TOO_CLOSE -> {
            "손바닥을 조금 멀리 해주세요."
        }

        CameraCaptureCondition.TOO_BLURRY -> {
            "손을 잠시 멈춰주세요. 초점이 흐립니다."
        }

        CameraCaptureCondition.TILT_BAD -> {
            "손바닥이 기울어졌습니다. 곧게 맞춰주세요."
        }

        CameraCaptureCondition.READY -> {
            "촬영 가능한 상태입니다."
        }
    }
}