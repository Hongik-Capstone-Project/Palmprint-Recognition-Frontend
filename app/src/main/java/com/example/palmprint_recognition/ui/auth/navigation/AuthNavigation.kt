package com.example.palmprint_recognition.ui.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.palmprint_recognition.ui.auth.features.login.screens.LoginScreen
import com.example.palmprint_recognition.ui.auth.features.sign_up.screens.SignUpScreen

/**
 * ============================================================================
 * Auth Navigation Graph
 * ============================================================================
 *
 * - 로그인(LoginScreen)
 * - 회원가입(SignUpScreen)
 *
 *   로그인 성공 후 화면 전환은 여기서 하지 않습니다.
 *    AuthViewModel의 authState가 바뀌면 AppNavHost가 자동으로 분기합니다.
 */
fun NavGraphBuilder.authGraph(
    navController: NavController
) {
    // 1) 로그인 화면
    composable(AuthRoutes.LOGIN) {
        LoginScreen(
            onSignUpClick = { navController.navigate(AuthRoutes.SIGN_UP) }
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
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
}
