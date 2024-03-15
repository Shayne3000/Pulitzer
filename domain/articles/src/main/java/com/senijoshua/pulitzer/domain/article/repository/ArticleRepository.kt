package com.senijoshua.pulitzer.domain.article.repository

/**
 * Interface that serves as the contract a Repository in the data layer must implement
 * to provision data from local and remote data sources to the domain layer.
 */
interface ArticleRepository {
    suspend fun getArticles()
}
