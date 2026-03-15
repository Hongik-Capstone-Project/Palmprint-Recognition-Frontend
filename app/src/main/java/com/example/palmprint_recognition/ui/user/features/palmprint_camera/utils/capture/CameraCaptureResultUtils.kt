package com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.capture

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraCapturedResult
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.status.CameraGuideDebugState
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.CameraConditionConfig
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.analysis.analyzeCapturedBitmap
import com.example.palmprint_recognition.ui.user.features.palmprint_camera.utils.guide.cropBitmapByGuideRect

/**
 * 촬영 성공 후 처리 결과
 *
 * @property capturedResult 최종 촬영 결과
 * @property updatedDebugState 갱신된 디버그 상태
 * @property logMessage 로그 출력용 메시지
 */
data class CameraCaptureSuccessResult(
    val capturedResult: CameraCapturedResult,
    val updatedDebugState: CameraGuideDebugState,
    val logMessage: String
)

/**
 * 촬영된 원본 Bitmap을 후처리하여 최종 결과를 만든다.
 *
 * 처리 순서
 * - guide 기준 crop
 * - crop 결과 분석
 * - debug 상태 갱신
 * - 로그 메시지 생성
 *
 * @param originalBitmap 촬영 원본 Bitmap
 * @param previewWidth 프리뷰 너비
 * @param previewHeight 프리뷰 높이
 * @param debugState 현재 디버그 상태
 * @param conditionConfig 조건 활성화 설정
 * @return 후처리 결과
 */
fun handleCapturedBitmap(
    originalBitmap: Bitmap,
    previewWidth: Float,
    previewHeight: Float,
    debugState: CameraGuideDebugState,
    conditionConfig: CameraConditionConfig
): CameraCaptureSuccessResult {
    val croppedBitmap = cropBitmapByGuideRect(
        bitmap = originalBitmap,
        viewW = previewWidth,
        viewH = previewHeight
    )

    val analysisState = analyzeCapturedBitmap(
        originalBitmap = originalBitmap,
        croppedBitmap = croppedBitmap,
        conditionConfig = conditionConfig
    )

    val updatedDebugState = debugState.addRatio(analysisState.ratio)

    val capturedResult = CameraCapturedResult(
        originalBitmap = originalBitmap,
        croppedBitmap = croppedBitmap,
        analysisState = analysisState
    )

    val logMessage = buildPalmCropLogMessage(
        previewWidth = previewWidth,
        previewHeight = previewHeight,
        result = capturedResult
    )

    return CameraCaptureSuccessResult(
        capturedResult = capturedResult,
        updatedDebugState = updatedDebugState,
        logMessage = logMessage
    )
}

/**
 * 테스트용 원본 / crop 이미지를 저장한다.
 *
 * @param context Context
 * @param result 촬영 결과
 */
fun saveCapturedBitmapsForTest(
    context: Context,
    result: CameraCapturedResult
) {
    val timestamp = System.currentTimeMillis()

    val rawSaved = saveBitmapToGallery(
        context = context,
        bitmap = result.originalBitmap,
        fileName = "palm_raw_$timestamp.jpg"
    )

    val cropSaved = saveBitmapToGallery(
        context = context,
        bitmap = result.croppedBitmap,
        fileName = "palm_crop_$timestamp.jpg"
    )

    Log.d(
        "PalmSave",
        "rawSaved=$rawSaved, cropSaved=$cropSaved"
    )
}

/**
 * 촬영 결과 로그 메시지를 생성한다.
 *
 * @param previewWidth 프리뷰 너비
 * @param previewHeight 프리뷰 높이
 * @param result 촬영 결과
 * @return 로그 문자열
 */
fun buildPalmCropLogMessage(
    previewWidth: Float,
    previewHeight: Float,
    result: CameraCapturedResult
): String {
    return "preview=${previewWidth}x${previewHeight}, " +
            "original=${result.originalBitmap.width}x${result.originalBitmap.height}, " +
            "cropped=${result.croppedBitmap.width}x${result.croppedBitmap.height}, " +
            "ratio=${result.analysisState.ratio}, " +
            "blur=${result.analysisState.blurScore}, " +
            "tilt=${result.analysisState.tiltScore}, " +
            "condition=${result.analysisState.condition}"
}