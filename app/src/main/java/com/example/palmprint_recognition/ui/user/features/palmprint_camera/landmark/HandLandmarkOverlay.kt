package com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.guide.calculateGuideSpec
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Rect

private val HAND_CONNECTIONS = listOf(
    0 to 1,
    1 to 2,
    2 to 3,
    3 to 4,
    0 to 5,
    5 to 6,
    6 to 7,
    7 to 8,
    0 to 9,
    9 to 10,
    10 to 11,
    11 to 12,
    0 to 13,
    13 to 14,
    14 to 15,
    15 to 16,
    0 to 17,
    17 to 18,
    18 to 19,
    19 to 20,
    5 to 9,
    9 to 13,
    13 to 17
)

/**
 * 손 랜드마크를 카메라 화면 위에 표시한다.
 *
 * 역할
 * - MediaPipe가 찾은 손 점을 Canvas에 그린다
 * - Camera preview가 실제 표시되는 previewRect 기준으로 좌표를 보정한다
 *
 * 노란색: 실제 손 랜드마크 bounding box
 * 하늘색: 가이드 cropRect
 * 흰색: 기존 손바닥 guide 이미지
 *
 * @param landmarks 손 랜드마크 목록
 * @param modifier Compose Modifier
 */
@Composable
fun HandLandmarkOverlay(
    landmarks: List<HandLandmarkPoint>,
    modifier: Modifier = Modifier,
    onHandRectChanged: (handRect: Rect?, guideRect: Rect) -> Unit = { _, _ -> }
) {
    Canvas(modifier = modifier) {
        val guideSpec = calculateGuideSpec(
            viewW = size.width,
            viewH = size.height
        )

        val previewRect = guideSpec.previewRect

        val landmarkOffsets = mapHandLandmarksToPreviewOffsets(
            landmarks = landmarks,
            previewRect = previewRect
        )

        val handBoundingRect = calculateHandBoundingRect(
            offsets = landmarkOffsets
        )

        onHandRectChanged(
            handBoundingRect,
            guideSpec.cropRect
        )

        HAND_CONNECTIONS.forEach { connection ->
            val startPoint = landmarks.getOrNull(connection.first)
            val endPoint = landmarks.getOrNull(connection.second)

            if (startPoint != null && endPoint != null) {
                drawLine(
                    color = Color.Green,
                    start = mapHandLandmarkToPreviewOffset(
                        landmark = startPoint,
                        previewRect = previewRect
                    ),
                    end = mapHandLandmarkToPreviewOffset(
                        landmark = endPoint,
                        previewRect = previewRect
                    ),
                    strokeWidth = 3.dp.toPx()
                )
            }
        }

        landmarks.forEach { landmark ->
            drawCircle(
                color = Color.Red,
                radius = 5.dp.toPx(),
                center = mapHandLandmarkToPreviewOffset(
                    landmark = landmark,
                    previewRect = previewRect
                )
            )
        }

        handBoundingRect?.let { rect ->
            drawRect(
                color = Color.Yellow,
                topLeft = rect.topLeft,
                size = rect.size,
                style = Stroke(width = 4.dp.toPx())
            )
        }

        drawRect(
            color = Color.Cyan,
            topLeft = guideSpec.cropRect.topLeft,
            size = guideSpec.cropRect.size,
            style = Stroke(width = 4.dp.toPx())
        )
    }

}