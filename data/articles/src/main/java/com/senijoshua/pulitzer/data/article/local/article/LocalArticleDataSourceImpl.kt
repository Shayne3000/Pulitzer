package com.senijoshua.pulitzer.data.article.local.article

import com.senijoshua.pulitzer.core.database.dao.ArticleDao
import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import com.senijoshua.pulitzer.core.database.entity.BookmarkedArticles
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [LocalArticleDataSource] that directly interacts with the
 * database to get data.
 */
internal class LocalArticleDataSourceImpl @Inject constructor(
    private val dao: ArticleDao
) : LocalArticleDataSource {
    override fun getArticlesFromDB(): Flow<List<ArticleEntity>> = dao.getArticles()

    override suspend fun insertArticles(articles: List<ArticleEntity>) =
        dao.insertArticles(articles)

    override fun getTimeCreated() = dao.getTimeCreated()

    override fun getArticleById(articleId: String) = dao.getArticleById(articleId)

    override suspend fun bookmarkArticle(articleId: String) = dao.bookmarkArticle(articleId)

    override suspend fun unBookmarkArticles(articleIds: List<String>) =
        dao.unBookmarkArticles(articleIds)

    override fun getBookmarkedArticles(searchQuery: String): Flow<List<BookmarkedArticles>> =
        dao.getBookmarkedArticles(searchQuery)

    override suspend fun clearArticles() = dao.clearArticles()

    // transactions clear remote keys and articles, insert remote keys and articles
}
