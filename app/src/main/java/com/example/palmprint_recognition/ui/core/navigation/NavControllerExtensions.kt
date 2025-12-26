package com.example.palmprint_recognition.ui.core.navigation

import androidx.navigation.NavController

/**
 * NavController 확장 함수
 *
 * - 특정 route까지 popUpTo 하면서 이동
 * - 삭제 / 저장 후 목록 화면으로 돌아가는 케이스에 주로 사용
 */
fun NavController.navigateAndClearUpTo(
    destination: String,
    popUpToRoute: String,
    inclusive: Boolean = true
) {
    navigate(destination) {
        popUpTo(popUpToRoute) {
            this.inclusive = inclusive
        }
        launchSingleTop = true
    }
}
