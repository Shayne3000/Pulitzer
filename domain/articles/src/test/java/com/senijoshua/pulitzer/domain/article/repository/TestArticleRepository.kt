package com.senijoshua.pulitzer.domain.article.repository

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.entity.fakeArticleList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * "Working implementation" of [ArticleRepository] for testing purposes.
 */
internal class TestArticleRepository : ArticleRepository {
    private var articleStorage = fakeArticleList.toMutableList()
    val errorMessage = "Error!"
    var shouldThrowError = false

    override suspend fun getPagedArticles(): Flow<PagingData<Article>> = flow {
        emit(
            if (shouldThrowError) {
                PagingData.from(
                    data = emptyList(),
                    sourceLoadStates = LoadStates(
                        refresh = LoadState.Error(Throwable(errorMessage)),
                        prepend = LoadState.NotLoading(true),
                        append = LoadState.NotLoading(true)
                    )
                )
            } else {
                PagingData.from(fakeArticleList)
            }
        )
    }

    override suspend fun getArticleGivenId(articleId: String): Flow<Result<Article>> = flow {
        emit(
            if (shouldThrowError) {
                Result.Error(Throwable(errorMessage))
            } else {
                Result.Success(getArticleFromStorageGivenId(articleId))
            }
        )
    }

    override suspend fun getBookmarkedArticles(searchQuery: String): Flow<Result<List<Article>>> =
        flow {
            emit(
                if (shouldThrowError) {
                    Result.Error(Throwable(errorMessage))
                } else {
                    Result.Success(getArticleFromStorageMatchesQuery(searchQuery))
                }
            )
        }

    override suspend fun bookmarkArticle(articleId: String) {
        if (!shouldThrowError) {
            val articleIndex = articleStorage.indexOfFirst { it.id == articleId }
            articleStorage[articleIndex] = articleStorage[articleIndex].copy(isBookmarked = true)
        }
    }

    override suspend fun unBookmarkArticles(articleIds: List<String>) {
        if (shouldThrowError) {
            return
        }

        articleStorage.forEachIndexed { index, article ->
            if (article.id in articleIds && article.isBookmarked) {
                articleStorage[index] = articleStorage[index].copy(isBookmarked = false)
            }
        }
    }

    private fun getArticleFromStorageGivenId(articleId: String) =
        articleStorage.find { it.id == articleId }!!

    private fun getArticleFromStorageMatchesQuery(searchQuery: String) =
        articleStorage.filter { article ->
            article.isBookmarked && article.title.contains(searchQuery)
        }
}
