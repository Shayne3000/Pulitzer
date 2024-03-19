package com.senijoshua.pulitzer.data.article.remote

import com.senijoshua.pulitzer.core.network.model.NetworkArticle

/**
 * Interface through which the Repository can interact
 * with the remote web service and retrieve article data.
 *
 * It abstracts away implementation details of retrieving data from the
 * remote service and is the contract that must be implemented to perform
 * said sourcing operation.
 */
internal interface RemoteDataSource {
    suspend fun getArticlesFromServer(): List<NetworkArticle>
}
