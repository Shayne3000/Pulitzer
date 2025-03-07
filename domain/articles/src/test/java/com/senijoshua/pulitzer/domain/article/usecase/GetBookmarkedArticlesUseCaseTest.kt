package com.senijoshua.pulitzer.domain.article.usecase

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.repository.TestArticleRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetBookmarkedArticlesUseCaseTest {
    private lateinit var repository: TestArticleRepository
    private lateinit var useCase: GetBookmarkedArticlesUseCase
    private val searchQuery = "title"

    @Before
    fun setUp() {
        repository = TestArticleRepository()
        useCase = GetBookmarkedArticlesUseCase(repository)
    }

    @Test
    fun `useCase returns a list of bookmarked articles on successful call`() = runTest {
        val articles = useCase(searchQuery).first()

        check(articles is Result.Success)
        assertTrue(articles.data.isNotEmpty())
        assertTrue(articles.data[0].isBookmarked)
    }

    @Test
    fun `useCase returns error on unsuccessful call`() = runTest {
        repository.shouldThrowError = true

        val articles = useCase(searchQuery).first()

        check(articles is Result.Error)
        assertEquals(repository.errorMessage, articles.error.message)
    }
}
