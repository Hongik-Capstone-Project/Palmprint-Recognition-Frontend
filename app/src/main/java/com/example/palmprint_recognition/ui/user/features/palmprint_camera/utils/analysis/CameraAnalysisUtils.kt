package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis

import android.graphics.Bitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.config.CameraAnalysisConfig
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraAnalysisState
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur.calculateBitmapBlurScore
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.blur.evaluateBlurCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.ratio.calculateBitmapRatioPercent
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.ratio.evaluateRatioCondition
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.tilt.calculateBitmapTiltScore
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.tilt.evaluateTiltCondition

/**
 * 촬영 이미지 분석 종합 유틸
 *
 * 역할
 * - ratio 계산
 * - blur score 계산
 * - tilt score 계산
 * - 개별 조건 평가
 * - 조건 on/off 설정 반영
 * - 최종 촬영 상태 생성
 */

/**
 * 촬영된 이미지를 분석하여 종합 결과를 반환한다.
 *
 * @param originalBitmap 원본 Bitmap
 * @param croppedBitmap crop된 Bitmap
 * @param conditionConfig 조건 활성화 설정
 * @return 분석 결과 상태
 */
fun analyzeCapturedBitmap(
    originalBitmap: Bitmap,
    croppedBitmap: Bitmap,
    conditionConfig: CameraConditionConfig = CameraAnalysisConfig.conditionConfig
): CameraAnalysisState {
    val ratio = calculateBitmapRatioPercent(
        original = originalBitmap,
        cropped = croppedBitmap
    )

    val blurScore = calculateBitmapBlurScore(croppedBitmap)

    val tiltScore = calculateBitmapTiltScore(
        bitmap = croppedBitmap,
        darkPixelThreshold = CameraAnalysisConfig.BITMAP_TILT_DARK_PIXEL_THRESHOLD
    )

    val ratioCondition = evaluateRatioCondition(
        ratio = ratio,
        tooFarThreshold = CameraAnalysisConfig.TOO_FAR_RATIO_THRESHOLD,
        tooCloseThreshold = CameraAnalysisConfig.TOO_CLOSE_RATIO_THRESHOLD
    )

    val blurCondition = evaluateBlurCondition(
        blurScore = blurScore,
        blurThreshold = CameraAnalysisConfig.BITMAP_BLUR_THRESHOLD
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

    return CameraAnalysisState(
        ratio = ratio,
        blurScore = blurScore,
        tiltScore = tiltScore,
        ratioCondition = ratioCondition,
        blurCondition = blurCondition,
        tiltCondition = tiltCondition,
        condition = finalCondition,
        message = message
    )
}