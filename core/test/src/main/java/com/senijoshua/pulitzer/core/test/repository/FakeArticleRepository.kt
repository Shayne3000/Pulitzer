package com.senijoshua.pulitzer.core.test.repository

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.core.test.model.fakeArticleList
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * "Working implementation" of [ArticleRepository] for testing purposes.
 */
class FakeArticleRepository : ArticleRepository {
    private val errorMessage = "Error!"
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

    override suspend fun getArticleGivenId(articleId: String): Flow<Result<Article>> {
        TODO("Not yet implemented")
    }

    override suspend fun getBookmarkedArticles(searchQuery: String): Flow<Result<List<Article>>> {
        TODO("Not yet implemented")
    }

    override suspend fun bookmarkArticle(articleId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun unBookmarkArticles(articleIds: List<String>) {
        TODO("Not yet implemented")
    }
}
