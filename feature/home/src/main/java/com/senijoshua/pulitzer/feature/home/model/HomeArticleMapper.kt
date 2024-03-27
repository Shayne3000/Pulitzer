package com.senijoshua.pulitzer.feature.home.model

import com.senijoshua.pulitzer.domain.article.entity.Article

/**
 * Extension functions for converting the domain layer's [Article] model to
 * [HomeArticle] which contains data relevant to the HomeScreen.
 */

internal fun Article.toPresentationFormat() = HomeArticle(
    id = id,
    thumbnail = thumbnail,
    title = title,
    author = author,
    isBookmarked = isBookmarked
)

internal fun List<Article>.toPresentationFormat() = map(Article::toPresentationFormat)

internal fun HomeArticle.toDomainFormat() = Article(
    id = id,
    thumbnail = thumbnail,
    title = title,
    author = author,
    body = null,
    isBookmarked = isBookmarked,
    lastModifiedDate = null
)

internal fun List<HomeArticle>.toDomainFormat() = map(HomeArticle::toDomainFormat)


