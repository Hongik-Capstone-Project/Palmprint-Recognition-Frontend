package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Rect
import kotlin.math.roundToInt
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF

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
 * 가이드 Rect 기준으로 먼저 사각 crop을 수행한다.
 *
 * @param bitmap 원본 Bitmap
 * @param viewW 프리뷰 너비
 * @param viewH 프리뷰 높이
 * @return guide rect 기준으로 crop된 Bitmap
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
 * crop된 사각형 Bitmap에 타원 마스크를 적용한다.
 *
 * 동작
 * - 사각형 crop 결과 위에 타원 path를 만든다
 * - 타원 내부만 남기고, 바깥은 검은색으로 채운다
 *
 * @param croppedRectBitmap guide rect 기준으로 crop된 Bitmap
 * @return 타원 내부만 유효한 Bitmap
 */
fun applyOvalMaskToCroppedBitmap(
    croppedRectBitmap: Bitmap
): Bitmap {

    val width = croppedRectBitmap.width
    val height = croppedRectBitmap.height

    val output = Bitmap.createBitmap(
        width,
        height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(output)

    val ovalPath = Path().apply {
        addOval(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            Path.Direction.CW
        )
    }

    canvas.save()
    canvas.clipPath(ovalPath)
    canvas.drawBitmap(croppedRectBitmap, 0f, 0f, null)
    canvas.restore()

    val borderPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = android.graphics.Color.WHITE
        strokeWidth = 4f
    }

    canvas.drawOval(
        RectF(0f, 0f, width.toFloat(), height.toFloat()),
        borderPaint
    )

    return output
}

/**
 * 가이드 영역 기준으로 사각 crop 후 타원 마스크까지 적용한다.
 *
 * @param bitmap 원본 Bitmap
 * @param viewW 프리뷰 너비
 * @param viewH 프리뷰 높이
 * @return 타원 마스크가 적용된 Bitmap
 */
fun cropBitmapByGuideOval(
    bitmap: Bitmap,
    viewW: Float,
    viewH: Float
): Bitmap {

    val rectCropped = cropBitmapByGuideRect(
        bitmap = bitmap,
        viewW = viewW,
        viewH = viewH
    )

    return applyOvalMaskToCroppedBitmap(rectCropped)
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