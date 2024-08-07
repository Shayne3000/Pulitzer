package com.senijoshua.pulitzer.data.article.remote

import com.senijoshua.pulitzer.core.network.model.NetworkArticle
import com.senijoshua.pulitzer.core.network.remote.ArticleApi
import javax.inject.Inject

/**
 * Implementation of the [RemoteDataSource] that interacts with the REST client
 * that executes network requests to retrieve data from the Guardian web service.
 */
internal class RemoteDataSourceImpl @Inject constructor(
    private val api: ArticleApi
) : RemoteDataSource {
    override suspend fun getArticlesFromServer(): List<NetworkArticle> {
       val newsArticles = api.getNewsArticles()
        return newsArticles.response.results
    }
}
