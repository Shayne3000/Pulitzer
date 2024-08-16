@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.pulitzer.core.ui.R
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground

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
    uiState: BookmarksScreenState,
    searchQuery: String,
    updateSearchQuery: (String) -> Unit = {},
    onBackClicked: () -> Unit = {},
) {
    // TODO Setup the UI of the search bar and the various columns/screen that hold search data.

    // TODO Potential blog on search?

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.density_16))
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        var isExpanded by remember { mutableStateOf(false) }

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(color = MaterialTheme.colorScheme.primaryContainer),
            query = searchQuery,
            onQueryChange = updateSearchQuery,
            onSearch = {
                keyboardController?.hide()
            },
            leadingIcon = {
                IconButton(onClick = {
                    if (!isExpanded) {
                        onBackClicked()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.back_content_desc
                        ),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            },
            shape = RoundedCornerShape(size = dimensionResource(id = R.dimen.density_36)),
            placeholder = { Text(text = stringResource(id = R.string.search_bookmarks)) }, // TODO Make light and italics using annotation in the string resource
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable {
                            updateSearchQuery("")
                        },
                        imageVector = Icons.Filled.Close, contentDescription = stringResource(
                            id = R.string.close_content_desc
                        ), tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            },
            active = isExpanded,
            onActiveChange = { expandedState ->
                isExpanded = expandedState
            },
        ) {
            // Search result content
        }

        // TODO Show search menu if context menu isn't showing.

        // TODO Error snack bar aligned to the bottom.
    }
}

@PreviewPulitzerLightDarkBackground
private fun BookmarksScreenPreview() {

}
