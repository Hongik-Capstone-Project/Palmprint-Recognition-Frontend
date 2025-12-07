package com.example.palmprint_recognition.ui.common.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PalmRootLayout(
    modifier: Modifier = Modifier,

    header: (@Composable () -> Unit)? = null, // ✅ [수정] null 허용 → 팝업 대응
    body: @Composable () -> Unit,
    footer: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        // ✅ Header (있을 때만 표시)
        header?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                it()
            }
        }

        // ✅ Body (항상 남은 영역 차지)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            body()
        }

        // ✅ Footer (있을 때만 표시)
        footer?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                it()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPalmRootLayout_PopupStyle() {
    PalmRootLayout(
        header = null, // ✅ 팝업처럼 헤더 제거
        body = { Box(modifier = Modifier.fillMaxSize()) },
        footer = null
    )
}
