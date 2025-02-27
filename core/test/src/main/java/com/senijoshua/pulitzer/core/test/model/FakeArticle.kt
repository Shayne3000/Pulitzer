package com.senijoshua.pulitzer.core.test.model

import com.senijoshua.pulitzer.domain.article.entity.Article
import java.util.Date

/**
 * Fake implementation of [Article] for testing.
 */

val fakeArticleList = List(10) { index ->
    Article(
        id = index.toString(),
        thumbnail = "Article $index",
        title = "Article $index title",
        author = "Article $index author",
        body = "Article $index body",
        isBookmarked = index % 2 == 0,
        lastModifiedDate = Date(System.currentTimeMillis()),
    )
}
