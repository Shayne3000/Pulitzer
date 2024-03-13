@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pulitzer.feature.home.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.components.PulitzerProgressIndicator
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground

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

    // not very thread-safe originally as a composable can be executed from several threads at once
    // Not optimised for optimistic recomposition, if recomposition was canceled, this may still execute
    // Composables should have no side-effects if plausible.
    vm.getArticles()
}

@Composable
internal fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onArticleClicked: (String) -> Unit = { _ -> },
    onErrorMessageShown: () -> Unit = {}
) {
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(padding)
        ) {
            // Handle the various states of the UI
            if (uiState.articles.isNotEmpty()) {
                HomeArticleList(
                    modifier = modifier,
                    uiState = uiState
                ) { articleId -> onArticleClicked(articleId) }
            } else if (uiState.isLoading) {
                // show progress, sending the modifier into the composable
                ProgressIndicator(modifier)
            } else {
                EmptyScreen()
            }
        }
    }

    // We show the error snackbar regardless of the state of the UI i.e. whether it's loading, filled or empty.
    uiState.errorMessage?.let { message ->
        LaunchedEffect(message, snackBarHostState) {
            snackBarHostState.showSnackbar(message)
            onErrorMessageShown()
        }
    }
}

@Composable
internal fun HomeArticleList(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onArticleClicked: (String) -> Unit = { _ -> },
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.density_8)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.density_4))
    ) {
        items(items = uiState.articles, key = { article -> article.id }) { homeArticle ->
            // Call Article card composable from UI -> Components with the animateItemPlacement modifier
        }
    }
}

@Composable
internal fun ProgressIndicator(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PulitzerProgressIndicator()
    }
}

@Composable
internal fun EmptyScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.density_16)),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_empty),
            contentDescription = stringResource(
                id = R.string.empty_article_list
            ),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.size(dimensionResource(id = R.dimen.density_4)))
        Text(
            text = stringResource(id = R.string.no_articles_text),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@PreviewPulitzerLightDarkBackground
private fun HomePreview() {
    PulitzerTheme {
        HomeContent(uiState = HomeUiState(isLoading = true))
    }
}
