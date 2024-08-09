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
import androidx.compose.runtime.LaunchedEffect
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

        // TODO Build your own searchbar component with BasicTextField that takes a state
        //  instead of a text and use it here since we cannot explicitly update TextFieldState or just use mutableStateOf in the ViewModel to store textField state

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            query = searchQuery,
            onQueryChange = updateSearchQuery,
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
