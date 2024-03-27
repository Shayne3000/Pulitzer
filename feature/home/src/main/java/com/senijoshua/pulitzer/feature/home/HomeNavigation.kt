package com.senijoshua.pulitzer.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

internal const val HOME_ROUTE = "home"
const val HOME_GRAPH = "home_graph"

/**
 * Nav graph for the screens within the Home module to be nested in the app-level NavGraph.
 * Its screens have internal visibility as they are self-contained/encapsulated within the feature
 * module and not accessible to other modules.
 */
fun NavGraphBuilder.homeGraph(navigateToDetailScreen: (String) -> Unit) {
    navigation(startDestination = HOME_ROUTE, route = HOME_GRAPH) {
        homeScreen { articleId ->
            navigateToDetailScreen(articleId)
        }
    }
}

/**
 * NavGraphBuilder extension function that adds the HomeScreen composable as a destination
 * in the Home nav graph.
 */
internal fun NavGraphBuilder.homeScreen(navigateToDetailScreen: (String) -> Unit) {
    composable(HOME_ROUTE) {
        HomeScreen { articleId ->
            navigateToDetailScreen(articleId)
        }
    }
}
