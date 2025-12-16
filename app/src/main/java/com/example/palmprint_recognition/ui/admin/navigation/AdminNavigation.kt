package com.example.palmprint_recognition.ui.admin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument

/* Dashboard */
import com.example.palmprint_recognition.ui.admin.features.dashboard.screens.AdminDashboardScreen

/* User */
import com.example.palmprint_recognition.ui.admin.features.user_management.screens.AddUserScreen
import com.example.palmprint_recognition.ui.admin.features.user_management.screens.DeleteUserScreen
import com.example.palmprint_recognition.ui.admin.features.user_management.screens.UserDetailScreen
import com.example.palmprint_recognition.ui.admin.features.user_management.screens.UserListScreen

/* Device */
import com.example.palmprint_recognition.ui.admin.features.device_management.screens.RegisterDeviceScreen
import com.example.palmprint_recognition.ui.admin.features.device_management.screens.DeleteDeviceScreen
import com.example.palmprint_recognition.ui.admin.features.device_management.screens.DeviceDetailScreen
import com.example.palmprint_recognition.ui.admin.features.device_management.screens.DeviceListScreen

/* Report */
import com.example.palmprint_recognition.ui.admin.features.report_management.screens.ReportDetailScreen
import com.example.palmprint_recognition.ui.admin.features.report_management.screens.ReportListScreen

/* Verification */
import com.example.palmprint_recognition.ui.admin.features.verification.screens.VerificationScreen

import com.example.palmprint_recognition.ui.core.navigation.navigateAndClearUpTo


fun NavGraphBuilder.adminGraph(
    navController: NavController,
    route: String
) {
    navigation(
        startDestination = AdminRoutes.DASHBOARD,
        route = route
    ) {

        // 1) DASHBOARD
        composable(AdminRoutes.DASHBOARD) {
            AdminDashboardScreen(
                onUserManageClick = { navController.navigate(AdminRoutes.USER_LIST) },
                onDeviceManageClick = { navController.navigate(AdminRoutes.DEVICE_LIST) },
                onReportManageClick = { navController.navigate(AdminRoutes.REPORT_LIST) },
                onVerificationClick = { navController.navigate(AdminRoutes.VERIFICATION) },
                onLogoutClick = {
                    // TODO: 로그아웃 처리(토큰 삭제/상위 그래프로 이동 등)
                    // 예: navController.navigate("login") { popUpTo(route){inclusive=true} }
                }
            )
        }

        // 2) USER
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

        composable(
            route = AdminRoutes.USER_DETAIL,
            arguments = listOf(navArgument(AdminRoutes.ARG_USER_ID) { type = NavType.IntType })
        ) { entry ->
            val userId = entry.arguments?.getInt(AdminRoutes.ARG_USER_ID) ?: return@composable

            UserDetailScreen(
                userId = userId,
                navController = navController,
                onDeleteClick = {
                    navController.navigate(AdminRoutes.deleteUser(userId))
                }
            )
        }

        composable(AdminRoutes.ADD_USER) {
            AddUserScreen(
                onAddSuccess = { newUserId ->
                    // add -> detail, add screen 제거
                    navController.navigate(AdminRoutes.userDetail(newUserId)) {
                        popUpTo(AdminRoutes.ADD_USER) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = AdminRoutes.DELETE_USER,
            arguments = listOf(navArgument(AdminRoutes.ARG_USER_ID) { type = NavType.IntType })
        ) { entry ->
            val userId = entry.arguments?.getInt(AdminRoutes.ARG_USER_ID) ?: return@composable

            DeleteUserScreen(
                userId = userId,
                onConfirmDelete = {
                    // 삭제 성공 -> USER_LIST 새로
                    navController.navigateAndClearUpTo(
                        destination = AdminRoutes.USER_LIST,
                        popUpToRoute = AdminRoutes.USER_LIST,
                        inclusive = true
                    )
                },
                onCancel = { navController.popBackStack() }
            )
        }

        // 3) DEVICE
        composable(AdminRoutes.DEVICE_LIST) {
            DeviceListScreen(
                onDeviceClick = { deviceId ->
                    navController.navigate(AdminRoutes.deviceDetail(deviceId))
                },
                onRegisterDeviceClick = {
                    navController.navigate(AdminRoutes.REGISTER_DEVICE)
                }
            )
        }

        composable(
            route = AdminRoutes.DEVICE_DETAIL,
            arguments = listOf(navArgument(AdminRoutes.ARG_DEVICE_ID) { type = NavType.IntType })
        ) { entry ->
            val deviceId = entry.arguments?.getInt(AdminRoutes.ARG_DEVICE_ID) ?: return@composable

            DeviceDetailScreen(
                deviceId = deviceId,
                navController = navController, // 현재 DeviceDetailScreen은 navController 사용(BackHandler)
                onDeleteClick = {
                    navController.navigate(AdminRoutes.deleteDevice(deviceId))
                }
            )
        }

        composable(AdminRoutes.REGISTER_DEVICE) {
            RegisterDeviceScreen(
                onAddSuccess = { newDeviceId ->
                    // register -> detail, 목록은 유지
                    navController.navigate(AdminRoutes.deviceDetail(newDeviceId)) {
                        popUpTo(AdminRoutes.DEVICE_LIST) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = AdminRoutes.DELETE_DEVICE,
            arguments = listOf(navArgument(AdminRoutes.ARG_DEVICE_ID) { type = NavType.IntType })
        ) { entry ->
            val deviceId = entry.arguments?.getInt(AdminRoutes.ARG_DEVICE_ID) ?: return@composable

            DeleteDeviceScreen(
                deviceId = deviceId,
                onConfirmDelete = {
                    navController.navigateAndClearUpTo(
                        destination = AdminRoutes.DEVICE_LIST,
                        popUpToRoute = AdminRoutes.DEVICE_LIST,
                        inclusive = true
                    )
                },
                onCancel = { navController.popBackStack() }
            )
        }

        // 4) REPORT
        composable(AdminRoutes.REPORT_LIST) {
            ReportListScreen(
                onReportClick = { reportId ->
                    navController.navigate(AdminRoutes.reportDetail(reportId))
                }
            )
        }

        composable(
            route = AdminRoutes.REPORT_DETAIL,
            arguments = listOf(navArgument(AdminRoutes.ARG_REPORT_ID) { type = NavType.IntType })
        ) { entry ->
            val reportId = entry.arguments?.getInt(AdminRoutes.ARG_REPORT_ID) ?: return@composable

            ReportDetailScreen(
                reportId = reportId,
                navController = navController, // ✅ ReportDetailScreen에서 BackHandler 사용 중
                onSaveSuccess = {
                    navController.navigateAndClearUpTo(
                        destination = AdminRoutes.REPORT_LIST,
                        popUpToRoute = AdminRoutes.REPORT_LIST,
                        inclusive = true
                    )
                }
            )
        }

        // 5) VERIFICATION
        composable(AdminRoutes.VERIFICATION) {
            VerificationScreen(
                // navController = navController
                // 필요 시 콜백 추가
            )
        }
    }
}
