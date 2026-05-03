package com.example.palmprint_recognition.ui.user.features.palmprint_camera.status

/**
 * 자동촬영 상태
 */
enum class AutoCaptureState {
    DISABLED,
    WAITING,
    READY_HOLDING,
    CAPTURING,
    COOLDOWN
}