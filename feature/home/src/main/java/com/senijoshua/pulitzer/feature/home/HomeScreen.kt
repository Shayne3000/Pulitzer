@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pulitzer.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.components.EmptyScreen
import com.senijoshua.pulitzer.core.ui.components.PulitzerProgressIndicator
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground
import com.senijoshua.pulitzer.feature.home.model.HomeArticle
import com.senijoshua.pulitzer.feature.home.model.fakePagedArticleList

@Composable
internal fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (String) -> Unit = {},
    onNavigateToBookmarksScreen: () -> Unit = {}
) {
    val pagedArticles = vm.pagedArticles.collectAsLazyPagingItems()

    HomeContent(
        pagedArticles = pagedArticles,
        canShowErrorSnack = vm.canShowErrorSnack,
        bookmarkIconClicked = {
            onNavigateToBookmarksScreen()
        },
        articleClicked = { articleId ->
            onNavigateToDetailScreen(articleId)
        },
        articleBookmarked = { articleId ->
            vm.bookmarkArticle(articleId)
        },
        errorMessageShown = {
            vm.toggleErrorDisplay()
        }
    )
}

@Composable
internal fun HomeContent(
    modifier: Modifier = Modifier,
    pagedArticles: LazyPagingItems<HomeArticle>,
    canShowErrorSnack: Boolean,
    articleClicked: (String) -> Unit = { _ -> },
    bookmarkIconClicked: () -> Unit = {},
    articleBookmarked: (String) -> Unit = {},
    errorMessageShown: () -> Unit = {}
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
                .padding(padding),
            isRefreshing = pagedArticles.loadState.refresh is LoadState.Loading && pagedArticles.itemCount != 0,
            state = pullToRefreshState,
            onRefresh = {
                errorMessageShown()
                pagedArticles.refresh()
            },
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    isRefreshing = pagedArticles.loadState.refresh is LoadState.Loading && pagedArticles.itemCount != 0,
                    containerColor = MaterialTheme.colorScheme.primary,
                    color = MaterialTheme.colorScheme.onPrimary,
                    state = pullToRefreshState
                )
            }
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {
                if (pagedArticles.loadState.refresh is LoadState.Loading && pagedArticles.itemCount == 0) {
                    PulitzerProgressIndicator(
                        modifier = modifier,
                        size = dimensionResource(id = R.dimen.density_64)
                    )
                } else if ((pagedArticles.loadState.refresh is LoadState.Error) && pagedArticles.itemCount == 0) {
                    EmptyScreen(
                        modifier,
                        text = R.string.no_articles_text,
                        iconContentDescription = R.string.empty_article_list_content_desc
                    )
                } else {
                    HomeArticleList(
                        modifier = modifier,
                        pagedArticles = pagedArticles,
                        onArticleClicked = { articleId -> articleClicked(articleId) },
                        onArticleBookmarked = { articleId -> articleBookmarked(articleId) },
                    )
                }
            }
        }
    }

    if (pagedArticles.loadState.refresh is LoadState.Error && canShowErrorSnack) {
        ShowError(pagedArticles, snackBarHostState)
        errorMessageShown()
    }
}

@Composable
internal fun ShowError(
    pagedArticles: LazyPagingItems<HomeArticle>,
    snackBarHostState: SnackbarHostState
) {
    val refreshError = pagedArticles.loadState.refresh as LoadState.Error
    val errorMessage = refreshError.error.message

    errorMessage?.let { message ->
        LaunchedEffect(message, snackBarHostState) {
            snackBarHostState.showSnackbar(message)
        }
    }
}

@Composable
internal fun HomeArticleList(
    modifier: Modifier = Modifier,
    pagedArticles: LazyPagingItems<HomeArticle>,
    onArticleClicked: (String) -> Unit = {},
    onArticleBookmarked: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.density_8)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.density_4)),
    ) {
        items(pagedArticles.itemCount) { index ->
            val homeArticle = pagedArticles[index]
            homeArticle?.let { article ->
                HomeArticleItem(article = article, onArticleClicked = { articleId ->
                    onArticleClicked(articleId)
                }, onArticleBookmarked = { articleId ->
                    onArticleBookmarked(articleId)
                })
            }
        }

        when (pagedArticles.loadState.append) {
            is LoadState.Loading -> {
                item {
                    PulitzerProgressIndicator(
                        modifier = modifier,
                        size = dimensionResource(id = R.dimen.density_36)
                    )
                }
            }

            is LoadState.Error -> {
                item {
                    AppendErrorItem(pagedArticles = pagedArticles)
                }
            }

            else -> {
                // No op
            }
        }
    }
}

@Composable
internal fun AppendErrorItem(
    modifier: Modifier = Modifier,
    pagedArticles: LazyPagingItems<HomeArticle>
) {
    val appendError = pagedArticles.loadState.append as LoadState.Error

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxWidth()
            .padding(
                dimensionResource(R.dimen.density_8)
            )
    ) {
        Text(
            text = appendError.error.localizedMessage!!,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Button(
            onClick = {
                pagedArticles.retry()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.article_list_retry),
            )
        }
    }
}

@Composable
@PreviewPulitzerLightDarkBackground
private fun HomeScreenPreview() {
    PulitzerTheme {
        HomeContent(
            pagedArticles = fakePagedArticleList.collectAsLazyPagingItems(),
            canShowErrorSnack = false,
        )
    }
}
