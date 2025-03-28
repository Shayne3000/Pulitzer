package com.senijoshua.pulitzer.data.article.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import com.senijoshua.pulitzer.data.article.local.TestLocalArticleDataSource
import java.util.Date

@OptIn(ExperimentalPagingApi::class)
internal class TestRemoteMediator(
    private val localArticleDataSource: TestLocalArticleDataSource,
) : RemoteMediator<Int, ArticleEntity>() {
    val articles = List(20) { index ->
        ArticleEntity(
            id = index.toString(),
            thumbnail = "$index thumbnail",
            title = "$index title",
            author = "$index author",
            body = "$index body",
            isBookmarked = index % 2 == 0,
            lastModifiedDate = Date(),
            createdAt = System.currentTimeMillis(),
        )
    }
    var shouldShowError = false
    val errorMessage = "Error."

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        return if (shouldShowError) {
           MediatorResult.Error(Throwable(errorMessage))
        }  else {
            localArticleDataSource.insertArticles(articles)
            MediatorResult.Success(endOfPaginationReached = true)
        }
    }
}
