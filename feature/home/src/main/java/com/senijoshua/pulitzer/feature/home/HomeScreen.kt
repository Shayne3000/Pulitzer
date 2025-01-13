@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pulitzer.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.components.EmptyScreen
import com.senijoshua.pulitzer.core.ui.components.PulitzerProgressIndicator
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground
import com.senijoshua.pulitzer.feature.home.model.HomeArticle
import com.senijoshua.pulitzer.feature.home.model.fakeArticleList
import com.senijoshua.pulitzer.feature.home.model.fakePagedArticleList

@Composable
internal fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (String) -> Unit = {},
    onNavigateToBookmarksScreen: () -> Unit = {}
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val pagedArticles = vm.pagedArticles.collectAsLazyPagingItems()

    HomeContent(
        uiState = uiState,
        pagedArticles = pagedArticles,
        onBookmarkIconClicked = {
            onNavigateToBookmarksScreen()
        },
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
    pagedArticles: LazyPagingItems<HomeArticle>,
    onArticleClicked: (String) -> Unit = { _ -> },
    onBookmarkIconClicked: () -> Unit = {},
    onArticleBookmarked: (String) -> Unit = {},
    onErrorMessageShown: () -> Unit = {}
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val pullToRefreshState = rememberPullToRefreshState()

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
                        onBookmarkIconClicked()
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
            modifier = modifier.fillMaxSize(),
            isRefreshing = pagedArticles.loadState.refresh is LoadState.Loading,
            state = pullToRefreshState,
            onRefresh = {
                pagedArticles.refresh()
            },
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = pagedArticles.loadState.refresh is LoadState.Loading,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = pullToRefreshState
                )
            }
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(padding)
            ) {
                // TODO Handle refresh loading, content and error states
                if (pagedArticles.loadState.refresh is LoadState.Loading && pagedArticles.itemCount == 0) {
                    PulitzerProgressIndicator(modifier)
                } else if (pagedArticles.loadState.refresh is LoadState.Error && pagedArticles.itemCount == 0){
                    EmptyScreen(
                        modifier,
                        text = R.string.no_articles_text,
                        iconContentDescription = R.string.empty_article_list_content_desc
                    )
                } else {
                    HomeArticleList(
                        modifier = modifier,
                        uiState = uiState,
                        pagedArticles = pagedArticles,
                        onArticleClicked = { articleId -> onArticleClicked(articleId) },
                        onArticleBookmarked = { articleId -> onArticleBookmarked(articleId) },
                    )
                }

            }
        }
    }

    // Show snackbar only when there's an error during initial or refresh loading
    if (pagedArticles.loadState.refresh is LoadState.Error) {
        val refreshError = pagedArticles.loadState.refresh as LoadState.Error
        val errorMessage = refreshError.error.message

        errorMessage?.let { message ->
            LaunchedEffect(message) {
                snackBarHostState.showSnackbar(message)
            }
        }

    }
}

@Composable
internal fun HomeArticleList(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    pagedArticles: LazyPagingItems<HomeArticle>,
    onArticleClicked: (String) -> Unit = {},
    onArticleBookmarked: (String) -> Unit = {},
) {
    // TODO Handle append loading, and error state
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.density_8)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.density_4)),
    ) {
        items(pagedArticles.itemCount, key = { index ->  pagedArticles[index]!!.id }) { index ->
            HomeArticleItem(article = pagedArticles[index]!!, onArticleClicked = { articleId ->
                onArticleClicked(articleId)
            }, onArticleBookmarked = { articleId ->
                onArticleBookmarked(articleId)
            })
        }
        pagedArticles.apply {
            when {
                
            }
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
            },
            errorMessage = "hey!"
        ), pagedArticles = fakePagedArticleList.collectAsLazyPagingItems())
    }
}
