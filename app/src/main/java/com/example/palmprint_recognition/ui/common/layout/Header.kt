package com.example.palmprint_recognition.ui.common.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.common.logo.Logo
import com.example.palmprint_recognition.ui.common.card.ProfileCard

@Composable
fun Header(
    userName: String,
    userEmail: String,

    modifier: Modifier = Modifier // 상대 위치 제어
) {

    // Row → Column 으로 변경 (상단: 로고 / 하단: 프로필)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),

        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {


        Logo(
            width = 180.dp
        )

        // 하단 프로필 카드
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
    Header(
        userName = "OO",
        userEmail = "email"
    )
}
