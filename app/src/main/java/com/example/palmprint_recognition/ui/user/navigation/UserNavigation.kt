package com.example.palmprint_recognition.ui.user.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.user.features.user_main.screens.UserMainScreen
import com.example.palmprint_recognition.ui.user.features.institutions.screens.AddInstitutionScreen
import com.example.palmprint_recognition.ui.user.features.institutions.screens.DeleteInstitutionScreen
import com.example.palmprint_recognition.ui.user.features.institutions.screens.InstitutionListScreen
import com.example.palmprint_recognition.ui.user.features.payments.screens.AddPaymentScreen
import com.example.palmprint_recognition.ui.user.features.payments.screens.DeletePaymentScreen
import com.example.palmprint_recognition.ui.user.features.payments.screens.PaymentListScreen
import com.example.palmprint_recognition.data.model.UserVerificationLog
import com.example.palmprint_recognition.ui.user.features.histories.screens.HistoryListScreen
import com.example.palmprint_recognition.ui.user.features.histories.screens.ReportHistoryScreen
import com.example.palmprint_recognition.ui.user.features.palmprint_management.screens.DeletePalmprintScreen
import com.example.palmprint_recognition.ui.user.features.palmprint_management.screens.RegisterPalmprintScreen

/**
 * ============================================================================
 * User Navigation Graph (일반 사용자 네비게이션)
 * ============================================================================
 *
 * - AppNavHost → userGraph("user_root") 형태로 호출됨
 * - 사용자 전용 화면(메인, 내 인증기관 관리 등)을 이 안에서 관리
 */
fun NavGraphBuilder.userGraph(
    navController: NavController,
    route: String,
    authViewModel: AuthViewModel
) {
    navigation(
        startDestination = UserRoutes.MAIN,
        route = route
    ) {
        /**
         * --------------------------------------------------------------------
         * 1) 사용자 메인 화면
         * --------------------------------------------------------------------
         */
        composable(UserRoutes.MAIN) {
            UserMainScreen(
                onInstitutionManageClick = {
                    navController.navigate(UserRoutes.INSTITUTION_LIST)
                },
                onPaymentManageClick = {
                    navController.navigate(UserRoutes.PAYMENT_LIST)
                },
                onRegisterPalmprintClick = {
                    navController.navigate(UserRoutes.REGISTER_PALMPRINT)
                },
                onDeletePalmprintClick = {
                    navController.navigate(UserRoutes.DELETE_PALMPRINT)
                },
                onHistoryClick = {
                    navController.navigate(UserRoutes.HISTORY_LIST)
                },
                onHowToUseClick = { navController.navigate(UserRoutes.MAIN) },
                onSignOutClick = { navController.navigate(UserRoutes.MAIN) },
                authViewModel = authViewModel
            )
        }

        /**
         * --------------------------------------------------------------------
         * 2) 내 인증기관 목록
         * --------------------------------------------------------------------
         */
        composable(UserRoutes.INSTITUTION_LIST) {
            InstitutionListScreen(
                onInstitutionClick = { institutionId ->
                    navController.navigate(UserRoutes.deleteInstitution(institutionId))
                },
                onAddInstitutionClick = {
                    navController.navigate(UserRoutes.ADD_INSTITUTION)
                }
            )
        }

        /**
         * --------------------------------------------------------------------
         * 3) 내 인증기관 추가
         * --------------------------------------------------------------------
         */
        composable(UserRoutes.ADD_INSTITUTION) {
            AddInstitutionScreen(
                onAddSuccess = {
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        /**
         * --------------------------------------------------------------------
         * 4) 내 인증기관 삭제
         * --------------------------------------------------------------------
         */
        composable(
            route = UserRoutes.DELETE_INSTITUTION,
            arguments = listOf(navArgument("institutionId") { type = NavType.IntType })
        ) { entry ->
            val institutionId = entry.arguments?.getInt("institutionId") ?: return@composable

            DeleteInstitutionScreen(
                institutionId = institutionId,
                onConfirmDelete = {
                    // DeleteInstitution 제거하고 InstitutionList로 복귀 (리스트 재조회)
                    navController.popBackStack()
                },
                onCancel = {
                    // 아니오 → 이전 화면(=InstitutionList)로
                    navController.popBackStack()
                }
            )
        }


        /**
         * 결제 수단 목록
         */
        composable(UserRoutes.PAYMENT_LIST) {
            PaymentListScreen(
                onPaymentMethodClick = { paymentMethodId ->
                    navController.navigate(UserRoutes.deletePayment(paymentMethodId))
                },
                onAddPaymentClick = {
                    navController.navigate(UserRoutes.ADD_PAYMENT)
                }
            )
        }

        /**
         * 결제 수단 추가
         */
        composable(UserRoutes.ADD_PAYMENT) {
            AddPaymentScreen(
                onAddSuccess = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        /**
         * 결제 수단 삭제
         */
        composable(
            route = UserRoutes.DELETE_PAYMENT,
            arguments = listOf(navArgument("paymentMethodId") { type = NavType.IntType })
        ) { entry ->
            val paymentMethodId = entry.arguments?.getInt("paymentMethodId") ?: return@composable

            DeletePaymentScreen(
                paymentMethodId = paymentMethodId,
                onConfirmDelete = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        /**
         * 히스토리 목록
         */
        composable(UserRoutes.HISTORY_LIST) {
            HistoryListScreen(
                onHistoryClick = { log ->
                    // 현재 엔트리에 선택 로그 저장 후 신고 화면 이동
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_log", log)

                    navController.navigate(UserRoutes.REPORT_HISTORY)
                }
            )
        }

        /**
         * 히스토리 신고
         */
        composable(UserRoutes.REPORT_HISTORY) {
            val log = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<UserVerificationLog>("selected_log")
                ?: return@composable

            ReportHistoryScreen(
                log = log,
                onReportSuccess = {
                    navController.navigate(UserRoutes.MAIN) {
                        popUpTo(UserRoutes.MAIN) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }

        /**
         * 손바닥 등록
         * - 성공 시 CommonResultScreen 내부에서 "메인으로 돌아가기" 버튼 클릭 → MAIN 이동
         */
        composable(UserRoutes.REGISTER_PALMPRINT) {
            RegisterPalmprintScreen(
                onGoMain = { navController.navigateToUserMainRefresh() }
            )
        }

        /**
         * 손바닥 삭제
         * - 성공 시 CommonResultScreen 내부에서 "메인으로 돌아가기" 버튼 클릭 → MAIN 이동
         */
        composable(UserRoutes.DELETE_PALMPRINT) {
            DeletePalmprintScreen(
                onGoMain = { navController.navigateToUserMainRefresh() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}

/**
 * MAIN으로 "새로 진입"시키는 방식
 * - MAIN을 재생성하여 UserMainScreen의 LaunchedEffect(Unit) refresh가 확실히 동작
 */
private fun NavController.navigateToUserMainRefresh() {
    navigate(UserRoutes.MAIN) {
        popUpTo(UserRoutes.MAIN) { inclusive = true }
        launchSingleTop = true
    }
}