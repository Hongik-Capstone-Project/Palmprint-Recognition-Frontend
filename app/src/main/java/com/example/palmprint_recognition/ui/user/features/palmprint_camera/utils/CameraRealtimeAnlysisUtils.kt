package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraRealtimeState

private const val DEFAULT_ANALYSIS_INTERVAL = 5
private const val DEFAULT_REALTIME_BLUR_THRESHOLD = 80f

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
 * - 실시간 blur / ratio / tilt를 계산한다
 * - 각 조건을 합쳐 최종 상태를 만든다
 *
 * 중요
 * - ImageProxy는 반드시 close() 해야 한다
 *
 * @param analysisInterval 몇 프레임마다 1번 분석할지 결정하는 주기
 * @param blurThreshold blur 최소 기준값
 * @param conditionConfig 조건 활성화 설정
 * @param onFrameAvailable 분석 완료된 프레임 결과 콜백
 */
class CameraRealtimeFrameAnalyzer(
    private val analysisInterval: Int = DEFAULT_ANALYSIS_INTERVAL,
    private val blurThreshold: Float = DEFAULT_REALTIME_BLUR_THRESHOLD,
    private val conditionConfig: CameraConditionConfig = CameraConditionConfig(),
    private val onFrameAvailable: (CameraFrameMetadata) -> Unit
) : ImageAnalysis.Analyzer {

    private var frameIndex: Long = 0L

    /**
     * CameraX가 전달한 프레임을 처리한다.
     *
     * 처리 순서
     * 1. 프레임 번호 증가
     * 2. 분석 대상 프레임인지 확인
     * 3. blur score 계산
     * 4. ratio 근사 계산
     * 5. tilt score 계산
     * 6. blur / ratio / tilt 상태 평가
     * 7. 조건 on/off를 반영하여 최종 상태 조합
     * 8. 결과 콜백 전달
     * 9. 항상 image.close() 호출
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
            val ratioEstimate = calculateRealtimeRatioEstimate(image)
            val tiltScore = calculateRealtimeTiltScore(image)

            val blurCondition = evaluateRealtimeBlurCondition(
                blurScore = blurScore,
                blurThreshold = blurThreshold
            )

            val ratioCondition = evaluateRealtimeRatioCondition(
                ratioEstimate = ratioEstimate
            )

            val tiltCondition = evaluateTiltCondition(
                tiltScore = tiltScore
            )

            val finalCondition = resolveCameraCaptureCondition(
                ratioCondition = ratioCondition,
                blurCondition = blurCondition,
                tiltCondition = tiltCondition,
                config = conditionConfig
            )

            val realtimeState = CameraRealtimeState(
                isAnalyzing = false,
                blurScore = blurScore,
                ratioEstimate = ratioEstimate,
                tiltScore = tiltScore,
                ratioCondition = ratioCondition,
                blurCondition = blurCondition,
                tiltCondition = tiltCondition,
                condition = finalCondition,
                message = toCameraConditionMessage(finalCondition)
            )

            val metadata = CameraFrameMetadata(
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

            onFrameAvailable(metadata)
        } finally {
            image.close()
        }
    }
}