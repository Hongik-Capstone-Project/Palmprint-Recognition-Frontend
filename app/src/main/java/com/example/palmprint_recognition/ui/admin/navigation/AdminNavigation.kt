package com.example.palmprint_recognition.ui.admin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.auth.features.logout.screens.LogoutScreen

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
    route: String,
    authViewModel: AuthViewModel // 추가
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
                onLogoutClick = { navController.navigate(AdminRoutes.LOGOUT) }  // ✅ 추가
            )
        }

        composable(AdminRoutes.LOGOUT) {
            LogoutScreen(
                onCancel = { navController.popBackStack() },
                authViewModel = authViewModel
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
                    navController.navigate(AdminRoutes.USER_LIST) {
                        popUpTo(AdminRoutes.USER_LIST) { inclusive = true }
                        launchSingleTop = true
                    }
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
                navController = navController,
                onDeleteClick = {
                    navController.navigate(AdminRoutes.deleteDevice(deviceId))
                }
            )
        }

        composable(AdminRoutes.REGISTER_DEVICE) {
            RegisterDeviceScreen(
                onAddSuccess = { newDeviceId ->
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
                    navController.navigate(AdminRoutes.DEVICE_LIST) {
                        popUpTo(AdminRoutes.DEVICE_LIST) { inclusive = true }
                        launchSingleTop = true
                    }

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
                navController = navController,
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
            VerificationScreen()
        }
    }
}
