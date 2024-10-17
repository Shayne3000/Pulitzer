@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.components.EmptyScreen
import com.senijoshua.pulitzer.core.ui.components.PulitzerProgressIndicator
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground
import com.senijoshua.pulitzer.feature.bookmarks.model.BookmarksArticle

@Composable
internal fun BookmarksScreen(
    vm: BookmarksViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {}
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    BookmarksContent(
        uiState = uiState,
        searchQuery = vm.searchQuery,
        updateSearchQuery = { newQuery ->
            vm.updateSearchQuery(newQuery)
        },
        onErrorShown = {
            vm.updateErrorState()
        },
        onBackClicked = {
            onBackClicked()
        },
    )

    LaunchedEffect(Unit) {
        vm.initialiseSearch()
    }
}

@Composable
internal fun BookmarksContent(
    modifier: Modifier = Modifier,
    uiState: BookmarksUiState,
    searchQuery: String,
    updateSearchQuery: (String) -> Unit = {},
    onErrorShown: () -> Unit = {},
    onBackClicked: () -> Unit = {},
) {
    val snackBarHostState = remember { SnackbarHostState() }

    SnackbarHost(hostState = snackBarHostState)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(dimensionResource(id = R.dimen.density_16))
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        var isExpanded by remember { mutableStateOf(false) }

        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = updateSearchQuery,
                    onSearch = {
                        keyboardController?.hide()
                    },
                    leadingIcon = {
                        IconButton(onClick = {
                            // If the search bar is active and empty and this is clicked,
                            if (isExpanded) {
                                // it would go back to the normal state. If it is clicked again,
                                isExpanded = false
                            } else {
                                // it would go back to the previous screen.
                                onBackClicked()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(
                                    id = R.string.back_content_desc
                                ),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unfocusedPrefixColor = MaterialTheme.colorScheme.outline,
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.search_bookmarks),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    },
                    trailingIcon = {
                        if (isExpanded && searchQuery.isNotEmpty()) {
                            Icon(
                                modifier = Modifier.clickable {
                                    updateSearchQuery("")
                                },
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(
                                    id = R.string.close_content_desc
                                ),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    },
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }
                )
            },
            shape = RoundedCornerShape(size = dimensionResource(id = R.dimen.density_36)),
            colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it }) {
            // TODO Setup the UI of the various columns/screens that holds search data.
            if (uiState.bookmarkedArticles.isNotEmpty()) {
                BookmarkedArticlesList(modifier = modifier, bookmarkedArticles = uiState.bookmarkedArticles)
            } else if (uiState.isLoading) {
                PulitzerProgressIndicator(modifier)
            } else {
                val emptyScreenText = if (searchQuery.isNotEmpty()) {
                    R.string.no_article_search_result_text
                } else {
                    R.string.no_bookmarked_articles
                }

                EmptyScreen(
                    modifier,
                    text = emptyScreenText,
                    iconContentDescription = emptyScreenText
                )
            }
        }

        // TODO Show search menu if context menu isn't showing.

        // TODO Error snack bar aligned to the bottom.

        uiState.errorMessage?.let { errorMessage ->
            LaunchedEffect(snackBarHostState, errorMessage) {
                snackBarHostState.showSnackbar(errorMessage)
                onErrorShown()
            }
        }
    }
}

@Composable
internal fun BookmarkedArticlesList(
    modifier: Modifier = Modifier,
    bookmarkedArticles: List<BookmarksArticle>
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.density_16)),
        verticalItemSpacing = dimensionResource(id = R.dimen.density_4),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.density_8)),
        content = {
            items(
                items = bookmarkedArticles,
                key = { bookmarkedArticle -> bookmarkedArticle.id }) { bookmarkedArticle ->
                BookmarksArticleItem()
            }
        },
    )
}

@PreviewPulitzerLightDarkBackground
@Composable
private fun BookmarksScreenPreview() {
    PulitzerTheme {
        BookmarksContent(uiState = BookmarksUiState(), searchQuery = "")
    }
}
