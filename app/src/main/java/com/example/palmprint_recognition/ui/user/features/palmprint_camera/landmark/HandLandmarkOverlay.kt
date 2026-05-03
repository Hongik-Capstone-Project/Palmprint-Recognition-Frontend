package com.example.palmprint_recognition.ui.user.features.palmprint_camera.landmark

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.guide.calculateGuideSpec

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
 * - MediaPipe가 찾은 손 점을 화면 좌표로 변환한다.
 * - 손 bounding box와 guide cropRect를 계산한다.
 * - 디버그 모드일 때만 랜드마크 점/선/박스를 화면에 표시한다.
 *
 * 중요
 * - isDebugVisible은 표시 여부만 제어한다.
 * - landmark 기반 조건 계산을 위해 onHandRectChanged는 항상 호출한다.
 *
 * @param landmarks 손 랜드마크 목록
 * @param modifier Compose Modifier
 * @param isDebugVisible 디버그 오버레이 표시 여부
 * @param onHandRectChanged 손 bounding box와 guide cropRect 전달 콜백
 */
@Composable
fun HandLandmarkOverlay(
    landmarks: List<HandLandmarkPoint>,
    modifier: Modifier = Modifier,
    isDebugVisible: Boolean = false,
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

        if (!isDebugVisible) {
            return@Canvas
        }

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