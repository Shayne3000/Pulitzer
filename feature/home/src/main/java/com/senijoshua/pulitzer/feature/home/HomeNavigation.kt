package com.senijoshua.pulitzer.feature.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.senijoshua.pulitzer.feature.home.detail.ArticleDetailScreen
import com.senijoshua.pulitzer.feature.home.list.HomeScreen

internal const val HOME_ROUTE = "home"
internal const val ARTICLE_DETAILS_ROUTE = "details"
const val HOME_GRAPH = "home_graph"

/**
 * Nav graph for the screens within the Home feature module that would be nested in the app-level
 * NavGraph.
 * Its screens have internal visibility as they are self-contained/encapsulated within the feature
 * module and not accessible to other modules.
 */
fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation(startDestination = HOME_ROUTE, route = HOME_GRAPH) {
        homeScreen { articleId ->
            navController.navigateToArticleDetail(articleId)
        }
        articleDetailScreen()
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

internal fun NavGraphBuilder.articleDetailScreen() {
    composable(ARTICLE_DETAILS_ROUTE) {
        ArticleDetailScreen()
    }
}

/**
 * Type-safe nav controller extension to navigate to the ArticleDetails Screen
 */
internal fun NavController.navigateToArticleDetail(articleId: String) {
    // TODO Follow web url format in sending an argument to the details screen
    this.navigate(ARTICLE_DETAILS_ROUTE)
}
