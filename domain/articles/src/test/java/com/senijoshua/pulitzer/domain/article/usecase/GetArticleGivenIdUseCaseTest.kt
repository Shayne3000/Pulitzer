package com.senijoshua.pulitzer.domain.article.usecase

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.entity.fakeArticleList
import com.senijoshua.pulitzer.domain.article.repository.TestArticleRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetArticleGivenIdUseCaseTest {
    private lateinit var repository: TestArticleRepository
    private lateinit var useCase: GetArticleGivenIdUseCase
    private val articleId = fakeArticleList[2].id

    @Before
    fun setUp() {
        repository = TestArticleRepository()
        useCase = GetArticleGivenIdUseCase(repository)
    }

    @Test
    fun `useCase returns article on successful response given the id`() = runTest {
        val result = useCase(articleId).first()

        check(result is Result.Success)
        assertEquals(articleId, result.data.id)
    }

    @Test
    fun `useCase returns error on unsuccessful response`() = runTest {
        repository.shouldThrowError = true

        val result = useCase(articleId).first()

        check(result is Result.Error)
        assertEquals(repository.errorMessage, result.error.message)
    }
}
