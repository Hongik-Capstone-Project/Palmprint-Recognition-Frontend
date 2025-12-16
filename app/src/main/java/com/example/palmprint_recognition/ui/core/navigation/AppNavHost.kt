package com.example.palmprint_recognition.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import com.example.palmprint_recognition.ui.admin.navigation.adminGraph
import com.example.palmprint_recognition.ui.auth.navigation.AuthRoutes
import com.example.palmprint_recognition.ui.auth.navigation.authGraph
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
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    val navController = rememberNavController()

    when {
        // 로그인 X → Auth NavGraph
        !authState.isLoggedIn -> {
            NavHost(
                navController = navController,
                startDestination = AuthRoutes.LOGIN,
                modifier = modifier
            ) {
                authGraph(
                    navController = navController,
                    onLoginCompleted = { role ->
                        authViewModel.resetLoginState()
                        if (role == "ADMIN") {
                            navController.navigate("admin_root") {
                                popUpTo(AuthRoutes.LOGIN) { inclusive = true }
                            }
                        } else {
                            navController.navigate("user_root") {
                                popUpTo(AuthRoutes.LOGIN) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }

        // 로그인 O & 관리자
        authState.role == "ADMIN" -> {
            NavHost(
                navController = navController,
                startDestination = "admin_root",
                modifier = modifier
            ) {
                adminGraph(
                    navController = navController,
                    route = "admin_root"
                )
            }
        }

        // 로그인 O & 사용자
        else -> {
            NavHost(
                navController = navController,
                startDestination = "user_root",
                modifier = modifier
            ) {
                userGraph(
                    navController = navController,
                    route = "user_root"
                )
            }
        }
    }
}
