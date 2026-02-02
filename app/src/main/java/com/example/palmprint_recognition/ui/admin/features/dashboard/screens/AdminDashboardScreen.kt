package com.example.palmprint_recognition.ui.admin.features.dashboard.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.admin.features.dashboard.components.DashboardFooterButtonsSection
import com.example.palmprint_recognition.ui.admin.features.dashboard.components.DashboardManagementSection
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable

@Composable
fun AdminDashboardScreen(
    onUserManageClick: () -> Unit,
    onDeviceManageClick: () -> Unit,
    onReportManageClick: () -> Unit,
    onVerificationClick: () -> Unit,
    onLogoutClick: () -> Unit        // 추가
) {
    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = { HeaderContainer() },
        body = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                DashboardManagementSection(
                    onUserManageClick = onUserManageClick,
                    onDeviceManageClick = onDeviceManageClick,
                    onReportManageClick = onReportManageClick
                )
            }
        },
        footer = {
            Footer {
                DashboardFooterButtonsSection(
                    onVerificationClick = onVerificationClick,
                    onLogoutClick = onLogoutClick   // 이제 navigate만
                )
            }
        }
    )
}
