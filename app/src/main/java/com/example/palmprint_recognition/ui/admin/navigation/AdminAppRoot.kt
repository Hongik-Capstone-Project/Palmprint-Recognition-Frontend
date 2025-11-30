package com.example.palmprint_recognition.ui.admin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

/**
 * ================================================================================
 * 🟦 AdminAppRoot (테스트/Preview용)
 * ================================================================================
 *
 * 이제 프로젝트 전체는 AppNavHost에서 adminGraph()를 사용하여
 * 관리자 네비게이션을 포함합니다.
 *
 * 이 파일은 “관리자 기능만 테스트할 때” 사용할 수 있는 별도 루트입니다.
 * 실제 앱에서는 AppNavHost를 사용하세요.
 */
@Composable
fun AdminAppRoot(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AdminRoutes.DASHBOARD,
        modifier = modifier
    ) {
        adminGraph(
            navController = navController,
            route = "admin_root"
        )
    }
}
