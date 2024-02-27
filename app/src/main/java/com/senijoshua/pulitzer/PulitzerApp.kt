package com.senijoshua.pulitzer

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.senijoshua.pulitzer.feature.home.HOME_GRAPH
import com.senijoshua.pulitzer.feature.home.homeGraph

/**
 * App-level composable for App-level UI components and logic like the app's
 * navhost.
 */
@Composable
fun PulitzerApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = HOME_GRAPH) {
        // Add nested nav graphs for the feature modules instead of screens as screens are self-contained/encapsulated within a feature module.
        homeGraph(navController)
        // Each feature module should have a navgraph builder extension for the various screens in the module.
    }
}
