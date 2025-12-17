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
    userRole: String? = null, // ✅ 필요하면 사용
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Logo(width = 180.dp)

        // 역할 표시가 필요하면 name에 붙이거나, ProfileCard를 확장하세요.
        val displayName = if (!userRole.isNullOrBlank()) "$userName ($userRole)" else userName

        ProfileCard(
            name = displayName,
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
        userEmail = "email",
        userRole = "ADMIN"
    )
}
