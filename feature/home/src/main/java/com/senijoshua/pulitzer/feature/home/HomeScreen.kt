@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pulitzer.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import com.senijoshua.pulitzer.core.ui.components.EmptyScreen
import com.senijoshua.pulitzer.core.ui.components.PulitzerProgressIndicator
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground
import com.senijoshua.pulitzer.feature.home.model.HomeArticle
import com.senijoshua.pulitzer.feature.home.model.fakeArticleList

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (String) -> Unit = {},
    onNavigateToBookmarksScreen: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        bookmarkIconClicked = {
            onNavigateToBookmarksScreen()
        },
        articleClicked = { articleId ->
            onNavigateToDetailScreen(articleId)
        },
        articleBookmarked = { articleId ->
            viewModel.bookmarkArticle(articleId)
        },
        page = {
            viewModel.pageArticles()
        },
        refresh = {
            viewModel.refreshPagedArticles()
        },
        errorMessageShown = {
            viewModel.onErrorMessageShown()
        }
    )

    LaunchedEffect(Unit) {
        viewModel.getPagedArticles()
    }
}

@Composable
internal fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    articleClicked: (String) -> Unit = { _ -> },
    bookmarkIconClicked: () -> Unit = {},
    articleBookmarked: (String) -> Unit = {},
    page: () -> Unit = {},
    refresh: () -> Unit = {},
    errorMessageShown: () -> Unit = {}
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val pullToRefreshState = rememberPullToRefreshState()

    val listState = rememberLazyListState()

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
                ),
                actions = {
                    IconButton(onClick = {
                        bookmarkIconClicked()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bookmark_filled),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = stringResource(id = R.string.bookmarks_menu_item)
                        )
                    }
                }
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
        PullToRefreshBox(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(padding),
            isRefreshing = uiState.isRefreshing,
            state = pullToRefreshState,
            onRefresh = {
                refresh()
            },
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    isRefreshing = uiState.isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = pullToRefreshState
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Handle the various states of the UI
                if (uiState.articles.isNotEmpty()) {
                    HomeArticleList(
                        modifier = modifier,
                        uiState = uiState,
                        listState = listState,
                        articleClicked = { articleId -> articleClicked(articleId) },
                        articleBookmarked = { articleId -> articleBookmarked(articleId) },
                    )
                } else if (uiState.isLoading) {
                    PulitzerProgressIndicator(
                        modifier = modifier,
                        size = dimensionResource(id = R.dimen.density_64)
                    )
                } else if (uiState.isRefreshing) {
                    RefreshScreen(modifier)
                } else {
                    EmptyScreen(
                        modifier,
                        text = R.string.no_articles_text,
                        iconContentDescription = R.string.empty_article_list_content_desc
                    )
                }
            }
        }
    }

    val hasScrolledListNearEnd = remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            lastVisibleItemIndex >= totalItemsCount - 2 && totalItemsCount > 0
        }
    }

    LaunchedEffect(hasScrolledListNearEnd.value) {
        if (hasScrolledListNearEnd.value && !uiState.isPaging && !uiState.isRefreshing) {
            page()
        }
    }

    uiState.errorMessage?.let { message ->
        LaunchedEffect(message, snackBarHostState) {
            snackBarHostState.showSnackbar(message)
            errorMessageShown()
        }
    }
}

@Composable
fun RefreshScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.refresh_articles_text),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
internal fun HomeArticleList(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    listState: LazyListState,
    articleClicked: (String) -> Unit = {},
    articleBookmarked: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.density_8)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.density_4))
    ) {
        items(items = uiState.articles, key = { article -> article.id }) { homeArticle ->
            HomeArticleItem(article = homeArticle, onArticleClicked = { articleId ->
                articleClicked(articleId)
            }, onArticleBookmarked = { articleId ->
                articleBookmarked(articleId)
            })
        }
        if (uiState.isPaging) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.density_16)),
                    contentAlignment = Alignment.Center
                ) {
                    PulitzerProgressIndicator(
                        modifier = modifier,
                        size = dimensionResource(id = R.dimen.density_36)
                    )
                }
            }
        }
    }
}

@Composable
@PreviewPulitzerLightDarkBackground
private fun HomeScreenPreview() {
    PulitzerTheme {
        HomeContent(
            uiState = HomeUiState(
                articles = fakeArticleList.map { article ->
                    HomeArticle(
                        article.id,
                        article.thumbnail,
                        article.title,
                        article.author,
                        article.isBookmarked
                    )
                },
            )
        )
    }
}
