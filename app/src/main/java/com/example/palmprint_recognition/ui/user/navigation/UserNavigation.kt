package com.example.palmprint_recognition.ui.user.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.palmprint_recognition.ui.user.main.MainScreen
import com.example.palmprint_recognition.ui.auth.AuthRoutes
import com.example.palmprint_recognition.ui.user.main.DeleteAccountScreen


/**
 * ============================================================================
 * User Navigation Graph (일반 사용자 네비게이션)
 * ============================================================================
 *
 * - AppNavHost → userGraph("user_root") 형태로 호출됨
 * - 사용자 전용 화면들(메인, 결제내역, 기관 조회 등)을 모두 이 안에서 관리
 * - 현재는 최소 기능(메인 화면)만 구현
 */
fun NavGraphBuilder.userGraph(
    navController: NavHostController,
    route: String
) {
    navigation(
        startDestination = UserRoutes.MAIN,
        route = route
    ) {

        /**
         * --------------------------------------------------------------------
         * 1️. 사용자 메인 화면
         *  - 사용자가 로그인한 후 처음 보는 메인 페이지
         * --------------------------------------------------------------------
         */
        composable(UserRoutes.MAIN) {
            MainScreen(
                onLogoutClick = {
                    navController.navigate(AuthRoutes.ROLE_SELECTION) {
                        popUpTo(route) { inclusive = true }
                    }
                },

                /** 회원탈퇴 버튼 누르면 DeleteAccountScreen 으로 이동 */
                onDeleteAccountClick = {
                    navController.navigate(UserRoutes.DELETE_ACCOUNT)
                }
            )
        }

        /* --------------------------------------------------------------------
         * 2️. 회원탈퇴 확인 화면
         * -------------------------------------------------------------------- */
        composable(UserRoutes.DELETE_ACCOUNT) {
            DeleteAccountScreen(
                onDeleteSuccess = {
                    navController.navigate(AuthRoutes.ROLE_SELECTION) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }

        /**
         * --------------------------------------------------------------------
         * 3. (추가 예정) 결제 내역 화면
         * --------------------------------------------------------------------
         */
        // composable(UserRoutes.PAYMENT_LIST) { ... }

        /**
         * --------------------------------------------------------------------
         * 4. (추가 예정) 기관 리스트 화면
         * --------------------------------------------------------------------
         */
        // composable(UserRoutes.INSTITUTION_LIST) { ... }

        /**
         * --------------------------------------------------------------------
         * 5. (추가 예정) 히스토리(출입/인증 기록) 화면
         * --------------------------------------------------------------------
         */
        // composable(UserRoutes.HISTORY_LIST) { ... }
    }
}
