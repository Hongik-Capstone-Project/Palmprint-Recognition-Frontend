package com.example.palmprint_recognition.ui.common.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RootLayout(
    modifier: Modifier = Modifier,

    headerWeight: Float = 0f,
    bodyWeight: Float = 1f,
    footerWeight: Float = 0f,

    sectionGapWeight: Float = 0.05f,  // ✅ 전체 화면 기준 간격 비율

    header: (@Composable () -> Unit)? = null,
    body: @Composable () -> Unit,
    footer: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        if (header != null && headerWeight > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(headerWeight)
            ) {
                header()
            }

            // Header → Body 간격
            Spacer(modifier = Modifier.weight(sectionGapWeight))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(bodyWeight)
        ) {
            body()
        }

        if (footer != null && footerWeight > 0f) {

            // Body → Footer 간격
            Spacer(modifier = Modifier.weight(sectionGapWeight))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(footerWeight)
            ) {
                footer()
            }

            // Footer → 화면 하단 간격
            Spacer(modifier = Modifier.weight(sectionGapWeight))
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewPalmRootLayout_PopupStyle() {
    RootLayout(
        header = null,
        body = { Box(modifier = Modifier.fillMaxSize()) },
        footer = null
    )
}
