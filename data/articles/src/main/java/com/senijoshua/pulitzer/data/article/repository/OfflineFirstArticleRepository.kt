package com.senijoshua.pulitzer.data.article.repository

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * [ArticleRepository] implementation that retrieves [Article] data
 * aggregated from local and remote data sources in an offline-first manner.
 *
 * This abstracts away data retrieval operations from higher layers in
 * the architecture.
 */
internal class OfflineFirstArticleRepository @Inject constructor(
// TODO Add iODispatcher in addition to the other
): ArticleRepository {

    override suspend fun getArticles(): Flow<Result<List<Article>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getArticleGivenId(articleId: Int): Result<Article> {
        TODO("Not yet implemented")
    }

    override suspend fun getBookmarkedArticles(): Flow<Result<List<Article>>> {
        TODO("Not yet implemented")
    }
}
