package com.senijoshua.pulitzer.feature.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

internal const val ARTICLE_ID_ARG = "articleId"
internal const val DETAILS_ROUTE = "details"
internal const val DETAILS_GRAPH = "detail_graph"
internal const val DETAILS_NAV_ROUTE = "$DETAILS_GRAPH/$DETAILS_ROUTE?$ARTICLE_ID_ARG={$ARTICLE_ID_ARG}"

/**
 * Nav graph for the screens in the Details module that will be nested in the app-level NavGraph.
 *
 * In navigating from one screen to another with arguments in a nested nav graph within a navhost,
 * keep in mind three things:
 *
 * 1. We navigate to destinations and not graphs so the route to the destination matters
 * more than the route of the graph. Following that, the nav route has to be the destination's URI.
 * 2. The destination URI must follow the standard URI format similar to going
 *  to a different page with a query on the web especially if you don't define argument names
 *  on the screen.
 * - The route of the screen destination in the graph and the graph's startDestination must be
 * the nav route/destination URI.
 */
fun NavGraphBuilder.detailGraph(onBackClicked: () -> Unit) {
    navigation(startDestination = DETAILS_NAV_ROUTE, route = DETAILS_GRAPH) {
        detailScreen {
            onBackClicked()
        }
    }
}

/**
 * NavGraphBuilder extension function that adds the DetailScreen composable as a destination
 * in the Detail nav graph.
 */
internal fun NavGraphBuilder.detailScreen(onBackClicked: () -> Unit) {
    composable(DETAILS_NAV_ROUTE) {
        DetailScreen {
            onBackClicked()
        }
    }
}

/**
 * Type-safe nav controller extension to navigate to the Details nav graph whilst
 * passing arguments to said nav graph in tandem.
 */
fun NavController.navigateToDetail(articleId: String) {
    this.navigate("$DETAILS_GRAPH/$DETAILS_ROUTE?$ARTICLE_ID_ARG=$articleId")
}
