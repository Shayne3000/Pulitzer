package com.senijoshua.pulitzer.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

/**
 * Route for the home module nav graph that will be nested in the global navhost
 */
@Serializable
object HomeGraph

/**
 * Route for the Home screen composable
 */
@Serializable
internal object HomeRoute

/**
 * Nav graph for the screens within the Home module to be nested in the app-level NavGraph.
 * Its screens have internal visibility as they are self-contained/encapsulated within the feature
 * module and not accessible to other modules.
 */
fun NavGraphBuilder.homeGraph(
    navigateToDetailScreen: (String) -> Unit,
    navigateToBookmarksScreen: () -> Unit,
) {
    navigation<HomeGraph>(startDestination = HomeRoute) {
        homeScreen(toDetailScreen = { articleId ->
            navigateToDetailScreen(articleId)
        }, toBookmarksScreen = {
            navigateToBookmarksScreen()
        })
    }
}

/**
 * NavGraphBuilder extension function that adds the HomeScreen composable as a destination
 * in the Home nav graph.
 */
internal fun NavGraphBuilder.homeScreen(
    toDetailScreen: (String) -> Unit,
    toBookmarksScreen: () -> Unit
) {
    composable<HomeRoute> {
        HomeScreen(onNavigateToDetailScreen = { articleId ->
            toDetailScreen(articleId)
        }, onNavigateToBookmarksScreen = {
            toBookmarksScreen()
        })
    }
}
