@file:OptIn(ExperimentalPagingApi::class)

package com.senijoshua.pulitzer.data.article.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import com.senijoshua.pulitzer.core.model.GlobalConstants
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.core.model.toResult
import com.senijoshua.pulitzer.data.article.local.article.LocalArticleDataSource
import com.senijoshua.pulitzer.data.article.mapper.toDomain
import com.senijoshua.pulitzer.data.article.mapper.toDomainFormat
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
    private val remoteMediator: RemoteMediator<Int, ArticleEntity>,
    private val dispatcher: CoroutineDispatcher,
) : ArticleRepository {
    override suspend fun getPagedArticles(): Flow<PagingData<Article>> {
        return withContext(dispatcher) {
            Pager(
                config = PagingConfig(
                    pageSize = GlobalConstants.PAGE_SIZE,
                    prefetchDistance = 0,
                    initialLoadSize = GlobalConstants.PAGE_SIZE,
                ),
                remoteMediator = remoteMediator,
                pagingSourceFactory = { local.getPagedArticlesFromDB() }
            ).flow.map { pagingData: PagingData<ArticleEntity> ->
                pagingData.map { articleEntity ->
                    articleEntity.toDomainFormat()
                }
            }
        }
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
}
