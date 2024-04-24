package com.senijoshua.pulitzer.data.article.local

import com.senijoshua.pulitzer.core.database.dao.ArticleDao
import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [LocalDataSource] that directly interacts with the
 * database to get data.
 */
internal class LocalDataSourceImpl @Inject constructor(
    private val dao: ArticleDao
) : LocalDataSource {
    override fun getArticlesFromDB(): Flow<List<ArticleEntity>> = dao.getArticles()

    override suspend fun insertArticles(articles: List<ArticleEntity>) =
        dao.insertArticles(articles)

    override fun getTimeCreated() = dao.getTimeCreated()
    override fun getArticleById(articleId: String) = dao.getArticleById(articleId)
    override suspend fun bookmarkArticle(articleId: String) = dao.bookmarkArticle(articleId)
}
