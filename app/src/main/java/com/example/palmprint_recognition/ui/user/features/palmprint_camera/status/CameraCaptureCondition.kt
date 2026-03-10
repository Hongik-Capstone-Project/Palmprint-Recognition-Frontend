package com.example.palmprint_recognition.ui.user.features.palmprint_camera.status

/**
 * 카메라 촬영 가능 상태
 *
 * 역할
 * - 현재 촬영 조건을 사용자에게 알려준다
 * - 촬영 후 분석 결과를 표시하는 데 사용한다
 * - 이후에는 프리뷰 실시간 분석 상태에도 재사용한다
 */
enum class CameraCaptureCondition {
    TOO_FAR,
    TOO_CLOSE,
    TOO_BLURRY,
    READY
}