package com.example.palmprint_recognition.ui.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.auth.features.login.screens.LoginScreen
import com.example.palmprint_recognition.ui.auth.features.sign_up.screens.SignUpScreen

/**
 * =====================================================================
 * Auth Navigation Graph
 * =====================================================================
 *
 * - 로그인(LoginScreen)
 * - 회원가입(SignUpScreen)
 * - 로그아웃(LogoutScreen)
 *
 * 로그인/로그아웃 성공 후 화면 전환은 여기서 직접 하지 않습니다.
 * → AuthViewModel.authState 변경을 AppNavHost가 감지해서 자동 분기합니다.
 */
fun NavGraphBuilder.authGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    // 1) 로그인 화면
    composable(AuthRoutes.LOGIN) {
        LoginScreen(
            onSignUpClick = { navController.navigate(AuthRoutes.SIGN_UP) },
            authViewModel = authViewModel
        )
    }

    // 2) 회원가입 화면
    composable(AuthRoutes.SIGN_UP) {
        SignUpScreen(
            onSignUpClick = {
                navController.navigate(AuthRoutes.LOGIN) {
                    popUpTo(AuthRoutes.LOGIN) { inclusive = true }
                }
            },
            onBackClick = { navController.popBackStack() }
        )
    }

}
