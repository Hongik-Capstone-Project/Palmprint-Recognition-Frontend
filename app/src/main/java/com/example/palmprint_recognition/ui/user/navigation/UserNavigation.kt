package com.example.palmprint_recognition.ui.user.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.user.features.user_main.screens.UserMainScreen

/**
 * ============================================================================
 * User Navigation Graph (일반 사용자 네비게이션)
 * ============================================================================
 *
 * - AppNavHost → userGraph("user_root") 형태로 호출됨
 * - 사용자 전용 화면(메인, 회원탈퇴 등)을 이 안에서 관리
 */
fun NavGraphBuilder.userGraph(
    navController: NavHostController,
    route: String,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = UserRoutes.MAIN,
        route = route
    ) {
        /**
         * --------------------------------------------------------------------
         * 1️. 사용자 메인 화면
         * --------------------------------------------------------------------
         */
        composable(UserRoutes.MAIN) {
            UserMainScreen(
                onInstitutionManageClick = { navController.navigate(UserRoutes.MAIN) },
                onPaymentManageClick = { navController.navigate(UserRoutes.MAIN) },
                onRegisterPalmprintClick = { navController.navigate(UserRoutes.MAIN) },
                onDeletePalmprintClick = { navController.navigate(UserRoutes.MAIN) },
                onMyVerificationClick = { navController.navigate(UserRoutes.MAIN) },
                onHowToUseClick = { navController.navigate(UserRoutes.MAIN) },
                onSignOutClick = { navController.navigate(UserRoutes.MAIN) },
                authViewModel = authViewModel // 전달

            )
        }

//        /**
//         * --------------------------------------------------------------------
//         * 2️. 회원탈퇴 확인 화면
//         * --------------------------------------------------------------------
//         */
//        composable(UserRoutes.DELETE_ACCOUNT) {
//            DeleteAccountScreen(
//                onDeleteSuccess = {
//                    // 회원탈퇴 성공 시 → 앱 전체 스택을 초기화하고 로그인 화면으로 이동
//                    //
//                    // popUpTo(0)의 의미:
//                    //   - back stack 의 인덱스 0(최상단 루트)까지 모두 제거
//                    //   - inclusive = true 이므로 루트까지 전부 비움
//                    //   - 결과적으로 "완전 초기화" 후 Login 화면만 하나 있는 상태가 됨
//                    navController.navigate(AuthRoutes.LOGIN) {
//                        popUpTo(0) {
//                            // true → 루트까지 전부 제거하여 스택을 완전히 비움
//                            inclusive = true
//                        }
//                    }
//                },
//                onCancel = {
//                    // 회원탈퇴 취소 → 이전 화면(user_main)으로 복귀
//                    navController.popBackStack()
//                }
//            )
//        }

        // --------------------------------------------------------------------
        // 추가 예정 화면들 (결제 내역, 기관 리스트, 히스토리 등)
        // --------------------------------------------------------------------
        // composable(UserRoutes.PAYMENT_LIST) { ... }
        // composable(UserRoutes.INSTITUTION_LIST) { ... }
        // composable(UserRoutes.HISTORY_LIST) { ... }
    }
}
