@file:OptIn(ExperimentalMaterial3Api::class)

package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    // Setup content screen, UDF and state production paradigms
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val searchQuery = vm.searchQuery

    // TODO Also get the search query from the viewmodel. The viewmodel is the stateholder and source of truth of data for the UI so we store the search query there.

    BookmarksContent(uiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChanged = { newQuery ->
            vm.updateSearchQuery(newQuery)
        },
        onBackClicked = {
            onBackClicked()
        })
}

@Composable
internal fun BookmarksContent(
    modifier: Modifier = Modifier,
    uiState: BookmarksScreenState,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchTriggered: (String) -> Unit = {},// TODO onSearchQueryChanged()
    onBackClicked: () -> Unit = {},
) {
    // TODO Setup the search bar which holds screen content.
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.density_16))
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            query = searchQuery,
            onQueryChange = onSearchQueryChanged,
            onSearch = {
                // TODO Hide keyboard
                keyboardController?.hide()
            },
            leadingIcon = {},
            shape = RoundedCornerShape(size = dimensionResource(id = R.dimen.density_36)),
            placeholder = { Text(text = stringResource(id = R.string.search_bookmarks)) }, // TODO Make light and italics
            trailingIcon = {},
            active = true, 
            onActiveChange = {},
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
