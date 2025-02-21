package com.senijoshua.pulitzer.data.article.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import com.senijoshua.pulitzer.core.database.entity.RemoteKeyEntity
import com.senijoshua.pulitzer.core.database.utils.PulitzerDbTransactionProvider
import com.senijoshua.pulitzer.core.model.GlobalConstants
import com.senijoshua.pulitzer.data.article.local.DbCacheLimit
import com.senijoshua.pulitzer.data.article.local.article.LocalArticleDataSource
import com.senijoshua.pulitzer.data.article.local.remotekey.LocalRemoteKeyDataSource
import com.senijoshua.pulitzer.data.article.mapper.toLocalFormat
import com.senijoshua.pulitzer.data.article.remote.RemoteArticleDataSource
import javax.inject.Inject

/**
 * More of a helper class that facilitates the pagination of articles from the backend.
 * Its primary role is to load more data from the network when either the Pager runs
 * out of cached paged data or the existing cached paged data is invalidated.
 */
@OptIn(ExperimentalPagingApi::class)
internal class ArticleRemoteMediator @Inject constructor(
    private val dbTransactionProvider: PulitzerDbTransactionProvider,
    private val localArticleSource: LocalArticleDataSource,
    private val localRemoteKeySource: LocalRemoteKeyDataSource,
    private val remote: RemoteArticleDataSource,
    private val cacheLimit: DbCacheLimit,
) : RemoteMediator<Int, ArticleEntity>() {

    override suspend fun initialize(): InitializeAction {
        return if (System.currentTimeMillis() - (localRemoteKeySource.getCreatedTime()
                ?: 0) <= cacheLimit.clearCacheLimit
        ) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        val pageToLoad: Int = when (loadType) {
            LoadType.REFRESH -> GlobalConstants.INITIAL_PAGE
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeyForLastArticleItem = state.lastItemOrNull()?.let { article ->
                    localRemoteKeySource.getRemoteKeyGivenArticleId(article.id)
                }

                remoteKeyForLastArticleItem?.nextPageKey
                    ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeyForLastArticleItem != null
                    )
            }
        }

        try {
            val response = remote.getPagedArticlesFromServer(pageToLoad)
            val articles = response.results
            val loadedPage = response.currentPage
            val endOfPaginationReached = articles.isEmpty()

            dbTransactionProvider.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localArticleSource.clearArticles()
                    localRemoteKeySource.clearRemoteKeys()
                }

                val prevPageKey = if (loadedPage > 1) loadedPage - 1 else null
                val nextPageKey = if (endOfPaginationReached) null else loadedPage + 1

                val remoteKeys = articles.map { article ->
                    RemoteKeyEntity(
                        articleId = article.id,
                        prevPageKey = prevPageKey,
                        currentPageKey = loadedPage,
                        nextPageKey = nextPageKey
                    )
                }

                localArticleSource.insertArticles(articles.toLocalFormat())
                localRemoteKeySource.insertRemoteKeys(remoteKeys)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            if (state.isEmpty()) {
                // No locally-cached paged data either so send an
                // error of "no data" state to the presentation layer
                return MediatorResult.Error(exception)
            }

            // Return the paged data cached in the DB
            return MediatorResult.Success(endOfPaginationReached = true)
        }
    }
}
