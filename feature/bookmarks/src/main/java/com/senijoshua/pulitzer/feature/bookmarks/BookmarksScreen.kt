package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground

@Composable
internal fun BookmarksScreen(
    vm: BookmarksViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {}
) {
    // Setup content screen, UDF and state production paradigms
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    BookmarksContent(uiState = uiState, onBackClicked = {
        onBackClicked()
    })
}

@Composable
internal fun BookmarksContent(
    uiState: BookmarksScreenState,
    onSearchClicked: (String) -> Unit = {},
    onBackClicked: () -> Unit = {},
) {

}

@PreviewPulitzerLightDarkBackground
private fun BookmarksScreenPreview(){

}
