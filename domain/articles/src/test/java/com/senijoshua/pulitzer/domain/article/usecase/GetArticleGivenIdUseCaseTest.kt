package com.senijoshua.pulitzer.domain.article.usecase

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.core.test.model.fakeArticleList
import com.senijoshua.pulitzer.core.test.repository.FakeArticleRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetArticleGivenIdUseCaseTest {
    // Fake dependencies
    private lateinit var repository: FakeArticleRepository

    // Setup class under test
    private lateinit var useCase: GetArticleGivenIdUseCase

    @Before
    fun setUp() {
        repository = FakeArticleRepository()
        useCase = GetArticleGivenIdUseCase(repository)
    }

    @Test
    fun `useCase returns article on successful response given the id`() = runTest {
        val articleId = fakeArticleList[2].id

        val result = useCase(articleId).first()

        check(result is Result.Success)
        assertEquals(articleId, result.data.id)
    }

    @Test
    fun `useCase returns error on unsuccessful response`() = runTest {
        repository.shouldThrowError = true
        val articleId = fakeArticleList[2].id

        val result = useCase(articleId).first()

        check(result is Result.Error)
        assertEquals(repository.errorMessage, result.error.message)
    }
}
