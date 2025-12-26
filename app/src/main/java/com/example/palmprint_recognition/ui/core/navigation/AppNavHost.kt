package com.example.palmprint_recognition.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.palmprint_recognition.ui.admin.navigation.adminGraph
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.auth.navigation.AuthRoutes
import com.example.palmprint_recognition.ui.auth.navigation.authGraph
import com.example.palmprint_recognition.ui.user.navigation.userGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // prefs 로딩 완료 전에는 NavHost를 만들지 않음 (깜빡임 방지)
    if (!authState.isInitialized) return

    // 상태에 따라 "서로 다른 NavHost"를 렌더링한다.
    // authState가 바뀌면 Composable이 재구성되며 NavHost 자체가 교체되어 즉시 화면이 바뀜.
    when {
        // 1) 미로그인 → Auth 그래프
        !authState.isLoggedIn -> {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = AuthRoutes.LOGIN,
                modifier = modifier
            ) {
                // authViewModel 전달
                authGraph(navController, authViewModel)
            }
        }

        // 2) 로그인 + 관리자 → Admin 그래프
        authState.role == "ADMIN" -> {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "admin_root",
                modifier = modifier
            ) {
                adminGraph(
                    navController = navController,
                    route = "admin_root",
                    authViewModel = authViewModel //  루트 VM 전달
                )
            }
        }

        // 3) 로그인 + 일반유저 → User 그래프
        else -> {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "user_root",
                modifier = modifier
            ) {
                userGraph(
                    navController = navController,
                    route = "user_root",
                    authViewModel = authViewModel
                )
            }
        }
    }
}
