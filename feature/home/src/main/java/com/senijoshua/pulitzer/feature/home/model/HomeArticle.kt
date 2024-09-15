package com.senijoshua.pulitzer.feature.home.model

/**
 * Representation of an Article type with data specific to the home screen.
 *
 * We use different model classes per screen to keep the screen data encapsulated
 * from one another. Home screen only accesses the data it needs and nothing more.
 */
internal data class HomeArticle(
    val id: String,
    val thumbnail: String,
    val title: String,
    val author: String?,
    val isBookmarked: Boolean,
)

internal val fakeArticleList = List(10) { index ->
    HomeArticle(
        index.toString(),
        "Article $index",
        "Article $index title",
        "Article $index author",
        index % 2 == 0,
    )
}
