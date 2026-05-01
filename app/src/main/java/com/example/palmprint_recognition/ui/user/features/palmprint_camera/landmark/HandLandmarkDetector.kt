package com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import timber.log.Timber

class HandLandmarkDetector(
    private val context: Context,
    private val onResult: (HandLandmarkResultWrapper) -> Unit,
    private val onError: (Throwable) -> Unit
) {
    private var handLandmarker: HandLandmarker? = null

    /**
     * MediaPipe HandLandmarker를 초기화한다.
     *
     * 역할
     * - assets/hand_landmarker.task 모델을 로드한다
     * - 실시간 카메라 입력용 LIVE_STREAM 모드로 설정한다
     * - 안정성을 위해 CPU delegate를 명시한다
     */
    fun setup() {
        close()

        Timber.tag("HandLandmark").d("setup started")

        val baseOptions = BaseOptions.builder()
            .setModelAssetPath("hand_landmarker.task")
            .build()

        val options = HandLandmarker.HandLandmarkerOptions.builder()
            .setBaseOptions(baseOptions)
            .setRunningMode(RunningMode.LIVE_STREAM)
            .setNumHands(1)
            .setMinHandDetectionConfidence(0.5f)
            .setMinHandPresenceConfidence(0.5f)
            .setMinTrackingConfidence(0.5f)
            .setResultListener { result, _ ->
                onResult(result.toAppResult())
            }
            .setErrorListener { error ->
                onError(error)
            }
            .build()

        handLandmarker = HandLandmarker.createFromOptions(context, options)

        Timber.tag("HandLandmark").d("setup finished")
    }

    /**
     * Bitmap에서 손 랜드마크를 비동기로 검출한다.
     *
     * @param bitmap 분석할 카메라 프레임 Bitmap
     */
    fun detect(
        bitmap: Bitmap
    ) {
        val landmarker = handLandmarker ?: return

        runCatching {
            val argbBitmap =
                if (bitmap.config == Bitmap.Config.ARGB_8888) {
                    bitmap
                } else {
                    bitmap.copy(Bitmap.Config.ARGB_8888, false)
                }

            val mpImage = BitmapImageBuilder(argbBitmap).build()
            val timestampMs = SystemClock.uptimeMillis()

            landmarker.detectAsync(
                mpImage,
                timestampMs
            )
        }.onFailure { exception ->
            onError(exception)
        }
    }

    /**
     * MediaPipe 리소스를 해제한다.
     */
    fun close() {
        handLandmarker?.close()
        handLandmarker = null
    }
}

data class HandLandmarkResultWrapper(
    val landmarks: List<HandLandmarkPoint>,
    val handedness: String?
) {
    val handDetected: Boolean
        get() = landmarks.isNotEmpty()
}

private fun HandLandmarkerResult.toAppResult(): HandLandmarkResultWrapper {
    val firstHandLandmarks = this.landmarks().firstOrNull()

    val points = firstHandLandmarks
        ?.mapIndexed { index, landmark ->
            HandLandmarkPoint(
                index = index,
                x = landmark.x(),
                y = landmark.y(),
                z = landmark.z()
            )
        }
        ?: emptyList()

    val handedness = this.handedness()
        .firstOrNull()
        ?.firstOrNull()
        ?.categoryName()

    return HandLandmarkResultWrapper(
        landmarks = points,
        handedness = handedness
    )
}