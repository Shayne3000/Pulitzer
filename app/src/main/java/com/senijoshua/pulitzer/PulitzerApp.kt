package com.senijoshua.pulitzer

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

/**
 * App-level composable for App-level UI components and UI logic. It
 * references UI-scoped sources of data like navigationController from
 * the app-level state holder.
 */
@Composable
fun PulitzerApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "Home") {
        // Add nested nav graphs for each feature module. Each feature module should have a navgraph builder extension for the various screens in the module
    }
}
