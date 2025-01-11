package com.senijoshua.pulitzer.domain.article.usecase

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Represents the core app functionality/use case of retrieving a list of news articles.
 */
class GetArticlesUseCase @Inject constructor(private val repository: ArticleRepository) {
    suspend operator fun invoke(): Flow<Result<List<Article>>> = repository.getArticles()
}
