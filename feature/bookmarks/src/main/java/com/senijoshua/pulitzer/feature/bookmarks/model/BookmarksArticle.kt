package com.senijoshua.pulitzer.feature.bookmarks.model

import java.util.Date

/**
 * Presentation layer representation of an Article type with data specific to the Bookmarks screen.
 */
internal data class BookmarksArticle(
    val id: String,
    val thumbnail: String,
    val title: String,
    val author: String?,
    val isBookmarked: Boolean,
    val lastModified: Date,
)

internal val fakeBookmarkedArticles = List(10) { index ->
    BookmarksArticle(
        index.toString(),
        "Article $index",
        "Article $index title: The recent news about the sport is interesting. This is epic!",
        "Article $index author",
        true,
        Date(System.currentTimeMillis()),
    )
}
