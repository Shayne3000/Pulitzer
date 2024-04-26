@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pulitzer.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.pulitzer.core.model.fakeArticleList
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.components.ArticleItem
import com.senijoshua.pulitzer.core.ui.components.EmptyScreen
import com.senijoshua.pulitzer.core.ui.components.PulitzerProgressIndicator
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground
import com.senijoshua.pulitzer.feature.home.model.HomeArticle

@Composable
internal fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (String) -> Unit = { _ -> }
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onArticleClicked = { articleId ->
            onNavigateToDetailScreen(articleId)
        },
        onArticleBookmarked = { articleId ->
            vm.bookmarkArticle(articleId)
        },
        onErrorMessageShown = {
            vm.onErrorMessageShown()
        }
    )

    LaunchedEffect(Unit) {
        vm.getArticles()
    }
}

@Composable
internal fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onArticleClicked: (String) -> Unit = { _ -> },
    onArticleBookmarked: (String) -> Unit = {},
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
                    uiState = uiState,
                    onArticleClicked = { articleId -> onArticleClicked(articleId) },
                    onArticleBookmarked = { articleId -> onArticleBookmarked(articleId) },
                )
            } else if (uiState.isLoading) {
                PulitzerProgressIndicator(modifier)
            } else {
                EmptyScreen(
                    modifier,
                    text = R.string.no_articles_text,
                    iconContentDescription = R.string.empty_article_list
                )
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
    onArticleClicked: (String) -> Unit = {},
    onArticleBookmarked: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.density_8)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.density_4))
    ) {
        items(items = uiState.articles, key = { article -> article.id }) { homeArticle ->
            ArticleItem(article = homeArticle, onArticleClicked = { articleId ->
                onArticleClicked(articleId)
            }, onArticleBookmarked = { articleId ->
                onArticleBookmarked(articleId)
            })
        }
    }
}

@Composable
@PreviewPulitzerLightDarkBackground
private fun HomeScreenPreview() {
    PulitzerTheme {
        HomeContent(uiState = HomeUiState(
            articles = fakeArticleList.map { article ->
                HomeArticle(
                    article.id,
                    article.thumbnail,
                    article.title,
                    article.author,
                    article.isBookmarked
                )
            }
        ))
    }
}
