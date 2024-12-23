package com.senijoshua.pulitzer.domain.article.usecase

import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import javax.inject.Inject

/**
 * Represents the core app functionality of bookmarking and un-bookmarking a single article.
 */
class BookmarkArticleUseCase @Inject constructor(private val repository: ArticleRepository){
    suspend operator fun invoke(articleId: String) = repository.bookmarkArticle(articleId)
}
