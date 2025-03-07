package com.senijoshua.pulitzer.domain.article.usecase

import androidx.paging.testing.asSnapshot
import com.senijoshua.pulitzer.domain.article.entity.fakeArticleList
import com.senijoshua.pulitzer.domain.article.repository.TestArticleRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPagedArticlesUseCaseTest {
    private lateinit var repository: TestArticleRepository
    private lateinit var useCase: GetPagedArticlesUseCase

    @Before
    fun setUp() {
        repository = TestArticleRepository()
        useCase = GetPagedArticlesUseCase(repository)
    }

    @Test
    fun `useCase returns PagingData with a list of Articles on successful call`() = runTest {
        val pagingData = useCase().asSnapshot()

        assertTrue(pagingData.isNotEmpty())
        assertEquals(fakeArticleList[0].id, pagingData[0].id)
    }

    @Test
    fun `useCase returns empty list on error`() = runTest {
        repository.shouldThrowError = true

        val pagingData = useCase().asSnapshot()

        assertTrue(pagingData.isEmpty())
    }
}
