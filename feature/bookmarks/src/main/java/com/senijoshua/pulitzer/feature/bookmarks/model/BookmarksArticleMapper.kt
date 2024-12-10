package com.senijoshua.pulitzer.feature.bookmarks.model

import com.senijoshua.pulitzer.domain.article.entity.Article

/**
 * Extension functions for converting the domain layer's [Article] model to
 * [BookmarksArticle] which contains data relevant to the BookmarksScreen.
 */
internal fun Article.toPresentationFormat() = BookmarksArticle(
    id = id,
    thumbnail = thumbnail,
    title = title,
    author = author
)

internal fun List<Article>.toPresentationFormat() = map(Article::toPresentationFormat)

internal fun BookmarksArticle.toDomainFormat() = Article(
    id = id,
    thumbnail = thumbnail,
    title = title,
    author = author ?: "",
    body = null,
    isBookmarked = false,
    lastModifiedDate = null
)

internal fun List<BookmarksArticle>.toDomainFormat() = map(BookmarksArticle::toDomainFormat)
