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
    override suspend fun getArticlesFromDB(): Flow<List<ArticleEntity>> = dao.getArticles()

    override suspend fun insertArticles(articles: List<ArticleEntity>) =
        dao.insertArticles(articles)

    override suspend fun getTimeCreated() = dao.getTimeCreated()
}
