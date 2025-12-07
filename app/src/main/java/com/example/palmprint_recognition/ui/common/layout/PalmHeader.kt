package com.example.palmprint_recognition.ui.common.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.common.logo.LogoPalmAI
import com.example.palmprint_recognition.ui.common.card.ProfileCard

@Composable
fun PalmHeader(
    userName: String,
    userEmail: String,

    modifier: Modifier = Modifier // ✅ [유지] 상대 위치 제어
) {

    // ✅ [수정] Row → Column 으로 변경 (상단: 로고 / 하단: 프로필)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),

        horizontalAlignment = Alignment.Start,          // ✅ [수정] 좌측 정렬
        verticalArrangement = Arrangement.spacedBy(10.dp) // ✅ [수정] 로고-프로필 간격 10dp
    ) {

        // ✅ 상단 로고
        LogoPalmAI(
            width = 180.dp // ✅ [유지] CSS 비율 기반 크기
        )

        // ✅ 하단 프로필 카드
        ProfileCard(
            name = userName,
            email = userEmail,
            width = 368.dp,
            height = 48.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPalmHeader() {
    PalmHeader(
        userName = "OO",
        userEmail = "email"
    )
}
