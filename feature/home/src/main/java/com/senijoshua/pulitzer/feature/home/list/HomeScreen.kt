package com.senijoshua.pulitzer.feature.home.list

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (articleId: String) -> Unit = { _ -> }
) {
    // create screens using UDF and state hoisting: takes in state and expose events/functions to change state
}

@Composable
internal fun HomeContent() {

}

@Composable
private fun HomePreview() {

}
