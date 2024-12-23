package com.senijoshua.pulitzer.domain.article.usecase

import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import javax.inject.Inject

/**
 * Represents the core app functionality of un-bookmarking one or more articles at once.
 */
class UnbookmarkArticlesUseCase @Inject constructor(private val repository: ArticleRepository){
    suspend operator fun invoke(articleIds: List<String>) =
        repository.unBookmarkArticles(articleIds)
}
