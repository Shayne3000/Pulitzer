package com.senijoshua.pulitzer.feature.bookmarks

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val BOOKMARKS_ROUTE = "bookmarks"
private const val BOOKMARKS_GRAPH = "bookmarks_graph"

/**
 * Nav graph for the screens in the Bookmarks module to be nested in the app-level NavGraph.
 */
fun NavGraphBuilder.bookmarksGraph(onBackClicked: () -> Unit) {
    navigation(startDestination = BOOKMARKS_ROUTE, route = BOOKMARKS_GRAPH) {
        composable(BOOKMARKS_ROUTE) {
            BookmarksScreen {
                onBackClicked()
            }
        }
    }
}

/**
 * Type-safe nav controller extension to navigate to the Bookmarks nav graph.
 */
fun NavController.navigateToBookmarks() {
    this.navigate(BOOKMARKS_ROUTE)
}
