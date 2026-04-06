package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.guide

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Rect
import kotlin.math.roundToInt

private const val PREVIEW_ASPECT_RATIO = 3f / 4f

/**
 * 손바닥 가이드 이미지 크기/위치 조정 상수
 *
 * 설명
 * - BASE_GUIDE_WIDTH_RATIO: 기존 기준 크기
 * - GUIDE_SCALE: 기존 대비 확대 배율
 * - GUIDE_TOP_OFFSET_RATIO: 프리뷰 상단에서 얼마나 아래로 내릴지
 *
 * 주의
 * - GUIDE_SCALE을 크게 해도 실제 화면에서는 previewRect를 넘지 않도록 제한한다
 */
private const val BASE_GUIDE_WIDTH_RATIO = 0.78f
private const val GUIDE_SCALE = 2f
private const val GUIDE_TOP_OFFSET_RATIO = 0.03f

/**
 * 손바닥 PNG 가이드가 화면을 너무 벗어나지 않도록 제한하는 값
 */
private const val MAX_GUIDE_WIDTH_RATIO = 1.3f
private const val MAX_GUIDE_HEIGHT_RATIO = 1.3f

/**
 * 손바닥 촬영 가이드 영역 정보
 *
 * 역할
 * - previewRect: 실제 카메라 프리뷰가 보이는 영역
 * - guideImageRect: 화면에 표시된 손바닥 PNG 가이드 이미지의 배치 영역
 * - cropRect: 서버 전송용 직사각형 crop 영역
 */
data class GuideSpec(
    val previewRect: Rect,
    val guideImageRect: Rect,
    val cropRect: Rect
)

private fun calculatePreviewContentRect(
    viewW: Float,
    viewH: Float
): Rect {
    val viewAspect = viewW / viewH

    return if (viewAspect > PREVIEW_ASPECT_RATIO) {
        val contentH = viewH
        val contentW = contentH * PREVIEW_ASPECT_RATIO
        val left = (viewW - contentW) / 2f

        Rect(
            left = left,
            top = 0f,
            right = left + contentW,
            bottom = contentH
        )
    } else {
        val contentW = viewW
        val contentH = contentW / PREVIEW_ASPECT_RATIO
        val top = (viewH - contentH) / 2f

        Rect(
            left = 0f,
            top = top,
            right = contentW,
            bottom = top + contentH
        )
    }
}

/**
 * CameraGuideOverlay에서 사용하는 손바닥 PNG 가이드의 화면 배치 영역을 계산한다.
 *
 * 반영 사항
 * - 기존보다 약 1.5배 크게 시도
 * - 화면 밖으로 너무 벗어나지 않도록 최대 크기 제한
 * - 기존보다 약간 아래쪽에 배치
 *
 * 주의
 * - Overlay와 Utils가 같은 배치 규칙을 써야 화면 가이드와 crop이 어긋나지 않는다.
 * - 기기 화면이 좁으면 "정확히 1.5배"가 아니라 제한된 최대 크기로 표시될 수 있다.
 */
private fun calculateGuideImageRect(previewRect: Rect): Rect {
    val previewW = previewRect.width
    val previewH = previewRect.height

    val baseGuideSize = previewW * BASE_GUIDE_WIDTH_RATIO
    val scaledGuideSize = baseGuideSize * GUIDE_SCALE

    val maxGuideWidth = previewW * MAX_GUIDE_WIDTH_RATIO
    val maxGuideHeight = previewH * MAX_GUIDE_HEIGHT_RATIO

    val guideSize = scaledGuideSize
        .coerceAtMost(maxGuideWidth)
        .coerceAtMost(maxGuideHeight)

    val guideW = guideSize
    val guideH = guideSize

    val left = previewRect.left + (previewW - guideW) / 2f
    val top = previewRect.top + previewH * GUIDE_TOP_OFFSET_RATIO

    return Rect(
        left = left,
        top = top,
        right = left + guideW,
        bottom = top + guideH
    )
}

/**
 * 손바닥 PNG 안에서 실제 손 모양이 차지하는 비율
 *
 * 설명
 * - PNG는 투명 여백을 포함한 정사각형 이미지다
 * - cropRect는 PNG 전체가 아니라, 실제 손 외곽선이 자리한 영역을 기준으로 잡아야 한다
 */
private object GuideImageHandBounds {
    const val LEFT = 0.15f
    const val TOP = 0.08f
    const val RIGHT = 0.82f
    const val BOTTOM = 0.89f
}

fun calculateGuideSpec(
    viewW: Float,
    viewH: Float
): GuideSpec {
    val previewRect = calculatePreviewContentRect(viewW, viewH)
    val guideImageRect = calculateGuideImageRect(previewRect)

    val guideW = guideImageRect.width
    val guideH = guideImageRect.height

    val handLeft = guideImageRect.left + guideW * GuideImageHandBounds.LEFT
    val handTop = guideImageRect.top + guideH * GuideImageHandBounds.TOP
    val handRight = guideImageRect.left + guideW * GuideImageHandBounds.RIGHT
    val handBottom = guideImageRect.top + guideH * GuideImageHandBounds.BOTTOM

    val handWidth = handRight - handLeft
    val handHeight = handBottom - handTop

    val cropLeft = (handLeft - handWidth * 0.08f).coerceAtLeast(previewRect.left)
    val cropTop = (handTop - handHeight * 0.05f).coerceAtLeast(previewRect.top)
    val cropRight = (handRight + handWidth * 0.08f).coerceAtMost(previewRect.right)
    val cropBottom = (handBottom + handHeight * 0.10f).coerceAtMost(previewRect.bottom)

    val cropRect = Rect(
        left = cropLeft,
        top = cropTop,
        right = cropRight,
        bottom = cropBottom
    )

    return GuideSpec(
        previewRect = previewRect,
        guideImageRect = guideImageRect,
        cropRect = cropRect
    )
}

fun cropBitmapByGuideRect(
    bitmap: Bitmap,
    viewW: Float,
    viewH: Float
): Bitmap {
    val spec = calculateGuideSpec(viewW, viewH)
    val previewRect = spec.previewRect
    val cropRect = spec.cropRect

    val previewW = previewRect.width
    val previewH = previewRect.height

    val leftN = ((cropRect.left - previewRect.left) / previewW).coerceIn(0f, 1f)
    val topN = ((cropRect.top - previewRect.top) / previewH).coerceIn(0f, 1f)
    val rightN = ((cropRect.right - previewRect.left) / previewW).coerceIn(0f, 1f)
    val bottomN = ((cropRect.bottom - previewRect.top) / previewH).coerceIn(0f, 1f)

    val x = (leftN * bitmap.width).roundToInt().coerceIn(0, bitmap.width - 1)
    val y = (topN * bitmap.height).roundToInt().coerceIn(0, bitmap.height - 1)

    val width = ((rightN - leftN) * bitmap.width).roundToInt().coerceAtLeast(1)
    val height = ((bottomN - topN) * bitmap.height).roundToInt().coerceAtLeast(1)

    val safeWidth = (x + width).coerceAtMost(bitmap.width) - x
    val safeHeight = (y + height).coerceAtMost(bitmap.height) - y

    return Bitmap.createBitmap(
        bitmap,
        x,
        y,
        safeWidth,
        safeHeight
    )
}