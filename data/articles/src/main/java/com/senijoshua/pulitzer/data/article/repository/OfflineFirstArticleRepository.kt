package com.senijoshua.pulitzer.data.article.repository

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.core.model.toResult
import com.senijoshua.pulitzer.data.article.local.LocalDataSource
import com.senijoshua.pulitzer.data.article.mapper.toDomainFormat
import com.senijoshua.pulitzer.data.article.mapper.toLocalFormat
import com.senijoshua.pulitzer.data.article.remote.RemoteDataSource
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * [ArticleRepository] implementation that aggregates [Article] data
 * from local and remote data sources in an offline-first manner and
 * returns it to the data layer.
 *
 * This abstracts away data retrieval operations from higher layers in
 * the architecture.
 */
internal class OfflineFirstArticleRepository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
) : ArticleRepository {

    // Max amount of time for which we can store article data in the DB.
    // After this, it's considered stale.
    private val cacheLimit = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

    override suspend fun getArticles(): Flow<Result<List<Article>>> {
        return local.getArticlesFromDB().map { articles ->
            articles.toDomainFormat()
        }.onEach {
            if (shouldLoadFromNetwork()) {
                val networkResponse = remote.getArticlesFromServer()
                local.insertArticles(networkResponse.toLocalFormat())
            }
        }.toResult()
    }

    override suspend fun getArticleGivenId(articleId: Int): Result<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun getBookmarkedArticles(): Flow<Result<List<Article>>> {
        TODO("Not yet implemented")
    }

    /**
     * We load fresh data from the remote service only if the data in the DB is stale or
     * if the database is empty
     */
    private suspend fun shouldLoadFromNetwork(): Boolean {
        return (System.currentTimeMillis() - (local.getTimeCreated()
            ?: 0)) > cacheLimit
    }
}
