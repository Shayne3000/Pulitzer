package com.senijoshua.pulitzer.data.article.remote

import com.senijoshua.pulitzer.core.network.model.NetworkArticle
import com.senijoshua.pulitzer.core.network.model.NetworkArticleResults
import com.senijoshua.pulitzer.core.network.remote.ArticleApi
import javax.inject.Inject

/**
 * Implementation of the [RemoteArticleDataSource] that interacts with the REST client
 * that executes network requests to retrieve data from the Guardian web service.
 */
internal class RemoteArticleDataSourceImpl @Inject constructor(
    private val api: ArticleApi
) : RemoteArticleDataSource {
    override suspend fun getArticlesFromServer(): List<NetworkArticle> {
       val newsArticles = api.getNewsArticles()
        return newsArticles.response.results
    }

    override suspend fun getPagedArticlesFromServer(page: Int): NetworkArticleResults {
        val pagedNewsArticles = api.getNewsArticles(page = page)
        return pagedNewsArticles.response
    }
}
