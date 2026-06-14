package com.example.palmprint_recognition.ui.demo.features.home.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable
import com.example.palmprint_recognition.ui.user.features.user_main.components.MainFooterButtonsSection
import com.example.palmprint_recognition.ui.user.features.user_main.components.MainManagementSection

@Composable
fun DemoHomeScreen(
    onRegisterClick: () -> Unit,
    onVerifyClick: () -> Unit,
    onGuideClick: () -> Unit
) {
    val demoUserName = "홍길동"

    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = {
            HeaderContainer()
        },
        body = {
            MainManagementSection(
                userName = demoUserName,
                palmSubtitle = "손바닥 이미지를 등록하고 인증해보세요.",

                // 데모에서는 서버 통신/화면 이동 없음
                onInstitutionManageClick = {
                    // 서버 통신 방지: 데모에서는 동작하지 않음
                },
                onPaymentManageClick = {
                    // 서버 통신 방지: 데모에서는 동작하지 않음
                },

                // 손바닥 등록은 데모 등록 화면으로 이동
                onRegisterPalmprintClick = onRegisterClick,

                // UI 텍스트는 "손바닥 삭제" 그대로지만,
                // 데모에서는 삭제 대신 인증 화면으로 이동
                onDeletePalmprintClick = onVerifyClick,

                // 인증 내역 조회도 서버 통신 방지
                onMyVerificationClick = {
                    // 서버 통신 방지: 데모에서는 동작하지 않음
                }
            )
        },
        footer = {
            Footer {
                MainFooterButtonsSection(
                    onLogoutClick = {
                        // 서버 통신 방지: 데모에서는 동작하지 않음
                    },
                    onSignOutClick = {
                        // 서버 통신 방지: 데모에서는 동작하지 않음
                    }
                )
            }
        }
    )
}