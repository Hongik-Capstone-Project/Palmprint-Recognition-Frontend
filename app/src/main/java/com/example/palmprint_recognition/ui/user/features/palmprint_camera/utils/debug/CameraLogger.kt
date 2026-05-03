package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.debug

import com.example.palmprint_recognition.ui.user.features.palmprint_camera.config.CameraAnalysisConfig
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCaptureCondition
import timber.log.Timber

/**
 * 카메라 모듈 로그 유틸리티
 *
 * 역할
 * - 카메라 관련 Timber 로그를 한 곳에서 관리한다.
 * - CameraAnalysisConfig.ENABLE_CAMERA_DEBUG_LOG 값으로 로그 출력을 제어한다.
 */
object CameraLogger {

    private const val TAG_CAMERA_SCREEN = "CameraScreen"
    private const val TAG_CAMERA_REALTIME = "CameraRealtime"
    private const val TAG_HAND_LANDMARK = "HandLandmark"
    private const val TAG_PALM_CROP = "PalmCrop"

    /**
     * 손 랜드마크 결과를 로그로 출력한다.
     *
     * @param landmarkCount 랜드마크 개수
     * @param handedness 손 방향 정보
     */
    fun logHandLandmarkResult(
        landmarkCount: Int,
        handedness: String?
    ) {
        if (!CameraAnalysisConfig.ENABLE_CAMERA_DEBUG_LOG) {
            return
        }

        Timber.tag(TAG_HAND_LANDMARK).d(
            "landmarkCount=%d handedness=%s",
            landmarkCount,
            handedness
        )
    }

    /**
     * 손 랜드마크 에러를 로그로 출력한다.
     *
     * @param throwable 에러 객체
     */
    fun logHandLandmarkError(
        throwable: Throwable
    ) {
        Timber.tag(TAG_HAND_LANDMARK).e(
            throwable,
            "Hand landmark detection failed"
        )
    }

    /**
     * 실시간 카메라 분석 결과를 로그로 출력한다.
     *
     * @param frameIndex 프레임 번호
     * @param width 프레임 너비
     * @param height 프레임 높이
     * @param rotationDegrees 회전 각도
     * @param blurScore blur 점수
     * @param blurCondition blur 조건
     */
    fun logRealtimeFrame(
        frameIndex: Long,
        width: Int,
        height: Int,
        rotationDegrees: Int,
        blurScore: Float,
        blurCondition: CameraCaptureCondition
    ) {
        if (!CameraAnalysisConfig.ENABLE_CAMERA_DEBUG_LOG) {
            return
        }

        Timber.tag(TAG_CAMERA_REALTIME).d(
            "frameIndex=%d frame=%dx%d rotation=%d blur=%.1f condition=%s",
            frameIndex,
            width,
            height,
            rotationDegrees,
            blurScore,
            blurCondition
        )
    }

    /**
     * 카메라 초기화 실패 로그를 출력한다.
     *
     * @param throwable 에러 객체
     */
    fun logCameraBindError(
        throwable: Throwable
    ) {
        Timber.tag(TAG_CAMERA_SCREEN).e(
            throwable,
            "Camera bind failed"
        )
    }

    /**
     * crop 결과 로그를 출력한다.
     *
     * @param message crop 결과 메시지
     */
    fun logPalmCrop(
        message: String
    ) {
        if (!CameraAnalysisConfig.ENABLE_CAMERA_DEBUG_LOG) {
            return
        }

        Timber.tag(TAG_PALM_CROP).d(message)
    }

    /**
     * 카메라 디버그 상태를 로그로 출력한다.
     *
     * @param landmarkCount 현재 손 랜드마크 개수
     * @param handHeightRatio 손 높이 비율
     * @param handSizeCondition 손 크기 조건
     * @param handTiltDegrees 손 기울기 각도
     * @param handTiltCondition 손 기울기 조건
     */
    fun logCameraDebugStatus(
        landmarkCount: Int,
        handHeightRatio: Float?,
        handSizeCondition: CameraCaptureCondition,
        handTiltDegrees: Float?,
        handTiltCondition: CameraCaptureCondition
    ) {
        if (!CameraAnalysisConfig.ENABLE_CAMERA_DEBUG_LOG) {
            return
        }

        Timber.tag(TAG_CAMERA_SCREEN).d(
            "landmarks=%d handRatio=%s sizeCond=%s tilt=%s tiltCond=%s",
            landmarkCount,
            handHeightRatio?.let { "%.2f".format(it) } ?: "-",
            handSizeCondition,
            handTiltDegrees?.let { "%.1f".format(it) } ?: "-",
            handTiltCondition
        )
    }
}