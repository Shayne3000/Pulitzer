package com.senijoshua.pulitzer.feature.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

internal const val ARTICLE_ARG = "articleId"
internal const val DETAILS_ROUTE = "details"
const val DETAILS_GRAPH = "detail_graph"
internal const val DETAILS_GRAPH_ARG_PLACEHOLDER = "$DETAILS_GRAPH/{$ARTICLE_ARG}"

/**
 * Nav graph for the screens in the Details module to be nested in the app-level NavGraph.
 */
fun NavGraphBuilder.detailGraph() {
    navigation(startDestination = DETAILS_ROUTE, route = DETAILS_GRAPH_ARG_PLACEHOLDER) {
        detailScreen()
    }
}

/**
 * NavGraphBuilder extension function that adds the DetailScreen composable as a destination
 * in the Detail nav graph.
 */
internal fun NavGraphBuilder.detailScreen() {
    composable(DETAILS_ROUTE) {
        DetailScreen()
    }
}

/**
 * Type-safe nav controller extension to navigate to the Details nav graph whilst
 * passing arguments to said nav graph in tandem.
 */
fun NavController.navigateToDetail(articleId: String) {
    this.navigate("$DETAILS_GRAPH/$articleId")
}
