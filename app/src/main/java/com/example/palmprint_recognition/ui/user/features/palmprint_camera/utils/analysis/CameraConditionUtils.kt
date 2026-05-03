package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis

import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition

/**
 * 카메라 조건 활성화 설정
 *
 * 역할
 * - 테스트 중 특정 조건을 켜고 끌 수 있게 한다
 * - hand size / blur / tilt 조건을 독립적으로 비활성화할 수 있다
 *
 * @property useHandSizeCondition 손 크기 조건 사용 여부
 * @property useBlurCondition blur 조건 사용 여부
 * @property useTiltCondition tilt 조건 사용 여부
 */
data class CameraConditionConfig(
    val useHandSizeCondition: Boolean = true,
    val useBlurCondition: Boolean = true,
    val useTiltCondition: Boolean = true
)

/**
 * 카메라 최종 촬영 조건을 계산한다.
 *
 * 우선순위
 * 1. 손 없음
 * 2. 너무 멂 / 너무 가까움
 * 3. blur
 * 4. tilt
 * 5. ready
 *
 * @param handSizeCondition 손 크기 조건
 * @param blurCondition blur 조건
 * @param tiltCondition tilt 조건
 * @param config 조건 활성화 설정
 * @return 최종 촬영 조건
 */
fun resolveCameraCaptureCondition(
    handSizeCondition: CameraCaptureCondition,
    blurCondition: CameraCaptureCondition,
    tiltCondition: CameraCaptureCondition,
    config: CameraConditionConfig
): CameraCaptureCondition {
    val effectiveHandSizeCondition = if (config.useHandSizeCondition) {
        handSizeCondition
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
        effectiveHandSizeCondition == CameraCaptureCondition.HAND_NOT_DETECTED -> {
            CameraCaptureCondition.HAND_NOT_DETECTED
        }

        effectiveHandSizeCondition == CameraCaptureCondition.TOO_FAR -> {
            CameraCaptureCondition.TOO_FAR
        }

        effectiveHandSizeCondition == CameraCaptureCondition.TOO_CLOSE -> {
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
 * 카메라 조건에 맞는 사용자 안내 문구를 반환한다.
 *
 * @param condition 카메라 촬영 조건
 * @return 안내 문구
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