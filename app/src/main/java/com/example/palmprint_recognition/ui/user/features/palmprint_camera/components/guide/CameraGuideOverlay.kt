package com.example.palmprint_recognition.ui.user.features.palmprint_camera.components.guide

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.palmprint_recognition.R
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.guide.calculateGuideSpec
import kotlin.math.roundToInt

/**
 * 손바닥 촬영 가이드라인 오버레이
 *
 * 기능
 * - 투명 PNG 손바닥 외곽 가이드 표시
 *
 * 설계 포인트
 * - CameraGuideUtils의 guideImageRect를 직접 사용한다
 * - 화면 가이드와 crop 계산이 같은 기준을 사용하도록 맞춘다
 * - 가이드는 흰색으로 표시한다
 */
@Composable
fun CameraGuideOverlay(
    modifier: Modifier = Modifier
) {
    val guideImageBitmap = ImageBitmap.imageResource(id = R.drawable.hand_guide)

    Canvas(modifier = modifier) {
        val guideSpec = calculateGuideSpec(
            viewW = size.width,
            viewH = size.height
        )

        val guideImageRect = guideSpec.guideImageRect

        drawImage(
            image = guideImageBitmap,
            dstOffset = IntOffset(
                x = guideImageRect.left.roundToInt(),
                y = guideImageRect.top.roundToInt()
            ),
            dstSize = IntSize(
                width = guideImageRect.width.roundToInt(),
                height = guideImageRect.height.roundToInt()
            ),
            colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.65f))
        )
    }
}
