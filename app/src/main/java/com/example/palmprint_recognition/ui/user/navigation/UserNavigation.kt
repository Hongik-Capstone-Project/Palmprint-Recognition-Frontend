package com.example.palmprint_recognition.ui.user.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.palmprint_recognition.ui.auth.AuthRoutes
import com.example.palmprint_recognition.ui.user.main.DeleteAccountScreen
import com.example.palmprint_recognition.ui.user.main.MainScreen

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
    route: String
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
            MainScreen(
                onLogoutClick = {
                    // 로그아웃 시 → 로그인 화면으로 이동
                    // popUpTo(route)의 의미:
                    //   - route 는 userGraph 의 루트("user_root")를 의미
                    //   - user_root 그래프 전체를 스택에서 제거하여
                    //     뒤로가기 했을 때 다시 유저 화면으로 돌아오지 않도록 함
                    navController.navigate(AuthRoutes.LOGIN) {
                        popUpTo(route) {
                            // true → route("user_root")까지 포함해서 제거
                            // 결과: 유저 네비게이션 그래프가 스택에서 완전히 제거됨
                            inclusive = true
                        }
                    }
                },
                onDeleteAccountClick = {
                    // 회원탈퇴 버튼 클릭 시:
                    //   - 여기서는 popUpTo를 사용하지 않고
                    //   - 현재 스택 위에 DeleteAccountScreen 만 하나 더 쌓는다.
                    //
                    //   스택 예시:
                    //     ... → user_root → user_main → delete_account
                    //   이 상태에서 onCancel 시 popBackStack() 하면
                    //   자연스럽게 user_main 으로 돌아갈 수 있다.
                    navController.navigate(UserRoutes.DELETE_ACCOUNT)
                }
            )
        }

        /**
         * --------------------------------------------------------------------
         * 2️. 회원탈퇴 확인 화면
         * --------------------------------------------------------------------
         */
        composable(UserRoutes.DELETE_ACCOUNT) {
            DeleteAccountScreen(
                onDeleteSuccess = {
                    // 회원탈퇴 성공 시 → 앱 전체 스택을 초기화하고 로그인 화면으로 이동
                    //
                    // popUpTo(0)의 의미:
                    //   - back stack 의 인덱스 0(최상단 루트)까지 모두 제거
                    //   - inclusive = true 이므로 루트까지 전부 비움
                    //   - 결과적으로 "완전 초기화" 후 Login 화면만 하나 있는 상태가 됨
                    navController.navigate(AuthRoutes.LOGIN) {
                        popUpTo(0) {
                            // true → 루트까지 전부 제거하여 스택을 완전히 비움
                            inclusive = true
                        }
                    }
                },
                onCancel = {
                    // 회원탈퇴 취소 → 이전 화면(user_main)으로 복귀
                    navController.popBackStack()
                }
            )
        }

        // --------------------------------------------------------------------
        // 추가 예정 화면들 (결제 내역, 기관 리스트, 히스토리 등)
        // --------------------------------------------------------------------
        // composable(UserRoutes.PAYMENT_LIST) { ... }
        // composable(UserRoutes.INSTITUTION_LIST) { ... }
        // composable(UserRoutes.HISTORY_LIST) { ... }
    }
}
