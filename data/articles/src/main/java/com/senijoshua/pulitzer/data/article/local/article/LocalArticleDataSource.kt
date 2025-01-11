package com.senijoshua.pulitzer.data.article.local.article

import androidx.paging.PagingSource
import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import com.senijoshua.pulitzer.core.database.entity.BookmarkedArticles
import kotlinx.coroutines.flow.Flow

/**
 * Interface through which higher elements in the architectural
 * hierarchy (i.e. repository) can interact (e.g. perform operations)
 * with the Articles table in the local database.
 *
 * It abstracts away implementation details of retrieving data from the
 * database and is the contract that must be implemented to perform
 * said sourcing operation.
 */
internal interface LocalArticleDataSource {
    suspend fun insertArticles(articles: List<ArticleEntity>)
    fun getArticlesFromDB(): Flow<List<ArticleEntity>>
    fun getPagedArticlesFromDB(): PagingSource<Int, ArticleEntity>
    fun getArticleById(articleId: String): Flow<ArticleEntity>
    fun getBookmarkedArticles(searchQuery: String): Flow<List<BookmarkedArticles>>
    suspend fun bookmarkArticle(articleId: String)
    suspend fun unBookmarkArticles(articleIds: List<String>)
    suspend fun clearArticles()
    fun getTimeCreated(): Long?
}
