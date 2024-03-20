package com.senijoshua.pulitzer.data.article.repository

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.data.article.local.LocalDataSource
import com.senijoshua.pulitzer.data.article.remote.RemoteDataSource
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * [ArticleRepository] implementation that aggregates [Article] data
 * from local and remote data sources in an offline-first manner and
 * returns it to the data layer.
 *
 * This abstracts away data retrieval operations from higher layers in
 * the architecture.
 */
class OfflineFirstArticleRepository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
): ArticleRepository {

    override suspend fun getArticles(): Flow<Result<List<Article>>> {
        local.getArticlesFromDB().map {

        }
    }

    override suspend fun getArticleGivenId(articleId: Int): Result<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun getBookmarkedArticles(): Flow<Result<List<Article>>> {
        TODO("Not yet implemented")
    }
}
