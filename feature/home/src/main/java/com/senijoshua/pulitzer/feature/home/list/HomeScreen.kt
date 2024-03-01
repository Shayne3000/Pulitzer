@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pulitzer.feature.home.list

import androidx.compose.material3.ExperimentalMaterial3Api
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

    HomeContent(
        uiState = uiState,
        onArticleClicked = { articleId ->
            onNavigateToDetailScreen(articleId)
        },
        onErrorMessageShown = {
            vm.onErrorMessageShown()
        }
    )

    vm.getArticles()
}

@Composable
internal fun HomeContent(
    uiState: HomeUiState,
    onArticleClicked: (String) -> Unit = { _ -> },
    onErrorMessageShown: () -> Unit = {}
) {
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(title = {
//
//            })
//        },
//        snackbarHost = {}
//    ) { padding ->
//
//    }
    // Setup scaffold to hold toolbar and snackbar
    // Setup list
}

@Composable
private fun HomePreview() {
    // consider multipreview templates
}
