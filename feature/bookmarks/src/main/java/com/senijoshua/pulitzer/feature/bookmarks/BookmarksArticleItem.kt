package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.senijoshua.pulitzer.core.ui.theme.PulitzerTheme
import com.senijoshua.pulitzer.core.ui.util.PreviewPulitzerLightDarkBackground
import com.senijoshua.pulitzer.feature.bookmarks.model.BookmarksArticle

@Composable
internal fun BookmarksArticleItem(
    modifier: Modifier = Modifier,
    article: BookmarksArticle,
    isSelected: Boolean = false,
    isInSelectionMode: Boolean = false,
) {
    OutlinedCard {  }
}

@Composable
@PreviewPulitzerLightDarkBackground
private fun BookmarksArticleItemPreview() {
    PulitzerTheme {
        //BookmarksArticleItem()
    }
}
