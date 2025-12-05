package com.example.palmprint_recognition.ui.admin.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument

/* Dashboard */
import com.example.palmprint_recognition.ui.admin.dashboard.AdminDashboardScreen

/* User Management */
import com.example.palmprint_recognition.ui.admin.user_management.AddUserScreen
import com.example.palmprint_recognition.ui.admin.user_management.DeleteUserScreen
import com.example.palmprint_recognition.ui.admin.user_management.UserDetailScreen
import com.example.palmprint_recognition.ui.admin.user_management.UserListScreen

/* Device Management */
import com.example.palmprint_recognition.ui.admin.device_management.AddDeviceScreen
import com.example.palmprint_recognition.ui.admin.device_management.DeleteDeviceScreen
import com.example.palmprint_recognition.ui.admin.device_management.DeviceDetailScreen
import com.example.palmprint_recognition.ui.admin.device_management.DeviceListScreen

/* Report Management */
import com.example.palmprint_recognition.ui.admin.report_management.ReportDetailScreen
import com.example.palmprint_recognition.ui.admin.report_management.ReportListScreen

/* Verification */
import com.example.palmprint_recognition.ui.admin.verification.VerificationListScreen

/**
 * adminGraph()
 *
 * AppNavHost 안에서 호출되어
 * 관리자 전용 화면들을 "admin_root" 하위에 묶는 Navigation Graph.
 */
fun NavGraphBuilder.adminGraph(
    navController: NavController,
    route: String      // 보통 "admin_root"
) {
    navigation(
        startDestination = AdminRoutes.DASHBOARD,
        route = route
    ) {
        /* =========================================================================
         * 1. ADMIN DASHBOARD (관리자 대시보드)
         * ========================================================================= */
        composable(AdminRoutes.DASHBOARD) {
            AdminDashboardScreen(
                onUserManagementClick = {
                    navController.navigate(AdminRoutes.USER_LIST)
                },
                onDeviceManagementClick = {
                    navController.navigate(AdminRoutes.DEVICE_LIST)
                },
                onReportManagementClick = {
                    navController.navigate(AdminRoutes.REPORT_LIST)
                },
                onVerificationManagementClick = {
                    navController.navigate(AdminRoutes.VERIFICATION_LIST)
                }
            )
        }

        /* =========================================================================
         * 2. USER MANAGEMENT (유저 관리)
         * ========================================================================= */

        /** 2-1. 유저 목록 */
        composable(AdminRoutes.USER_LIST) {
            UserListScreen(
                onUserClick = { userId ->
                    navController.navigate(AdminRoutes.userDetail(userId))
                },
                onAddUserClick = {
                    navController.navigate(AdminRoutes.ADD_USER)
                },

            )
        }

        /** 2-2. 유저 상세 */
        composable(
            route = AdminRoutes.USER_DETAIL,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { entry ->
            val userId = entry.arguments!!.getInt("userId")

            UserDetailScreen(
                userId = userId,
                onDeleteClick = {
                    navController.navigate(AdminRoutes.deleteUser(userId))
                },
                navController = navController
            )
        }

        /** 2-3. 유저 추가 */
        composable(AdminRoutes.ADD_USER) {
            AddUserScreen(
                onAddSuccess = { newUserId ->
                    navController.navigate(AdminRoutes.userDetail(newUserId)) {
                        popUpTo(AdminRoutes.ADD_USER) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        /** 2-4. 유저 삭제 */
        composable(
            route = AdminRoutes.DELETE_USER,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { entry ->
            val userId = entry.arguments!!.getInt("userId")

            DeleteUserScreen(
                userId = userId,
                onConfirmDelete = {
                    // 삭제 플로우 스택 예시:
                    //   DASHBOARD → USER_LIST → USER_DETAIL → DELETE_USER
                    // popUpTo(USER_LIST, inclusive=true)의 의미:
                    //   - USER_LIST까지 포함해서 모두 제거 (USER_LIST도 삭제)
                    //   - 남는 스택: DASHBOARD
                    // 그 다음 USER_LIST로 다시 이동 → 새로운 USER_LIST 화면을 하나 쌓음
                    //   결과 스택: DASHBOARD → USER_LIST
                    navController.navigate(AdminRoutes.USER_LIST) {
                        popUpTo(AdminRoutes.USER_LIST) {
                            // true → 기존 USER_LIST도 제거 후 새 USER_LIST를 다시 쌓는다
                            inclusive = true
                        }
                    }
                },
                onCancel = {
                    // 삭제 취소 → 이전 화면(USER_DETAIL)로 복귀
                    navController.popBackStack()
                }
            )
        }

        /* =========================================================================
         * 3. DEVICE MANAGEMENT (디바이스 관리)
         * ========================================================================= */

        composable(AdminRoutes.DEVICE_LIST) {
            DeviceListScreen(
                onDeviceClick = { deviceId ->
                    navController.navigate(AdminRoutes.deviceDetail(deviceId))
                },
                onAddDeviceClick = {
                    navController.navigate(AdminRoutes.ADD_DEVICE)
                }
            )
        }

        composable(
            route = AdminRoutes.DEVICE_DETAIL,
            arguments = listOf(navArgument("deviceId") { type = NavType.IntType })
        ) { entry ->
            val deviceId = entry.arguments!!.getInt("deviceId")

            DeviceDetailScreen(
                deviceId = deviceId,
                onDeleteClick = {
                    navController.navigate(AdminRoutes.deleteDevice(deviceId))
                }
            )
        }

        composable(AdminRoutes.ADD_DEVICE) {
            AddDeviceScreen(
                onAddSuccess = { newDeviceId ->
                    // 디바이스 추가 성공 시 → 디바이스 상세 화면으로 이동
                    // 현재 스택 예시:
                    //   DASHBOARD → DEVICE_LIST → ADD_DEVICE
                    // popUpTo(DEVICE_LIST, inclusive=false)의 의미:
                    //   - ADD_DEVICE만 제거하고 DEVICE_LIST는 남김
                    //   - 결과 스택: DASHBOARD → DEVICE_LIST
                    //   → 그 위에 DEVICE_DETAIL(newDeviceId)를 쌓음
                    navController.navigate(AdminRoutes.deviceDetail(newDeviceId)) {
                        popUpTo(AdminRoutes.DEVICE_LIST) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        composable(
            route = AdminRoutes.DELETE_DEVICE,
            arguments = listOf(navArgument("deviceId") { type = NavType.IntType })
        ) { entry ->
            val deviceId = entry.arguments!!.getInt("deviceId")

            DeleteDeviceScreen(
                deviceId = deviceId,
                onConfirmDelete = {
                    // 삭제 플로우 스택 예시:
                    //   DASHBOARD → DEVICE_LIST → DEVICE_DETAIL → DELETE_DEVICE
                    // popUpTo(DEVICE_LIST, inclusive=true)의 의미:
                    //   - DEVICE_LIST까지 모두 제거 (DEVICE_LIST도 삭제)
                    //   - 남는 스택: DASHBOARD
                    // 이후 DEVICE_LIST로 다시 이동 → 새로운 목록 화면
                    navController.navigate(AdminRoutes.DEVICE_LIST) {
                        popUpTo(AdminRoutes.DEVICE_LIST) {
                            inclusive = true
                        }
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        /* =========================================================================
         * 4. REPORT MANAGEMENT (신고 관리)
         * ========================================================================= */

        composable(AdminRoutes.REPORT_LIST) {
            ReportListScreen(
                onReportClick = { reportId ->
                    navController.navigate(AdminRoutes.reportDetail(reportId))
                }
            )
        }

        composable(
            route = AdminRoutes.REPORT_DETAIL,
            arguments = listOf(navArgument("reportId") { type = NavType.IntType })
        ) { entry ->
            val reportId = entry.arguments!!.getInt("reportId")

            ReportDetailScreen(
                reportId = reportId,
                onSaveSuccess = {
                    // 신고 상세 → 저장 후 목록으로 돌아가기
                    // 스택 예시:
                    //   DASHBOARD → REPORT_LIST → REPORT_DETAIL
                    // popUpTo(REPORT_LIST, inclusive=true)의 의미:
                    //   - REPORT_LIST까지 제거 (REPORT_LIST + REPORT_DETAIL 제거)
                    //   - 남는 스택: DASHBOARD
                    // 이후 REPORT_LIST를 새로 쌓음 → 최신 목록 화면
                    navController.navigate(AdminRoutes.REPORT_LIST) {
                        popUpTo(AdminRoutes.REPORT_LIST) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        /* =========================================================================
         * 5. VERIFICATION MANAGEMENT (인증 내역 조회)
         * ========================================================================= */

        composable(AdminRoutes.VERIFICATION_LIST) {
            VerificationListScreen(

            )
        }
    }
}
