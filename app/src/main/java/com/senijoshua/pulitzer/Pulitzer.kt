package com.senijoshua.pulitzer

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.senijoshua.pulitzer.feature.details.detailGraph
import com.senijoshua.pulitzer.feature.details.navigateToDetail
import com.senijoshua.pulitzer.feature.home.HOME_GRAPH
import com.senijoshua.pulitzer.feature.home.homeGraph

/**
 * App-level composable for App-level UI components and logic like the app's
 * nav host.
 */
@Composable
fun Pulitzer(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = HOME_GRAPH) {
        homeGraph { articleId ->
            navController.navigateToDetail(articleId)
        }
        detailGraph {
            navController.popBackStack()
        }
    }
}
