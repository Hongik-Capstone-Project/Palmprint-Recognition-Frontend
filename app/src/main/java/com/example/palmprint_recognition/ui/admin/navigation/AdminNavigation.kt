package com.example.palmprint_recognition.ui.admin.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable

/* Dashboard */
import com.example.palmprint_recognition.ui.admin.dashboard.AdminDashboardScreen

/* User Management */
import com.example.palmprint_recognition.ui.admin.user_management.*

/* Device Management */
import com.example.palmprint_recognition.ui.admin.device_management.*

/* Report Management */
import com.example.palmprint_recognition.ui.admin.report_management.*

/* Palmprint Management */
import com.example.palmprint_recognition.ui.admin.palmprint_management.*

/* Verification */
import com.example.palmprint_recognition.ui.admin.verification.VerificationListScreen


/**
 * adminGraph()
 *
 * AppNavHost 안에서 불려서,
 * 관리자 전용 모든 화면을 한 개의 Navigation Graph 로 묶는 함수.
 * navigation() 사용하여 “admin_root” 라우트 하위에 모든 화면 구성
 */
fun NavGraphBuilder.adminGraph(
    navController: NavController,
    route: String      // 보통 "admin_root" 로 AppNavHost에서 전달됨
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
                }
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
                onPalmprintListClick = {
                    navController.navigate(AdminRoutes.palmprintList(userId))
                }
            )
        }

        /** 2-3. 유저 추가 */
        composable(AdminRoutes.ADD_USER) {
            AddUserScreen(
                onAddSuccess = { newUserId ->
                    navController.navigate(AdminRoutes.userDetail(newUserId)) {
                        popUpTo(AdminRoutes.USER_LIST) { inclusive = false }
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
                    navController.navigate(AdminRoutes.USER_LIST) {
                        popUpTo(AdminRoutes.USER_LIST) { inclusive = false }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }


        /* =========================================================================
         * 3. PALMPRINT MANAGEMENT (손바닥 관리)
         * ========================================================================= */

        /** 3-1. Palmprint 리스트 */
        composable(
            route = AdminRoutes.PALMPRINT_LIST,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType }
            )
        ) { entry ->
            val userId = entry.arguments!!.getInt("userId")

            PalmprintListScreen(
                userId = userId,

                onAddPalmprintClick = {
                    navController.navigate(AdminRoutes.uploadPalmprint(userId))
                },

                onDeletePalmprintClick = { palmprintId ->
                    navController.navigate(
                        AdminRoutes.deletePalmprint(userId, palmprintId)
                    )
                },

                onBack = { navController.popBackStack() }
            )
        }

        /** 3-2. Palmprint 업로드 화면 */
        composable(
            route = AdminRoutes.UPLOAD_PALMPRINT,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType }
            )
        ) { entry ->
            val userId = entry.arguments!!.getInt("userId")

            UploadPalmprintScreen(
                userId = userId,
                onUploadSuccess = {
                    navController.navigate(AdminRoutes.palmprintList(userId)) {
                        popUpTo(AdminRoutes.palmprintList(userId)) {
                            inclusive = true
                        }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }

        /** 3-3. Palmprint 삭제 화면 */
        composable(
            route = AdminRoutes.DELETE_PALMPRINT,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("palmprintId") { type = NavType.IntType }
            )
        ) { entry ->
            val userId = entry.arguments!!.getInt("userId")
            val palmprintId = entry.arguments!!.getInt("palmprintId")

            DeletePalmprintScreen(
                userId = userId,
                palmprintId = palmprintId,

                onDeleteSuccess = {
                    navController.navigate(AdminRoutes.palmprintList(userId)) {
                        popUpTo(AdminRoutes.palmprintList(userId)) {
                            inclusive = true
                        }
                    }
                },

                onCancel = { navController.popBackStack() }
            )
        }



        /* =========================================================================
         * 4. DEVICE MANAGEMENT (디바이스 관리)
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
                    navController.navigate(AdminRoutes.deviceDetail(newDeviceId)) {
                        popUpTo(AdminRoutes.DEVICE_LIST) { inclusive = false }
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
                    navController.navigate(AdminRoutes.DEVICE_LIST) {
                        popUpTo(AdminRoutes.DEVICE_LIST) { inclusive = false }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }


        /* =========================================================================
         * 5. REPORT MANAGEMENT (신고 관리)
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
                    navController.navigate(AdminRoutes.REPORT_LIST) {
                        popUpTo(AdminRoutes.REPORT_LIST) { inclusive = true }
                    }
                }
            )
        }


        /* =========================================================================
         * 📌 6. VERIFICATION MANAGEMENT (인증 내역 조회)
         * ========================================================================= */

        composable(AdminRoutes.VERIFICATION_LIST) {
            VerificationListScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
