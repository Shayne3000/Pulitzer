package com.senijoshua.pulitzer.data.article.repository

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.core.model.toResult
import com.senijoshua.pulitzer.data.article.local.LocalDataSource
import com.senijoshua.pulitzer.data.article.mapper.toDomainFormat
import com.senijoshua.pulitzer.data.article.mapper.toLocalFormat
import com.senijoshua.pulitzer.data.article.remote.RemoteDataSource
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
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
    private val dispatcher: CoroutineDispatcher,
) : ArticleRepository {
    /**
     * The cache limit is the max amount of time for which we
     * can serve article data in the DB after which, it will be
     * considered old and fresh will be requested from the server.
     */
    private val refreshCacheLimit = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

    /**
     * The clear limit is  the max amount of time for which we
     * can store article data in the DB after which it will be
     * considered stale and deleted from the DB.
     */
    private val clearCacheLimit = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)

    override fun getArticles(): Flow<Result<List<Article>>> {
        return local.getArticlesFromDB().map { articles ->
            articles.toDomainFormat()
        }.onEach {
            if (shouldLoadArticlesFromRemoteService()) {
                val networkResponse = remote.getArticlesFromServer()

                if (shouldClearStaleData()) {
                    local.clearArticles()
                }
                local.insertArticles(networkResponse.toLocalFormat())
            }
        }.flowOn(dispatcher).toResult()
    }

    override fun getArticleGivenId(articleId: String): Flow<Result<Article>> {
        return local.getArticleById(articleId).flowOn(dispatcher).map { article ->
            article.toDomainFormat()
        }.toResult()

    }

    override suspend fun getBookmarkedArticles(): Flow<Result<List<Article>>> {
        TODO("Not yet implemented")
    }

    override suspend fun bookmarkArticle(articleId: String) {
        local.bookmarkArticle(articleId)
    }

    /**
     * We load fresh data from the remote service only if the data in the DB is old or
     * if the database is empty.
     */
    private fun shouldLoadArticlesFromRemoteService(): Boolean {
        return isLimitExceeded(refreshCacheLimit)
    }

    /**
     * We delete data from the DB if the data in the DB is stale.
     */
    private fun shouldClearStaleData(): Boolean = isLimitExceeded(clearCacheLimit)

    private fun isLimitExceeded(cacheLimit: Long) =
        (System.currentTimeMillis() - (local.getTimeCreated() ?: 0)) > cacheLimit
}
