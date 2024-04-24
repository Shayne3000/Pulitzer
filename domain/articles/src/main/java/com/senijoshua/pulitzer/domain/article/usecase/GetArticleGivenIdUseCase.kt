package com.senijoshua.pulitzer.domain.article.usecase

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Represents one of the core functionalities of the app which is to get the
 * article whose id matches the supplied id
 */
class GetArticleGivenIdUseCase @Inject constructor(private val repository: ArticleRepository){
    operator fun invoke(articleId: String): Flow<Result<Article>> = repository.getArticleGivenId(articleId)
}
