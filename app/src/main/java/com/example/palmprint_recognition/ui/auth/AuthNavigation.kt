package com.example.palmprint_recognition.ui.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

/**
 * ============================================================================
 * Auth Navigation Graph
 * 역할 선택 → 로그인 화면
 * ============================================================================
 *
 * - 앱 첫 실행 시 보여주는 구간
 * - RoleSelectionScreen → LoginScreen(USER or ADMIN)
 * - 로그인 성공 시 onLoginCompleted(role) 실행 → 상위 NavHost에서 메인 화면 분기 처리
 */
fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginCompleted: (role: String) -> Unit
) {

    /**
     * ------------------------------------------------------------------------
     * 1. 역할 선택 화면
     * ------------------------------------------------------------------------
     *
     * - 사용자 로그인 선택 시 → Login(USER)
     * - 관리자 로그인 선택 시 → Login(ADMIN)
     */
    composable(AuthRoutes.ROLE_SELECTION) {
        RoleSelectionScreen(
            onUserLoginClick = {
                navController.navigate(AuthRoutes.login("USER"))
            },
            onAdminLoginClick = {
                navController.navigate(AuthRoutes.login("ADMIN"))
            },
            onSignUpClick = {
                navController.navigate(AuthRoutes.SIGN_UP)
            }
        )
    }

    /**
     * ------------------------------------------------------------------------
     * 2. 로그인 화면
     * ------------------------------------------------------------------------
     *
     * route 예시:
     *  - "login/ADMIN"
     *  - "login/USER"
     *
     * role 값에 따라 화면 텍스트가 달라진다.
     * 로그인 성공 시 onLoginCompleted(role) 실행 → 상위 NavHost로 전달
     */
    composable(
        route = AuthRoutes.LOGIN,
        arguments = listOf(
            navArgument("role") { type = NavType.StringType }
        )
    ) { backStackEntry ->

        val role = backStackEntry.arguments?.getString("role") ?: "USER"

        LoginScreen(
            role = role,

            /** 로그인 성공 시 네비게이션 처리 */
            onLoginSuccess = { loggedInRole ->
                onLoginCompleted(loggedInRole)
            },

            /** 뒤로 가기 → RoleSelectionScreen */
            onBack = { navController.popBackStack() }
        )
    }

    // 3. 회원가입 화면
    composable(route = AuthRoutes.SIGN_UP) {
        SignUpScreen(
            onSignUpSuccess = {
                // 회원가입 성공 후 → 사용자 로그인 화면으로 이동 (예시)
                navController.navigate(AuthRoutes.login("USER")) {
                    popUpTo(AuthRoutes.ROLE_SELECTION) { inclusive = false }
                }
            },
            onBack = { navController.popBackStack() }
        )
    }

}
