package com.senijoshua.pulitzer

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.senijoshua.pulitzer.feature.bookmarks.bookmarksGraph
import com.senijoshua.pulitzer.feature.bookmarks.navigateToBookmarks
import com.senijoshua.pulitzer.feature.details.detailGraph
import com.senijoshua.pulitzer.feature.details.navigateToDetail
import com.senijoshua.pulitzer.feature.home.HomeGraph
import com.senijoshua.pulitzer.feature.home.homeGraph

/**
 * App-level composable for App-level UI components and logic like the app's
 * nav host.
 */
@Composable
fun Pulitzer(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = HomeGraph) {
        homeGraph(navigateToDetailScreen =  { articleId ->
            navController.navigateToDetail(articleId)
        }, navigateToBookmarksScreen = {
            navController.navigateToBookmarks()
        })
        detailGraph {
            navController.popBackStack()
        }
        bookmarksGraph(navigateToDetailScreen = { articleId ->
            navController.navigateToDetail(articleId)
        }, onBackClicked = {
            navController.popBackStack()
        })
    }
}
