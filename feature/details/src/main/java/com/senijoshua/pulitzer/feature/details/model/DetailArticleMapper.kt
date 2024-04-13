package com.senijoshua.pulitzer.feature.details.model

import com.senijoshua.pulitzer.domain.article.entity.Article
import java.util.Date

/**
 * Extension functions for converting the domain layer's [Article] model to
 * [DetailArticle] which contains data relevant for the DetailScreen.
 */
internal fun Article.toPresentationFormat() = DetailArticle(
    id = id,
    thumbnail = thumbnail,
    title = title,
    author = author,
    body = body ?: "",
    isBookmarked = isBookmarked,
    lastModified = lastModifiedDate ?: Date(System.currentTimeMillis())
)
