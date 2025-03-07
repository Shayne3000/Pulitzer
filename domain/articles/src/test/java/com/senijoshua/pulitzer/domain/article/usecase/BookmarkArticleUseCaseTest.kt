package com.senijoshua.pulitzer.domain.article.usecase

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.entity.Article
import com.senijoshua.pulitzer.domain.article.entity.fakeArticleList
import com.senijoshua.pulitzer.domain.article.repository.TestArticleRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BookmarkArticleUseCaseTest {
    private lateinit var repository: TestArticleRepository

    private lateinit var useCase: BookmarkArticleUseCase

    @Before
    fun setUp() {
        repository = TestArticleRepository()
        useCase = BookmarkArticleUseCase(repository)
    }

    @Test
    fun `useCase bookmarks article on successful call`() = runTest {
        val articleId = fakeArticleList[5].id
        assertFalse(getArticle(articleId).isBookmarked)

        useCase(articleId)

        assertTrue(getArticle(articleId).isBookmarked)
    }

    @Test
    fun `useCase does not bookmark article on error`() = runTest {
        val articleId = fakeArticleList[5].id
        assertFalse(getArticle(articleId).isBookmarked)

        repository.shouldThrowError = true
        useCase(articleId)

        repository.shouldThrowError = false
        assertFalse(getArticle(articleId).isBookmarked)
    }

    private suspend fun getArticle(articleId: String): Article {
        val articleState = repository.getArticleGivenId(articleId).first()
        check(articleState is Result.Success)
        return articleState.data
    }
}
