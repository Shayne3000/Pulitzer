package com.senijoshua.pulitzer.feature.bookmarks.model

import java.util.Date

/**
 * Representation of an Article type with data specific to the Bookmarks screen.
 */
internal data class BookmarksArticle(
    val id: String,
    val thumbnail: String,
    val title: String,
    val author: String?,
    val isBookmarked: Boolean,
    val lastModified: Date,
)
