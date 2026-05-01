package com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark

data class HandLandmarkResult(
    val landmarks: List<HandLandmarkPoint> = emptyList(),
    val handedness: String? = null
) {
    val handDetected: Boolean
        get() = landmarks.isNotEmpty()
}