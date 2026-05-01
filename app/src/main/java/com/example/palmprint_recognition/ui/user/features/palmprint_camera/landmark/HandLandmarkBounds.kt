package com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

/**
 * 손 랜드마크 영역 계산 유틸리티
 *
 * 역할
 * - 화면 좌표로 변환된 손 랜드마크 목록을 감싸는 bounding box를 계산한다
 */

/**
 * 손 랜드마크 목록을 감싸는 Rect를 계산한다.
 *
 * @param offsets 화면 좌표로 변환된 손 랜드마크 목록
 * @return 손 영역 Rect, 랜드마크가 없으면 null
 */
fun calculateHandBoundingRect(
    offsets: List<Offset>
): Rect? {
    if (offsets.isEmpty()) {
        return null
    }

    val minX = offsets.minOf { it.x }
    val maxX = offsets.maxOf { it.x }
    val minY = offsets.minOf { it.y }
    val maxY = offsets.maxOf { it.y }

    return Rect(
        left = minX,
        top = minY,
        right = maxX,
        bottom = maxY
    )
}