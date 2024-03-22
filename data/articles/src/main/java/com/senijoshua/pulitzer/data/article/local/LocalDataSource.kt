package com.senijoshua.pulitzer.data.article.local

import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface through which higher elements in the architectural
 * hierarchy (i.e. repository) can interact (e.g. retrieve data)
 * with the local database.
 *
 * It abstracts away implementation details of retrieving data from the
 * database and is the contract that must be implemented to perform
 * said sourcing operation.
 */
internal interface LocalDataSource {
    suspend fun getArticlesFromDB(): Flow<List<ArticleEntity>>
    suspend fun insertArticles(articles: List<ArticleEntity>)
    suspend fun getTimeCreated(): Long?
}
