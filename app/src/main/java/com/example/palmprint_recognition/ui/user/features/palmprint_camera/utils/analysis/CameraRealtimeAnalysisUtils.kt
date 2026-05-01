package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.config.CameraAnalysisConfig
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraRealtimeState
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur.calculateRealtimeBlurScore
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur.evaluateBlurCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.ratio.calculateRealtimeRatioMetrics
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.ratio.evaluateRatioCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.tilt.calculateRealtimeTiltScore
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.tilt.evaluateTiltCondition
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
 * @property ratioEstimate 실시간 ratio 근사값
 * @property tiltScore 실시간 tilt 점수
 * @property realtimeState 실시간 상태 결과
 */
data class CameraFrameMetadata(
    val width: Int,
    val height: Int,
    val rotationDegrees: Int,
    val timestamp: Long,
    val frameIndex: Long,
    val blurScore: Float,
    val ratioEstimate: Float,
    val tiltScore: Float,
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
 * - 프리뷰 프레임을 계속 전달받는다
 * - 프레임 스킵 로직을 적용한다
 * - 실시간 blur, ratio, tilt를 계산한다
 * - 각 조건을 합쳐 최종 상태를 만든다
 *
 * 중요
 * - ImageProxy는 반드시 close() 해야 한다
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

            val ratioMetrics = calculateRealtimeRatioMetrics(image)
            val ratioEstimate = ratioMetrics.ratioScore

            val tiltScore = calculateRealtimeTiltScore(
                image = image,
                darkPixelThreshold = CameraAnalysisConfig.REALTIME_TILT_DARK_PIXEL_THRESHOLD
            )

            val blurCondition = evaluateBlurCondition(
                blurScore = blurScore,
                blurThreshold = CameraAnalysisConfig.REALTIME_BLUR_THRESHOLD
            )

            val ratioCondition = evaluateRatioCondition(
                centerOccupancyPercent = ratioMetrics.centerOccupancyPercent,
                edgeTouchPercent = ratioMetrics.edgeTouchPercent,
                tooFarThreshold = CameraAnalysisConfig.REALTIME_TOO_FAR_RATIO_THRESHOLD,
                tooCloseThreshold = CameraAnalysisConfig.REALTIME_TOO_CLOSE_RATIO_THRESHOLD
            )

            val tiltCondition = evaluateTiltCondition(
                tiltScore = tiltScore,
                tiltOffsetThreshold = CameraAnalysisConfig.TILT_OFFSET_THRESHOLD
            )

            val finalCondition = resolveCameraCaptureCondition(
                ratioCondition = ratioCondition,
                blurCondition = blurCondition,
                tiltCondition = tiltCondition,
                config = conditionConfig
            )

            val message = toCameraConditionMessage(finalCondition)

            val realtimeState = CameraRealtimeState(
                isAnalyzing = false,
                blurScore = blurScore,
                ratioEstimate = ratioEstimate,
                tiltScore = tiltScore,
                ratioCondition = ratioCondition,
                blurCondition = blurCondition,
                tiltCondition = tiltCondition,
                condition = finalCondition,
                message = message
            )

            Timber.tag("RealtimeAnalysis").d(
                "frame=%d ratio=%.1f center=%.1f edge=%.1f blur=%.1f tilt=%.3f ratioCond=%s blurCond=%s tiltCond=%s final=%s",
                frameIndex,
                ratioEstimate,
                ratioMetrics.centerOccupancyPercent,
                ratioMetrics.edgeTouchPercent,
                blurScore,
                tiltScore,
                ratioCondition,
                blurCondition,
                tiltCondition,
                finalCondition
            )

            onFrameAvailable(
                CameraFrameMetadata(
                    width = image.width,
                    height = image.height,
                    rotationDegrees = image.imageInfo.rotationDegrees,
                    timestamp = image.imageInfo.timestamp,
                    frameIndex = frameIndex,
                    blurScore = blurScore,
                    ratioEstimate = ratioEstimate,
                    tiltScore = tiltScore,
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