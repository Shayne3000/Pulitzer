package com.senijoshua.pulitzer.data.article.mapper

import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import com.senijoshua.pulitzer.core.database.entity.BookmarkedArticles
import com.senijoshua.pulitzer.core.network.model.NetworkArticle
import com.senijoshua.pulitzer.domain.article.entity.Article

/**
 * Extension functions for converting the data layer's [ArticleEntity] model to
 * the domain layer's [Article] which is the app's core business object; and for
 * converting the network model's, [NetworkArticle] to [ArticleEntity].
 *
 * This stays at the framework & drivers level with the datasource implementations
 * in the data layer.
 */
internal fun ArticleEntity.toDomainFormat() = Article(
    id = id,
    thumbnail = thumbnail,
    title = title,
    author = author,
    body = body,
    isBookmarked = isBookmarked,
    lastModifiedDate = lastModifiedDate
)

internal fun List<ArticleEntity>.toDomainFormat() = map(ArticleEntity::toDomainFormat)

internal fun NetworkArticle.toLocalFormat() = ArticleEntity(
    id = id,
    thumbnail = fields.thumbnail ?: "",
    title = fields.headline,
    author = fields.byline ?: "",
    body = fields.body,
    isBookmarked = false,
    lastModifiedDate = fields.lastModified,
    createdAt = System.currentTimeMillis()
)

internal fun List<NetworkArticle>.toLocalFormat() = map(NetworkArticle::toLocalFormat)

internal fun BookmarkedArticles.toDomain() =  Article(
    id = id,
    thumbnail = thumbnail,
    title = title,
    author = author,
    body = null,
    isBookmarked = false,
    lastModifiedDate = null
)

internal fun List<BookmarkedArticles>.toDomain() = map(BookmarkedArticles::toDomain)
