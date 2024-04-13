package com.senijoshua.pulitzer.domain.article.usecase

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import javax.inject.Inject

/**
 * Represents one of the core functionalities of the app which is to get the
 * article whose id matches the supplied id
 */
class GetArticleGivenIdUseCase @Inject constructor(private val repository: ArticleRepository){
    // TODO May have to return a Flow so that we can get the updated article
    suspend operator fun invoke(articleId: String): Result<Article> = repository.getArticleGivenId(articleId)
}
