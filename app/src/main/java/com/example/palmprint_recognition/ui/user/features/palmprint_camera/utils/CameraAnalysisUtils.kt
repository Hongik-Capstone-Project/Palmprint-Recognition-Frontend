package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import android.graphics.Bitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraAnalysisState

/**
 * 촬영 이미지 분석 종합 유틸
 *
 * 역할
 * - ratio 계산
 * - blur score 계산
 * - 개별 조건 평가
 * - 최종 촬영 상태 생성
 */

/**
 * 촬영된 이미지를 분석하여 종합 결과를 반환한다.
 *
 * @param originalBitmap 원본 Bitmap
 * @param croppedBitmap crop된 Bitmap
 * @return 분석 결과 상태
 */
fun analyzeCapturedBitmap(
    originalBitmap: Bitmap,
    croppedBitmap: Bitmap
): CameraAnalysisState {
    val ratio = calcGuideCropAreaRatioPercent(
        original = originalBitmap,
        cropped = croppedBitmap
    )

    val blurScore = calculateBlurScore(croppedBitmap)

    val ratioCondition = evaluateGuideRatioCondition(ratio)
    val blurCondition = evaluateBlurCondition(blurScore)

    val finalCondition = resolveCameraCaptureCondition(
        ratioCondition = ratioCondition,
        blurCondition = blurCondition
    )

    return CameraAnalysisState(
        ratio = ratio,
        blurScore = blurScore,
        condition = finalCondition
    )
}