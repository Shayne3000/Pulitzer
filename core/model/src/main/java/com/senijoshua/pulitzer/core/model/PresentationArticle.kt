package com.senijoshua.pulitzer.core.model

import java.util.Date

/**
 * Representation of an Article type with data relevant to the presentation layer.
 * It operates mainly in the presentation layer and is helpful in testing.
 */
open class PresentationArticle(
    open val id: String,
    open val thumbnail: String,
    open val title: String,
    open val author: String,
    open val isBookmarked: Boolean,
    open val lastModified: Date?,
)

val fakeArticleList = List(10) { index ->
    PresentationArticle(
        index.toString(),
        "Article $index",
        "Article $index title",
        "Article $index author",
        index % 2 == 0,
        Date(System.currentTimeMillis())
    )
}
