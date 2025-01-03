package com.senijoshua.pulitzer.domain.article.usecase

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Represents the core app functionality of getting a list of all bookmarked articles
 */
class GetBookmarkedArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository,
){
    suspend operator fun invoke(searchQuery: String): Flow<Result<List<Article>>> =
        repository.getBookmarkedArticles(searchQuery = searchQuery)
}
