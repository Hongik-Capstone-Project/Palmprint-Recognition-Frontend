package com.example.palmprint_recognition.ui.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.palmprint_recognition.ui.demo.navigation.demoGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "demo_root",
        modifier = modifier
    ) {
        demoGraph(
            navController = navController,
            route = "demo_root"
        )
    }
}