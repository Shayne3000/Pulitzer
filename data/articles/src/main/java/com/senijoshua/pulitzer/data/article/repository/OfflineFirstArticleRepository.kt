package com.senijoshua.pulitzer.data.article.repository

import com.senijoshua.pulitzer.core.datastore.repository.PagingPreferences
import com.senijoshua.pulitzer.core.model.GlobalConstants
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.core.model.toResult
import com.senijoshua.pulitzer.data.article.local.DbCacheLimit
import com.senijoshua.pulitzer.data.article.local.article.LocalArticleDataSource
import com.senijoshua.pulitzer.data.article.mapper.toDomain
import com.senijoshua.pulitzer.data.article.mapper.toDomainFormat
import com.senijoshua.pulitzer.data.article.mapper.toLocalFormat
import com.senijoshua.pulitzer.data.article.remote.RemoteArticleDataSource
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
    private val local: LocalArticleDataSource,
    private val remote: RemoteArticleDataSource,
    private val pagingPreference: PagingPreferences,
    private val dispatcher: CoroutineDispatcher,
    private val cacheLimit: DbCacheLimit,
) : ArticleRepository {
    // Store internal page, offset and limit here and it should persist for the lifetime of the app since this class has singleton scope
    private var isPagingInternally: Boolean = false

    override suspend fun getPagedArticles(page: Int, isRefresh: Boolean, isPaging: Boolean): Flow<Result<List<Article>>> {
        val offset = (page - GlobalConstants.INITIAL_PAGE) * GlobalConstants.PAGE_SIZE
        isPagingInternally = isPaging

        return withContext(dispatcher) {
            if (isRefresh || (System.currentTimeMillis() - (local.getTimeCreated()
                    ?: 0) > cacheLimit.clearCacheLimit)
            ) {
                // Clear DB if refresh OR if createdtime has exceeded 48 hours
                local.clearArticles()
            }
            local.getArticlesFromDB().map { articles ->
                articles.toDomainFormat()
            }.onEach { domainArticles ->
                if (domainArticles.isEmpty()) {
                    // Load from network and insert into DB
                    loadAndPersistNetworkData(page)
                } else {
                    // Check if is Paging,
                    if (isPaging && isPagingInternally) {
                        loadAndPersistNetworkData(page)
                        isPagingInternally = false
                    }
                    // If it is, then load from the network, insert into the DB and then do the below.
                }
            }.map { domainArticles ->
                if (isPaging) {
                    // manipulate the returned DB list to only return list item whose index matches the offset if paging is true else return list as-is
                    if (domainArticles.size > offset) {
                        domainArticles.subList(offset, offset + GlobalConstants.PAGE_SIZE)
                    } else {
                        domainArticles
                    }
                } else {
                    // If it isn't paging, then just return everything in the DB.
                    domainArticles
                }
            }.toResult()
        }
    }

    private suspend fun loadAndPersistNetworkData(page: Int) {
        val networkResponse = remote.getPagedArticlesFromServer(page = page)
        val articlesList = networkResponse.results
        var currentPage = networkResponse.currentPage

        local.insertArticles(articlesList.toLocalFormat())
        if (articlesList.isNotEmpty()) {
            currentPage++
            pagingPreference.setNextPageToLoad(currentPage)
        }
    }

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
        isLimitExceeded(cacheLimit.clearCacheLimit)

    /**
     * We delete data from the DB if the data in the DB is stale.
     */
    private fun shouldClearStaleData(): Boolean = isLimitExceeded(cacheLimit.clearCacheLimit)

    private fun isLimitExceeded(cacheLimit: Long) =
        (System.currentTimeMillis() - (local.getTimeCreated() ?: 0)) > cacheLimit
}
