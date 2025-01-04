@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.senijoshua.pulitzer.feature.bookmarks

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.components.EmptyScreen
import com.senijoshua.pulitzer.core.ui.components.PulitzerProgressIndicator
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground
import com.senijoshua.pulitzer.feature.bookmarks.model.BookmarksArticle
import com.senijoshua.pulitzer.feature.bookmarks.model.fakeBookmarkedArticles

@Composable
internal fun BookmarksScreen(
    vm: BookmarksViewModel = hiltViewModel(),
    onNavigateToDetailScreen: (String) -> Unit = {},
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
        onArticleClicked = { articleId ->
            onNavigateToDetailScreen(articleId)
        },
        unBookmarkArticles = { selectedArticles ->
            vm.unBookmarkSelectedArticles(selectedArticles)
        },
        onBackClicked = {
            onBackClicked()
        },
    )

    LaunchedEffect(vm.searchQuery) {
        vm.triggerSearch()
    }
}

@Composable
internal fun BookmarksContent(
    modifier: Modifier = Modifier,
    uiState: BookmarksUiState,
    searchQuery: String,
    updateSearchQuery: (String) -> Unit = {},
    onErrorShown: () -> Unit = {},
    onArticleClicked: (String) -> Unit = { _ -> },
    unBookmarkArticles: (List<String>) -> Unit = {},
    onBackClicked: () -> Unit = {},
) {
    val snackBarHostState = remember { SnackbarHostState() }

    // TODO Use mutableStateSetOf and delete the helper class by the time Compose v1.8.0 is stable
    val selectedArticleIds = remember { mutableStateOf(emptySet<String>()) }

    var isInSelectionMode by remember { mutableStateOf(false) }

    val resetSelectionMode = {
        isInSelectionMode = false
        clearArticleIds(selectedArticleIds)
    }

    // NB: We do not dismiss selection mode until we go back or press the close button in the multi select bar
    BackHandler(
        enabled = isInSelectionMode,
        onBack = resetSelectionMode
    )

    val haptics = LocalHapticFeedback.current

    SnackbarHost(hostState = snackBarHostState)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        AnimatedVisibility(
            visible = !isInSelectionMode,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            SearchBar(searchQuery, updateSearchQuery, keyboardController, onBackClicked)
        }

        AnimatedVisibility(
            visible = isInSelectionMode,
            enter = scaleIn() + expandVertically(),
            exit = scaleOut() + shrinkVertically(),
        ) {
            MultiSelectBar(
                numberOfSelectedArticles = selectedArticleIds.value.size,
                hasSelectedAllItems = uiState.bookmarkedArticles.size == selectedArticleIds.value.size,
                onSelectAll = { shouldSelectAll ->
                    if (shouldSelectAll) {
                        addAllArticleIds(
                            selectedArticleIds,
                            uiState.bookmarkedArticles.map { it.id })
                    } else {
                        clearArticleIds(selectedArticleIds)
                    }
                },
                unBookmarkSelectedArticles = {
                    unBookmarkArticles(selectedArticleIds.value.toList())
                    resetSelectionMode()
                },
                onClose = { resetSelectionMode() }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.bookmarkedArticles.isNotEmpty()) {
                BookmarkedArticlesList(
                    modifier = modifier,
                    searchQuery = searchQuery,
                    bookmarkedArticles = uiState.bookmarkedArticles,
                    selectedArticleIds = selectedArticleIds,
                    isInSelectionMode = isInSelectionMode,
                    hapticFeedback = haptics,
                    onArticleClicked = onArticleClicked,
                    initiateSelectionMode = {
                        isInSelectionMode = true
                    }
                )
            } else if (uiState.isLoading) {
                PulitzerProgressIndicator(modifier)
            } else {
                val emptyScreenText = if (uiState.hasNoBookmarks) {
                    R.string.no_bookmarked_articles
                } else {
                    R.string.no_article_search_result_text
                }

                EmptyScreen(
                    modifier,
                    text = emptyScreenText,
                    iconContentDescription = emptyScreenText
                )
            }
        }

        // TODO Align error snack bar to the bottom.
        uiState.errorMessage?.let { errorMessage ->
            LaunchedEffect(snackBarHostState, errorMessage) {
                snackBarHostState.showSnackbar(errorMessage)
                onErrorShown()
            }
        }
    }
}

@Composable
internal fun SearchBar(
    searchQuery: String,
    updateSearchQuery: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    onBackClicked: () -> Unit
) {
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.density_8)),
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = updateSearchQuery,
                onSearch = {
                    keyboardController?.hide()
                },
                leadingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.isEmpty()) {
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
                    if (searchQuery.isNotEmpty()) {
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
                expanded = false,
                onExpandedChange = { }
            )
        },
        shape = RoundedCornerShape(size = dimensionResource(id = R.dimen.density_36)),
        colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        expanded = false,
        onExpandedChange = { }
    ) {
        // No content in the search bar itself. We put the content directly below it.
    }
}

@Composable
internal fun MultiSelectBar(
    numberOfSelectedArticles: Int,
    hasSelectedAllItems: Boolean,
    onSelectAll: (Boolean) -> Unit = { _ -> },
    unBookmarkSelectedArticles: () -> Unit = {},
    onClose: () -> Unit = {},
) {
    TopAppBar(
        modifier = Modifier.padding(bottom = 8.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        navigationIcon = {
            IconButton(onClick = { onClose() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.close_content_desc),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        },
        title = {
            Text(
                text = stringResource(
                    R.string.selected_bookmark_count,
                    numberOfSelectedArticles
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
        },
        actions = {
            IconButton(onClick = {
                unBookmarkSelectedArticles()
            }) {
                Icon(
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.density_4)),
                    painter = painterResource(id = R.drawable.ic_bookmark_filled),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    contentDescription = stringResource(id = R.string.article_bookmark_content_desc)
                )
            }

            Text(
                modifier = Modifier.clickable {
                    onSelectAll(!hasSelectedAllItems)
                },
                text = if (hasSelectedAllItems) stringResource(R.string.deselect_all) else stringResource(
                    R.string.select_all
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.size(dimensionResource(R.dimen.density_16)))
        },
    )
}

@Composable
internal fun BookmarkedArticlesList(
    modifier: Modifier = Modifier,
    searchQuery: String,
    bookmarkedArticles: List<BookmarksArticle>,
    selectedArticleIds: MutableState<Set<String>>,
    isInSelectionMode: Boolean,
    hapticFeedback: HapticFeedback,
    onArticleClicked: (String) -> Unit = {},
    initiateSelectionMode: () -> Unit = {},
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = dimensionResource(id = R.dimen.density_8),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.density_8)),
        content = {
            items(
                items = bookmarkedArticles,
                key = { bookmarkedArticle -> bookmarkedArticle.id }) { bookmarkedArticle ->

                val selected = selectedArticleIds.value.contains(bookmarkedArticle.id)

                BookmarksArticleItem(
                    modifier = Modifier.combinedClickable(
                        onClick = {
                            if (isInSelectionMode) {
                                toggleArticleSelection(
                                    selected,
                                    selectedArticleIds,
                                    bookmarkedArticle
                                )
                            } else {
                                onArticleClicked(bookmarkedArticle.id)
                            }
                        },
                        onLongClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            if (isInSelectionMode) {
                                toggleArticleSelection(
                                    selected,
                                    selectedArticleIds,
                                    bookmarkedArticle
                                )
                            } else {
                                addArticleId(selectedArticleIds, bookmarkedArticle.id)
                                initiateSelectionMode()
                            }
                        }
                    ),
                    article = bookmarkedArticle,
                    searchQuery = searchQuery,
                    isSelected = selected,
                )
            }
        },
    )
}

internal fun toggleArticleSelection(
    selected: Boolean,
    selectedArticleIds: MutableState<Set<String>>,
    bookmarkedArticle: BookmarksArticle
) {
    if (selected) {
        removeArticleIds(selectedArticleIds, bookmarkedArticle.id)
    } else {
        addArticleId(selectedArticleIds, bookmarkedArticle.id)
    }
}

@PreviewPulitzerLightDarkBackground
@Composable
private fun BookmarksScreenPreview() {
    PulitzerTheme {
        BookmarksContent(
            uiState = BookmarksUiState(bookmarkedArticles = fakeBookmarkedArticles),
            searchQuery = ""
        )
    }
}

// Helper functions for operations on the Set holding selected article ids
private fun addArticleId(articleIdsSet: MutableState<Set<String>>, id: String) {
    articleIdsSet.value = articleIdsSet.value.toMutableSet().apply {
        add(id)
    }
}

private fun addAllArticleIds(articleIdsSet: MutableState<Set<String>>, idList: List<String>) {
    articleIdsSet.value = articleIdsSet.value.toMutableSet().apply {
        addAll(idList)
    }
}

private fun removeArticleIds(articleIdsSet: MutableState<Set<String>>, id: String) {
    articleIdsSet.value = articleIdsSet.value.toMutableSet().apply {
        remove(id)
    }
}

private fun clearArticleIds(articleIdsSet: MutableState<Set<String>>) {
    articleIdsSet.value = mutableSetOf()
}
