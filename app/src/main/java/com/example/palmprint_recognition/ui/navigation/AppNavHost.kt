package com.example.palmprint_recognition.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.palmprint_recognition.ui.admin.navigation.adminGraph
import com.example.palmprint_recognition.ui.auth.AuthRoutes
import com.example.palmprint_recognition.ui.auth.authGraph
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.user.navigation.userGraph

/**
 * ===========================================================================
 * AppNavHost
 * 앱 전체 네비게이션을 총괄하는 최상위 NavHost.
 *
 * 로그인 여부 & 역할(role)에 따라 적절한 네비게이션 그래프 선택
 *     - 미로그인  → authGraph()
 *     - 관리자    → adminGraph()
 *     - 일반 사용자 → userGraph()
 * ===========================================================================
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState = authViewModel.authState.value

    // 시작 화면 결정 (로그인 여부 + 역할)
    val startDestination = when {
        !authState.isLoggedIn -> AuthRoutes.ROLE_SELECTION
        authState.role == "ADMIN" -> "admin_root"
        authState.role == "USER" -> "user_root"
        else -> AuthRoutes.ROLE_SELECTION
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        /**
         * ---------------------------------------------------------------
         * 1️. Auth 구간 (역할 선택 + 로그인)
         * ---------------------------------------------------------------
         */
        authGraph(
            navController = navController,
            onLoginCompleted = { role ->
                authViewModel.resetLoginState()

                if (role == "ADMIN") {
                    navController.navigate("admin_root") {
                        popUpTo(AuthRoutes.ROLE_SELECTION) { inclusive = true }
                    }
                } else {
                    navController.navigate("user_root") {
                        popUpTo(AuthRoutes.ROLE_SELECTION) { inclusive = true }
                    }
                }
            }
        )

        /**
         * ---------------------------------------------------------------
         * 2️. 관리자 전용 Navigation Graph
         * ---------------------------------------------------------------
         */
        adminGraph(
            navController = navController,
            route = "admin_root"
        )

        /**
         * ---------------------------------------------------------------
         * 3️. 일반 사용자 전용 Navigation Graph
         * ---------------------------------------------------------------
         */
        userGraph(
            navController = navController,
            route = "user_root"
        )
    }
}
