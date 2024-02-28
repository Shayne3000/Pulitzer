package com.senijoshua.pulitzer.feature.home.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (String) -> Unit = { _ -> }
) {
    // create screens using UDF and state hoisting: takes in state and expose events/functions to change state
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    HomeContent(uiState) { articleId ->
        onNavigateToDetailScreen(articleId)
    }

    vm.getArticles()
}

@Composable
internal fun HomeContent(
    uiState: HomeUiState,
    onArticleClicked: (String) -> Unit = { _ -> }
) {

}

@Composable
private fun HomePreview() {

}
