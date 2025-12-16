package com.example.palmprint_recognition.ui.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.palmprint_recognition.ui.auth.screens.LoginScreen
import com.example.palmprint_recognition.ui.auth.screens.SignUpScreen

/**
 * ============================================================================
 * Auth Navigation Graph
 * ============================================================================
 *
 * - 로그인 화면(LoginScreen)
 * - 회원가입 화면(SignUpScreen)
 *
 * 로그인 성공 시 onLoginCompleted(role)을 호출하여
 * 상위(AppNavHost)에서 role에 따라 관리자/유저 메인으로 분기합니다.
 */
fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginCompleted: (role: String) -> Unit
) {
    /**
     * --------------------------------------------------------------
     * 1. 로그인 화면 (Admin/User 공용)
     * --------------------------------------------------------------
     */
    composable(AuthRoutes.LOGIN) {
        LoginScreen(
            onLoginSuccess = {
            },
            role = TODO(),
            onBack = TODO(),
            viewModel = TODO()
        )
    }

    /**
     * --------------------------------------------------------------
     * 2. 회원가입 화면
     * --------------------------------------------------------------
     */
    composable(AuthRoutes.SIGN_UP) {
        SignUpScreen(
            onSignUpSuccess = {
                // 회원가입 성공 후 → 로그인 화면으로 이동
                // popUpTo(AuthRoutes.LOGIN) 을 사용하는 이유:
                //   - 스택에 이전 Login 화면이 있을 수 있음 (Login → SignUp)
                //   - inclusive = true 로 설정하면,
                //     기존 Login + SignUp 을 모두 지우고
                //     새 Login 화면만 스택에 하나만 남도록 함
                navController.navigate(AuthRoutes.LOGIN) {
                    popUpTo(AuthRoutes.LOGIN) {
                        // true → AuthRoutes.LOGIN 목적지까지 "포함해서" 모두 제거
                        // 최종 스택 상태: Login 화면 하나만 존재
                        inclusive = true
                    }
                }
            },
            onBack = {
                // 회원가입 화면에서 뒤로가기 → 이전 화면(Login)으로 돌아감
                navController.popBackStack()
            }
        )
    }
}
