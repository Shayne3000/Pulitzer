package com.senijoshua.pulitzer.feature.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

/**
 * Route for the detail module nav graph that will be nested in the global navhost
 */
@Serializable
internal object DetailGraph

/**
 * Route for the Detail screen composable that takes an articleId as an argument
 */
@Serializable
internal data class DetailRoute(val articleId: String)

/**
 * Nav graph for the screens in the Details module that will be nested in the app-level NavGraph.
 *
 * In navigating from one screen to another with arguments in a nested nav graph within a navhost,
 * keep in mind these things:
 *
 * 1. We navigate to destinations and not graphs so the route to the destination matters
 * more than the route of the graph. Following that, the nav route has to be the destination's URI.
 * 2. The route of the screen destination in the graph and the graph's startDestination must be
 * the same for it to work.
 */
fun NavGraphBuilder.detailGraph(onBackClicked: () -> Unit) {
    navigation<DetailGraph>(startDestination = DetailRoute(articleId = "")) {
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
    composable<DetailRoute> {
        DetailScreen {
            onBackClicked()
        }
    }
}

/**
 * Type-safe nav controller extension to navigate to the Details destination in its nav graph whilst
 * passing arguments through to said nav graph in tandem.
 */
fun NavController.navigateToDetail(articleId: String) =
    this.navigate(DetailRoute(articleId = articleId))
