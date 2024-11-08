package com.senijoshua.pulitzer.feature.bookmarks

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
internal object BookmarksGraph

@Serializable
internal object BookmarksRoute

/**
 * Nav graph for the screens in the Bookmarks module to be nested in the app-level NavGraph.
 */
fun NavGraphBuilder.bookmarksGraph(onBackClicked: () -> Unit) {
    navigation<BookmarksGraph>(startDestination = BookmarksRoute) {
        composable<BookmarksRoute> {
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
    this.navigate(BookmarksRoute)
}
