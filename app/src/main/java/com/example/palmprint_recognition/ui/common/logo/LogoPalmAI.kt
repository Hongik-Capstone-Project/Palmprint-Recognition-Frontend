package com.example.palmprint_recognition.ui.common.logo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.R

@Composable
fun LogoPalmAI(
    modifier: Modifier = Modifier,    // ✅ [유지] 상대 위치 제어
    width: Dp = 186.dp                // ✅ [추가] 크기 변경 가능
) {
    Image(
        painter = painterResource(id = R.drawable.logo_palm_ai),
        contentDescription = "Palm AI Logo",
        modifier = modifier.width(width),          // ✅ [수정]
        contentScale = ContentScale.Fit            // ✅ [추가] 비율 유지
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLogoPalmAI() {
    LogoPalmAI(width = 140.dp)
}
