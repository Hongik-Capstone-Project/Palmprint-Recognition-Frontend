package com.example.palmprint_recognition.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.palmprint_recognition.ui.admin.navigation.adminGraph
import com.example.palmprint_recognition.ui.auth.navigation.AuthRoutes
import com.example.palmprint_recognition.ui.auth.navigation.authGraph
import com.example.palmprint_recognition.ui.auth.AuthViewModel
import com.example.palmprint_recognition.ui.user.navigation.userGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val navController = rememberNavController()

    // prefs 로딩 완료 전에는 NavHost를 만들지 않음 (깜빡임 방지)
    if (!authState.isInitialized) return

    val start = when {
        !authState.isLoggedIn -> AuthRoutes.LOGIN
        authState.role == "ADMIN" -> "admin_root"
        else -> "user_root"
    }

    NavHost(
        navController = navController,
        startDestination = start,
        modifier = modifier
    ) {
        authGraph(navController)

        adminGraph(
            navController = navController,
            route = "admin_root"
        )

        userGraph(
            navController = navController,
            route = "user_root"
        )
    }
}
