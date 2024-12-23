package com.senijoshua.pulitzer.data.article.repository

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.core.model.toResult
import com.senijoshua.pulitzer.data.article.local.DbCacheLimit
import com.senijoshua.pulitzer.data.article.local.LocalDataSource
import com.senijoshua.pulitzer.data.article.mapper.toDomain
import com.senijoshua.pulitzer.data.article.mapper.toDomainFormat
import com.senijoshua.pulitzer.data.article.mapper.toLocalFormat
import com.senijoshua.pulitzer.data.article.remote.RemoteDataSource
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * [ArticleRepository] implementation that aggregates [Article] data
 * from local and remote data sources in an offline-first manner and
 * returns it to the domain layer.
 *
 * This abstracts away data retrieval operations/logic from higher layers in
 * the architecture.
 */
internal class OfflineFirstArticleRepository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val cacheLimit: DbCacheLimit,
) : ArticleRepository {

    override suspend fun getArticles(): Flow<Result<List<Article>>> {
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
        }.distinctUntilChanged().flowOn(dispatcher).toResult()
    }

    override suspend fun getArticleGivenId(articleId: String): Flow<Result<Article>> {
        return local.getArticleById(articleId).map { article ->
            article.toDomainFormat()
        }.flowOn(dispatcher).toResult()
    }

    override suspend fun getBookmarkedArticles(searchQuery: String): Flow<Result<List<Article>>> {
        return local.getBookmarkedArticles(searchQuery = searchQuery)
            .distinctUntilChanged()
            .map { bookmarkArticles -> bookmarkArticles.toDomain() }
            .flowOn(dispatcher)
            .toResult()
    }

    override suspend fun bookmarkArticle(articleId: String) {
        withContext(dispatcher) {
            local.bookmarkArticle(articleId)
        }
    }

    override suspend fun unBookmarkArticles(articleIds: List<String>) {
        withContext(dispatcher) {
            local.unBookmarkArticles(articleIds)
        }
    }

    /**
     * We load fresh data from the remote service only if the data in the DB is old or
     * if the database is empty.
     */
    private fun shouldLoadArticlesFromRemoteService() =
        isLimitExceeded(cacheLimit.refreshCacheLimit)

    /**
     * We delete data from the DB if the data in the DB is stale.
     */
    private fun shouldClearStaleData(): Boolean = isLimitExceeded(cacheLimit.clearCacheLimit)

    private fun isLimitExceeded(cacheLimit: Long) =
        (System.currentTimeMillis() - (local.getTimeCreated() ?: 0)) > cacheLimit
}
