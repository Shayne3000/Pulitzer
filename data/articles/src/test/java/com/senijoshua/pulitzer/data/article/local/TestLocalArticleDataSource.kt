package com.senijoshua.pulitzer.data.article.local

import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import com.senijoshua.pulitzer.core.database.entity.BookmarkedArticles
import com.senijoshua.pulitzer.data.article.local.article.LocalArticleDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * "Working implementation" of [LocalArticleDataSource] for testing purposes.
 */
class TestLocalArticleDataSource : LocalArticleDataSource {
    private var articleTable = mutableListOf<ArticleEntity>()
    var shouldThrowError = false
    val errorMessage = "Error!"

    override suspend fun insertArticles(articles: List<ArticleEntity>) {
        articleTable += articles
    }

    override fun getPagedArticlesFromDB(): PagingSource<Int, ArticleEntity> {
        val pagingSourceFactory = if (shouldThrowError) {
            val emptyArticles = mutableListOf<ArticleEntity>()
            emptyArticles.asPagingSourceFactory()
        } else {
            articleTable.asPagingSourceFactory()
        }
        return pagingSourceFactory()
    }

    override fun getArticleById(articleId: String): Flow<ArticleEntity> = flow {
        if (shouldThrowError) {
            throw Throwable(errorMessage)
        } else {
            emit(articleTable.first { it.id == articleId })
        }
    }

    override fun getBookmarkedArticles(searchQuery: String): Flow<List<BookmarkedArticles>> = flow {
        val bookmarkedArticles = mutableListOf<BookmarkedArticles>()
        if (shouldThrowError) {
            emit(bookmarkedArticles)
        } else {
            val bookmarks = articleTable.filter { article ->
                article.isBookmarked && article.title.contains(searchQuery)
            }

            bookmarkedArticles.addAll(bookmarks.map { articleEntity ->
                BookmarkedArticles(
                    id = articleEntity.id,
                    thumbnail = articleEntity.thumbnail,
                    title = articleEntity.title,
                    author = articleEntity.author,
                )
            })
            emit(bookmarkedArticles)
        }
    }

    override suspend fun bookmarkArticle(articleId: String) {
        if (!shouldThrowError) {
            val articleIndex = articleTable.indexOfFirst { it.id == articleId }
            articleTable[articleIndex] = articleTable[articleIndex].copy(isBookmarked = true)
        }
    }

    override suspend fun unBookmarkArticles(articleIds: List<String>) {
        if (shouldThrowError) {
            return
        }

        articleTable.forEachIndexed { index, article ->
            if (article.id in articleIds && article.isBookmarked) {
                articleTable[index] = articleTable[index].copy(isBookmarked = false)
            }
        }
    }

    override suspend fun clearArticles() {
        articleTable = mutableListOf()
    }

    override fun getTimeCreated(): Long? = System.currentTimeMillis()
}
