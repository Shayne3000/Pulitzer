package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground

@Composable
internal fun BookmarksArticleItem(
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current

    // TODO Store isHighlighted state across recompositions

    OutlinedCard {  }
}

@Composable
@PreviewPulitzerLightDarkBackground
private fun BookmarksArticleItemPreview() {
    PulitzerTheme {
        BookmarksArticleItem()
    }
}
