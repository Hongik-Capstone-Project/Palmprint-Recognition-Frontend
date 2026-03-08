package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Rect
import kotlin.math.roundToInt

/**
 * 손바닥 가이드라인 계산 관련 유틸리티
 */

/**
 * 손바닥 촬영 가이드 영역 정보
 *
 * @property ovalRect 타원 가이드의 bounding rect
 * @property wristY 손목 기준선 위치
 */
data class GuideSpec(
    val ovalRect: Rect,
    val wristY: Float
)

/**
 * 화면 크기에 맞는 손바닥 가이드 영역 계산
 *
 * @param viewW 프리뷰 너비
 * @param viewH 프리뷰 높이
 */
fun calculateGuideSpec(
    viewW: Float,
    viewH: Float
): GuideSpec {

    val ovalW = viewW * 0.75f
    val ovalH = viewH * 0.50f

    val ovalLeft = (viewW - ovalW) / 2f
    val ovalTop = (viewH - ovalH) / 2f

    val ovalRect = Rect(
        ovalLeft,
        ovalTop,
        ovalLeft + ovalW,
        ovalTop + ovalH
    )

    val wristY = ovalRect.bottom + (viewH * 0.03f)

    return GuideSpec(
        ovalRect = ovalRect,
        wristY = wristY
    )
}

/**
 * 프리뷰 기준 가이드 영역을 실제 Bitmap 좌표로 변환하여 crop
 *
 * @param bitmap 원본 이미지
 * @param viewW 프리뷰 너비
 * @param viewH 프리뷰 높이
 */
fun cropBitmapByGuideRect(
    bitmap: Bitmap,
    viewW: Float,
    viewH: Float
): Bitmap {

    val spec = calculateGuideSpec(viewW, viewH)
    val guideRect = spec.ovalRect

    val leftN = (guideRect.left / viewW).coerceIn(0f, 1f)
    val topN = (guideRect.top / viewH).coerceIn(0f, 1f)
    val rightN = (guideRect.right / viewW).coerceIn(0f, 1f)
    val bottomN = (guideRect.bottom / viewH).coerceIn(0f, 1f)

    val x = (leftN * bitmap.width).roundToInt().coerceIn(0, bitmap.width - 1)
    val y = (topN * bitmap.height).roundToInt().coerceIn(0, bitmap.height - 1)

    val w = ((rightN - leftN) * bitmap.width).roundToInt().coerceAtLeast(1)
    val h = ((bottomN - topN) * bitmap.height).roundToInt().coerceAtLeast(1)

    val safeW = (x + w).coerceAtMost(bitmap.width) - x
    val safeH = (y + h).coerceAtMost(bitmap.height) - y

    return Bitmap.createBitmap(
        bitmap,
        x,
        y,
        safeW,
        safeH
    )
}

/**
 * 원본 대비 crop 영역 비율 계산
 *
 * 주의
 * 손바닥 비율이 아니라 "가이드 영역 비율"이다.
 */
fun calcGuideCropAreaRatioPercent(
    original: Bitmap,
    cropped: Bitmap
): Float {

    val total = original.width.toFloat() * original.height.toFloat()

    val part = cropped.width.toFloat() * cropped.height.toFloat()

    if (total <= 0f) return 0f

    return (part / total) * 100f
}