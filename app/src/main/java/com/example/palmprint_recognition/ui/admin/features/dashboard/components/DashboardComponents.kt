package com.example.palmprint_recognition.ui.admin.features.dashboard.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.common.button.PrimaryButton

@Composable
fun DashboardManagementSection(
    onUserManageClick: () -> Unit,
    onDeviceManageClick: () -> Unit,
    onReportManageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    //타이틀과 박스 사이 "위아래" 여백
    Text(
        text = "정보 관리",
        modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF796E6E))
            .padding(horizontal = 16.dp, vertical = 25.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        DashboardMenuButton(text = "유저 관리", onClick = onUserManageClick)
        DashboardMenuButton(text = "디바이스 관리", onClick = onDeviceManageClick)
        DashboardMenuButton(text = "신고 내역 관리", onClick = onReportManageClick)
    }
}

@Composable
fun DashboardFooterButtonsSection(
    onVerificationClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        PrimaryButton(
            text = "통계 정보 조회",
            onClick = onVerificationClick,
            modifier = Modifier.fillMaxWidth(),
            width = null,
            height = 56.dp,
            backgroundColor = Color(0xFFC1C7CD),
            borderColor = Color(0xFFC1C7CD),
            textColor = Color.White,
            textSize = 20
        )

        PrimaryButton(
            text = "로그아웃",
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth(),
            width = null,
            height = 56.dp,
            backgroundColor = Color(0xF5F5F5),
            borderColor = Color(0xFFC1C7CD),
            textColor = Color.Black,
            textSize = 20
        )
    }
}

@Composable
fun DashboardMenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // CSS 비율: 298 / 356 ≈ 0.837
    // 356 기준으로: 좌 29, 가운데 298, 우 29
    val sideWeight = 29f
    val centerWeight = 298f

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(sideWeight))

        PrimaryButton(
            text = text,
            onClick = onClick,
            modifier = Modifier.weight(centerWeight),
            width = null, // weight 폭 우선
            height = 62.dp,
            backgroundColor = Color(0xFFDDE1E6),
            borderColor = Color(0xFF697077),
            textColor = Color(0xFF21272A),
            textSize = 16
        )

        Spacer(modifier = Modifier.weight(sideWeight))
    }
}
