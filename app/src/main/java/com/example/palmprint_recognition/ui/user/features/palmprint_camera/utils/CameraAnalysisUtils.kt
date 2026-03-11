package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import android.graphics.Bitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraAnalysisState

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
    conditionConfig: CameraConditionConfig = CameraConditionConfig()
): CameraAnalysisState {
    val ratio = calcGuideCropAreaRatioPercent(
        original = originalBitmap,
        cropped = croppedBitmap
    )

    val blurScore = calculateBlurScore(croppedBitmap)
    val tiltScore = calculateTiltScore(croppedBitmap)

    val ratioCondition = evaluateGuideRatioCondition(ratio)
    val blurCondition = evaluateBlurCondition(blurScore)
    val tiltCondition = evaluateTiltCondition(tiltScore)

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