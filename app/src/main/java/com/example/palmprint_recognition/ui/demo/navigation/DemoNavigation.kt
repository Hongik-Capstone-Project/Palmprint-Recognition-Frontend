package com.example.palmprint_recognition.ui.demo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.palmprint_recognition.ui.demo.features.home.screens.DemoHomeScreen
import com.example.palmprint_recognition.ui.demo.features.register.screens.DemoRegisterScreen
import com.example.palmprint_recognition.ui.demo.features.verify.screens.DemoVerifyScreen

fun NavGraphBuilder.demoGraph(
    navController: NavController,
    route: String
) {
    navigation(
        startDestination = DemoRoutes.HOME,
        route = route
    ) {
        composable(DemoRoutes.HOME) {
            DemoHomeScreen(
                onRegisterClick = { navController.navigate(DemoRoutes.REGISTER) },
                onVerifyClick = { navController.navigate(DemoRoutes.VERIFY) },
                onGuideClick = { navController.navigate(DemoRoutes.GUIDE) }
            )
        }

        composable(DemoRoutes.REGISTER) {
            DemoRegisterScreen(
                onGoHome = {
                    navController.navigate(DemoRoutes.HOME) {
                        popUpTo(DemoRoutes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(DemoRoutes.VERIFY) {
            DemoVerifyScreen(
                onGoHome = {
                    navController.navigate(DemoRoutes.HOME) {
                        popUpTo(DemoRoutes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}