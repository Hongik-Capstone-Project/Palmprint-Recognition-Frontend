package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.guide

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Rect
import kotlin.math.roundToInt

private const val PREVIEW_ASPECT_RATIO = 3f / 4f

/**
 * 손바닥 촬영 가이드 영역 정보
 *
 * 역할
 * - previewRect: 실제 카메라 프리뷰가 보이는 영역
 * - ovalRect: 사용자에게 보여줄 세로 타원 가이드 영역
 * - cropRect: 서버 전송용 직사각형 crop 영역
 * - wristY: 손목 기준선 y 좌표
 *
 * @property previewRect 실제 프리뷰가 표시되는 영역
 * @property ovalRect 화면에 표시할 타원 가이드 영역
 * @property cropRect 서버 전송용 직사각형 crop 영역
 * @property wristY 손목 기준선 y 좌표
 */
data class GuideSpec(
    val previewRect: Rect,
    val ovalRect: Rect,
    val cropRect: Rect,
    val wristY: Float
)

/**
 * 전체 화면 안에서 실제 카메라 프리뷰가 표시되는 영역을 계산한다.
 *
 * 전제
 * - PreviewView는 FIT_CENTER
 * - CameraX는 4:3 비율을 사용
 * - 세로 화면에서는 실제 프리뷰 비율이 3:4 형태로 보인다
 *
 * @param viewW 전체 뷰 너비
 * @param viewH 전체 뷰 높이
 * @return 실제 프리뷰 표시 영역
 */
private fun calculatePreviewContentRect(
    viewW: Float,
    viewH: Float
): Rect {
    val viewAspect = viewW / viewH

    return if (viewAspect > PREVIEW_ASPECT_RATIO) {
        // 화면이 더 넓은 경우: 좌우 여백
        val contentH = viewH
        val contentW = contentH * PREVIEW_ASPECT_RATIO
        val left = (viewW - contentW) / 2f

        Rect(
            left,
            0f,
            left + contentW,
            contentH
        )
    } else {
        // 화면이 더 높은 경우: 위아래 여백
        val contentW = viewW
        val contentH = contentW / PREVIEW_ASPECT_RATIO
        val top = (viewH - contentH) / 2f

        Rect(
            0f,
            top,
            contentW,
            top + contentH
        )
    }
}

/**
 * 화면 크기에 맞는 손바닥 촬영 가이드 영역을 계산한다.
 *
 * 설계 의도
 * - 타원은 사용자 가이드용으로만 사용한다
 * - 실제 서버 전송 이미지는 cropRect 기준 직사각형 crop을 사용한다
 * - 손가락 끝은 덜 포함하고, 손목은 조금 더 포함하도록 아래쪽 여백을 더 준다
 * - 전체 화면이 아니라 실제 프리뷰 영역 기준으로 계산한다
 *
 * @param viewW 프리뷰 View 너비
 * @param viewH 프리뷰 View 높이
 * @return 손바닥 촬영 가이드 정보
 */
fun calculateGuideSpec(
    viewW: Float,
    viewH: Float
): GuideSpec {
    val previewRect = calculatePreviewContentRect(viewW, viewH)

    val previewW = previewRect.width
    val previewH = previewRect.height

    // -----------------------------
    // 1) 사용자에게 보여줄 타원 가이드
    // -----------------------------
    val ovalW = previewW * 0.70f
    val ovalH = previewH * 0.62f

    val ovalLeft = previewRect.left + (previewW - ovalW) / 2f
    val ovalTop = previewRect.top + previewH * 0.14f

    val ovalRect = Rect(
        ovalLeft,
        ovalTop,
        ovalLeft + ovalW,
        ovalTop + ovalH
    )

    val wristY = ovalRect.bottom + previewH * 0.05f

    // -----------------------------
    // 2) 실제 전송용 직사각형 crop 영역
    // -----------------------------
    // - 좌우는 약간 여유
    // - 위쪽은 손가락 끝 비중을 줄이기 위해 조금 덜 포함
    // - 아래쪽은 손목/하단 손바닥 구조를 위해 더 포함
    val cropLeft = (ovalRect.left - ovalW * 0.06f).coerceAtLeast(previewRect.left)
    val cropRight = (ovalRect.right + ovalW * 0.06f).coerceAtMost(previewRect.right)

    val cropTop = (ovalRect.top + ovalH * 0.06f).coerceAtLeast(previewRect.top)
    val cropBottom = (ovalRect.bottom + ovalH * 0.18f).coerceAtMost(previewRect.bottom)

    val cropRect = Rect(
        cropLeft,
        cropTop,
        cropRight,
        cropBottom
    )

    return GuideSpec(
        previewRect = previewRect,
        ovalRect = ovalRect,
        cropRect = cropRect,
        wristY = wristY
    )
}

/**
 * 프리뷰 기준 cropRect를 실제 Bitmap 좌표로 변환하여 crop한다.
 *
 * 동작
 * - 전체 화면이 아니라 실제 previewRect를 기준으로 정규화한다
 * - previewRect 내부에서의 비율을 Bitmap 좌표로 변환한다
 * - 최종적으로 서버 전송용 직사각형 crop Bitmap을 반환한다
 *
 * 주의
 * - 이 함수는 타원 마스크를 적용하지 않는다
 * - 사용자에게 보여지는 이미지와 서버 전송 이미지가 동일하도록 직사각형 crop만 사용한다
 *
 * @param bitmap 원본 Bitmap
 * @param viewW 프리뷰 View 너비
 * @param viewH 프리뷰 View 높이
 * @return 서버 전송용 직사각형 crop Bitmap
 */
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

    // previewRect 내부 기준으로 정규화
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

