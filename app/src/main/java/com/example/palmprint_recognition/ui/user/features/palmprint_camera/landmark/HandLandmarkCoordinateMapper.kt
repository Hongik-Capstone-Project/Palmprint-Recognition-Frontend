package com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

/**
 * 손 랜드마크 좌표 변환 유틸리티
 *
 * 역할
 * - MediaPipe의 정규화 좌표를 Compose 화면 좌표로 변환한다
 * - Camera preview가 실제로 표시되는 previewRect 기준으로 좌표를 맞춘다
 */

/**
 * 정규화된 랜드마크 좌표를 화면 좌표로 변환한다.
 *
 * @param landmark MediaPipe 손 랜드마크 점
 * @param previewRect 실제 카메라 프리뷰가 표시되는 화면 영역
 * @return Compose Canvas에 그릴 수 있는 좌표
 */
fun mapHandLandmarkToPreviewOffset(
    landmark: HandLandmarkPoint,
    previewRect: Rect
): Offset {
    val x = previewRect.left + landmark.x * previewRect.width
    val y = previewRect.top + landmark.y * previewRect.height

    return Offset(
        x = x,
        y = y
    )
}

/**
 * 랜드마크 목록을 화면 좌표 목록으로 변환한다.
 *
 * @param landmarks MediaPipe 손 랜드마크 목록
 * @param previewRect 실제 카메라 프리뷰가 표시되는 화면 영역
 * @return 화면 좌표 목록
 */
fun mapHandLandmarksToPreviewOffsets(
    landmarks: List<HandLandmarkPoint>,
    previewRect: Rect
): List<Offset> {
    return landmarks.map { landmark ->
        mapHandLandmarkToPreviewOffset(
            landmark = landmark,
            previewRect = previewRect
        )
    }
}