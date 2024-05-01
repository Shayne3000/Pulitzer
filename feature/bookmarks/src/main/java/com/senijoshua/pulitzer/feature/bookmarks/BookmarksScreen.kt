package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground

@Composable
internal fun BookmarksScreen(
    vm: BookmarksViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {}
) {
    // Setup content screen, UDF and state production paradigms
    BookmarksContent()
}

@Composable
internal fun BookmarksContent() {

}

@PreviewPulitzerLightDarkBackground
private fun BookmarksScreenPreview(){

}
