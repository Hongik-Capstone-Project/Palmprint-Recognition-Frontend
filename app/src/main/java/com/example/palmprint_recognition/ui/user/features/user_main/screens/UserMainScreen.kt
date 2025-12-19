package com.example.palmprint_recognition.ui.user.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.common.button.PrimaryButton
import com.example.palmprint_recognition.ui.common.button.VerticalTwoButtons
import com.example.palmprint_recognition.ui.common.layout.Footer
import com.example.palmprint_recognition.ui.common.layout.Header
import com.example.palmprint_recognition.ui.common.layout.HeaderContainer
import com.example.palmprint_recognition.ui.common.layout.RootLayoutScrollable

/**
 * 사용자 메인 화면 (스크롤 버전)
 */
@Composable
fun UserMainScreen(
    onInstitutionManageClick: () -> Unit,
    onPaymentManageClick: () -> Unit,
    onRegisterPalmprintClick: () -> Unit,
    onDeletePalmprintClick: () -> Unit,
    onMyVerificationClick: () -> Unit,
    onHowToUseClick: () -> Unit,
    onSignOutClick: () -> Unit,
    authViewModel: AuthViewModel // preview 테스트시 authViewModel: AuthViewModel?
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "로그아웃") },
            text = { Text(text = "정말 로그아웃 하시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        authViewModel.logoutLocal() // preview 테스트시 authViewModel?.logoutLocal()
                    }
                ) { Text("예") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("아니오") }
            }
        )
    }

    RootLayoutScrollable(
        sectionGap = 12.dp,
        header = {
            HeaderContainer()
        },
        body = {
            MainManagementSection(
                onInstitutionManageClick = onInstitutionManageClick,
                onPaymentManageClick = onPaymentManageClick,
                onRegisterPalmprintClick = onRegisterPalmprintClick,
                onDeletePalmprintClick = onDeletePalmprintClick,
                onMyVerificationClick = onMyVerificationClick,
                onHowToUseClick = onHowToUseClick
            )
        },
        footer = {
            Footer {
                MainFooterButtonsSection(
                    onLogoutClick = { showLogoutDialog = true },
                    onSignOutClick = onSignOutClick
                )
            }
        }
    )
}

@Composable
fun MainManagementSection(
    onInstitutionManageClick: () -> Unit,
    onPaymentManageClick: () -> Unit,
    onRegisterPalmprintClick: () -> Unit,
    onDeletePalmprintClick: () -> Unit,
    onMyVerificationClick: () -> Unit,
    onHowToUseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth() // 스크롤 환경에서는 fillMaxSize()보다 안전
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        InfoCard(
            title = "oo님, 환영합니다!",
            subtitle = "oo님의 인증 정보를 추가하고 어디서든 손쉽게 Palm 인증을 사용하세요.",
            leftButtonText = "인증 기관 관리",
            rightButtonText = "결제 수단 관리",
            onLeftClick = onInstitutionManageClick,
            onRightClick = onPaymentManageClick
        )

        Spacer(modifier = Modifier.height(18.dp))

        InfoCard(
            title = "손바닥 관리",
            subtitle = "현재 손바닥이 정상적으로 등록되어있어요.",
            leftButtonText = "손바닥 등록",
            rightButtonText = "손바닥 삭제",
            onLeftClick = onRegisterPalmprintClick,
            onRightClick = onDeletePalmprintClick
        )

        Spacer(modifier = Modifier.height(24.dp))

        VerticalTwoButtons(
            firstText = "인증 내역 조회하기",
            secondText = "MODEN AI 사용설명",
            onFirstClick = onMyVerificationClick,
            onSecondClick = onHowToUseClick,
            width = null,
            height = 56.dp,
            backgroundColor = Color(0xFFC1C7CD),
            borderColor = Color(0xFFC1C7CD),
            textColor = Color.White,
            textSize = 20
        )

        // 스크롤 버전에서는 weight로 "바닥 밀기" 불필요
        Spacer(modifier = Modifier.height(3.dp))
    }
}

@Composable
fun MainFooterButtonsSection(
    onLogoutClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp), // footer 안에서 좌우 여백 맞추기
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        PrimaryButton(
            text = "로그아웃",
            onClick = onLogoutClick,
            modifier = Modifier.weight(1f),
            width = null,
            height = 56.dp,
            backgroundColor = Color(0xFFF5F5F5),
            borderColor = Color(0xFFC1C7CD),
            textColor = Color.Black,
            textSize = 17
        )

        PrimaryButton(
            text = "회원탈퇴",
            onClick = onSignOutClick,
            modifier = Modifier.weight(1f),
            width = null,
            height = 56.dp,
            backgroundColor = Color(0xFFF5F5F5),
            borderColor = Color(0xFFC1C7CD),
            textColor = Color.Black,
            textSize = 17
        )
    }
}

/**
 * 카드 박스(테두리) + 제목/설명 + 버튼 2개
 */
@Composable
private fun InfoCard(
    title: String,
    subtitle: String,
    leftButtonText: String,
    rightButtonText: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF796E6E))
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF21272A)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = subtitle,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF697077),
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrimaryButton(
                text = leftButtonText,
                onClick = onLeftClick,
                modifier = Modifier.weight(1f),
                width = null,
                height = 65.dp,
                backgroundColor = Color(0xFFDDE1E6),
                borderColor = Color(0xFF697077),
                textColor = Color(0xFF21272A),
                textSize = 14
            )

            PrimaryButton(
                text = rightButtonText,
                onClick = onRightClick,
                modifier = Modifier.weight(1f),
                width = null,
                height = 65.dp,
                backgroundColor = Color(0xFFDDE1E6),
                borderColor = Color(0xFF697077),
                textColor = Color(0xFF21272A),
                textSize = 14
            )
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewUserMainScreen_Scrollable() {
//    UserMainScreen(
//        onInstitutionManageClick = {},
//        onPaymentManageClick = {},
//        onRegisterPalmprintClick = {},
//        onDeletePalmprintClick = {},
//        onMyVerificationClick = {},
//        onHowToUseClick = {},
//        onSignOutClick = {},
//        authViewModel = null
//    )
//}
