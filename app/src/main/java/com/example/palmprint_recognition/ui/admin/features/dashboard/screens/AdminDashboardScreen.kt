package com.example.palmprint_recognition.ui.admin.features.dashboard.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.admin.features.dashboard.components.DashboardFooterButtonsSection
import com.example.palmprint_recognition.ui.admin.features.dashboard.components.DashboardManagementSection
import com.example.palmprint_recognition.ui.common.button.VerticalTwoButtons
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.Header
import com.example.palmprint_recognition.ui.common.layout.RootLayout

@Composable
fun AdminDashboardScreen(
    onUserManageClick: () -> Unit,
    onDeviceManageClick: () -> Unit,
    onReportManageClick: () -> Unit,
    onVerificationClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    RootLayout(
        headerWeight = 2f,
        bodyWeight = 5f,
        footerWeight = 3f,
        sectionGapWeight = 0.4f,

        header = {
            Header(
                userName = "Alice",
                userEmail = "alice@example.com"
            )
        },

        body = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
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
                    onLogoutClick = onLogoutClick
                )
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewAdminDashboardScreen() {
    AdminDashboardScreen(
        onUserManageClick = {},
        onDeviceManageClick = {},
        onReportManageClick = {},
        onVerificationClick = {},
        onLogoutClick = {}
    )
}
