package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraRealtimeState

private const val DEFAULT_ANALYSIS_INTERVAL = 5

/**
 * 실시간 프리뷰 프레임 메타 정보
 *
 * 역할
 * - ImageAnalysis에서 받은 프레임의 기본 정보와
 *   실시간 blur 분석 결과를 함께 담는다
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
    if (interval <= 1) return true
    return frameIndex % interval == 0L
}

/**
 * CameraX ImageAnalysis 전용 Analyzer
 *
 * 역할
 * - 프리뷰 프레임을 계속 전달받는다
 * - 프레임 스킵 로직을 적용한다
 * - 실시간 blur score 계산은 CameraBlurUtils에 위임한다
 * - blur 결과를 기반으로 실시간 상태를 만든다
 *
 * 중요
 * - ImageProxy는 반드시 close() 해야 한다
 *
 * @param analysisInterval 몇 프레임마다 1번 분석할지 결정하는 주기
 * @param blurThreshold blur 최소 기준값
 * @param onFrameAvailable 분석 완료된 프레임 결과 콜백
 */
class CameraRealtimeFrameAnalyzer(
    private val analysisInterval: Int = DEFAULT_ANALYSIS_INTERVAL,
    private val blurThreshold: Float = 80f,
    private val onFrameAvailable: (CameraFrameMetadata) -> Unit
) : ImageAnalysis.Analyzer {

    private var frameIndex: Long = 0L

    /**
     * CameraX가 전달한 프레임을 처리한다.
     *
     * 처리 순서
     * 1. 프레임 번호 증가
     * 2. 분석 대상 프레임인지 확인
     * 3. 실시간 blur score 계산
     * 4. blur 상태를 CameraRealtimeState로 변환
     * 5. 결과 콜백 전달
     * 6. 항상 image.close() 호출
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

            val condition = evaluateRealtimeBlurCondition(
                blurScore = blurScore,
                blurThreshold = blurThreshold
            )

            val realtimeState = CameraRealtimeState(
                isAnalyzing = false,
                blurScore = blurScore,
                ratioEstimate = null,
                condition = condition,
                message = toRealtimeConditionMessage(condition)
            )

            val metadata = CameraFrameMetadata(
                width = image.width,
                height = image.height,
                rotationDegrees = image.imageInfo.rotationDegrees,
                timestamp = image.imageInfo.timestamp,
                frameIndex = frameIndex,
                blurScore = blurScore,
                realtimeState = realtimeState
            )

            onFrameAvailable(metadata)
        } finally {
            image.close()
        }
    }
}

/**
 * 실시간 촬영 상태를 사용자 메시지로 변환한다.
 *
 * 현재 단계
 * - blur 상태만 사용한다
 *
 * @param condition 실시간 촬영 상태
 * @return 사용자 안내 문구
 */
fun toRealtimeConditionMessage(
    condition: CameraCaptureCondition
): String {
    return when (condition) {
        CameraCaptureCondition.TOO_BLURRY -> "손을 잠시 멈춰주세요. 초점이 흐립니다."
        CameraCaptureCondition.READY -> "촬영 가능한 상태입니다."
        CameraCaptureCondition.TOO_FAR -> "손바닥을 더 가까이 맞춰주세요."
        CameraCaptureCondition.TOO_CLOSE -> "손바닥을 조금 멀리 해주세요."
    }
}