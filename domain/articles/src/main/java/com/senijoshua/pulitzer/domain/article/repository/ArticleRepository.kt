package com.senijoshua.pulitzer.domain.article.repository

import androidx.paging.PagingData
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.entity.Article
import kotlinx.coroutines.flow.Flow

/**
 * Interface that specifies the contract a Repository in the data layer must implement
 * to provision article data from the data layer (in an offline-first manner) to the domain layer.
 */
interface ArticleRepository {
    suspend fun getPagedArticles(): Flow<PagingData<Article>>
    suspend fun getArticleGivenId(articleId: String): Flow<Result<Article>>
    suspend fun getBookmarkedArticles(searchQuery: String): Flow<Result<List<Article>>>
    suspend fun bookmarkArticle(articleId: String)
    suspend fun unBookmarkArticles(articleIds: List<String>)
}
