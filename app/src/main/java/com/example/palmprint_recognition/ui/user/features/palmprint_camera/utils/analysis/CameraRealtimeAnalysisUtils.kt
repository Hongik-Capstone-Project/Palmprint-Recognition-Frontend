package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.config.CameraAnalysisConfig
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraRealtimeState
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur.calculateRealtimeBlurScore
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur.evaluateBlurCondition
import timber.log.Timber
import android.graphics.Bitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.helper.convertImageProxyToBitmap

private const val DEFAULT_ANALYSIS_INTERVAL = 5

/**
 * 실시간 프리뷰 프레임 메타 정보
 *
 * 역할
 * - ImageAnalysis에서 받은 프레임의 기본 정보와
 *   실시간 분석 결과를 함께 담는다
 *
 * @property width 프레임 너비
 * @property height 프레임 높이
 * @property rotationDegrees 프레임 회전 정보
 * @property timestamp 프레임 타임스탬프
 * @property frameIndex 누적 프레임 번호
 * @property blurScore 실시간 blur score
 * @property realtimeState 실시간 상태 결과
 */
data class CameraFrameMetadata(
    val width: Int,
    val height: Int,
    val rotationDegrees: Int,
    val timestamp: Long,
    val frameIndex: Long,
    val blurScore: Float,
    val realtimeState: CameraRealtimeState
)

/**
 * 현재 프레임을 분석 대상으로 사용할지 판단한다.
 *
 * @param frameIndex 현재 프레임 번호
 * @param interval 분석 주기
 * @return 분석 대상 여부
 */
fun shouldAnalyzeFrame(
    frameIndex: Long,
    interval: Int = DEFAULT_ANALYSIS_INTERVAL
): Boolean {
    if (interval <= 1) {
        return true
    }

    return frameIndex % interval == 0L
}

/**
 * CameraX ImageAnalysis 전용 Analyzer
 *
 * 역할
 * ImageProxy 받기
 * → blur 계산
 * → MediaPipe용 Bitmap 전달
 * → ImageProxy close
 *
 * @param analysisInterval 몇 프레임마다 1번 분석할지 결정하는 주기
 * @param conditionConfig 조건 활성화 설정
 * @param onFrameAvailable 분석 완료된 프레임 결과 콜백
 */
class CameraRealtimeFrameAnalyzer(
    private val analysisInterval: Int = DEFAULT_ANALYSIS_INTERVAL,
    private val conditionConfig: CameraConditionConfig = CameraAnalysisConfig.conditionConfig,
    private val onBitmapFrameAvailable: ((Bitmap) -> Unit)? = null,
    private val onFrameAvailable: (CameraFrameMetadata) -> Unit
) : ImageAnalysis.Analyzer {

    private var frameIndex: Long = 0L

    /**
     * CameraX가 전달한 프레임을 처리한다.
     *
     * @param image CameraX 분석 프레임
     */
    override fun analyze(
        image: ImageProxy
    ) {
        frameIndex += 1L

        try {
            if (!shouldAnalyzeFrame(frameIndex, analysisInterval)) {
                return
            }

            val blurScore = calculateRealtimeBlurScore(image)

            val blurCondition = evaluateBlurCondition(
                blurScore = blurScore,
                blurThreshold = CameraAnalysisConfig.REALTIME_BLUR_THRESHOLD
            )

            val realtimeState = CameraRealtimeState(
                isAnalyzing = false,
                blurScore = blurScore,
                blurCondition = blurCondition
            )

            onFrameAvailable(
                CameraFrameMetadata(
                    width = image.width,
                    height = image.height,
                    rotationDegrees = image.imageInfo.rotationDegrees,
                    timestamp = image.imageInfo.timestamp,
                    frameIndex = frameIndex,
                    blurScore = blurScore,
                    realtimeState = realtimeState
                )
            )

            convertImageProxyToBitmap(image)?.let { bitmap ->
                onBitmapFrameAvailable?.invoke(bitmap)
            }
        } catch (exception: Exception) {
            Timber.tag("RealtimeAnalysis").e(
                exception,
                "Realtime frame analysis failed"
            )
        } finally {
            image.close()
        }
    }
}