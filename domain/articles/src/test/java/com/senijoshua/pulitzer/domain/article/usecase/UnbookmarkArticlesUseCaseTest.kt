package com.senijoshua.pulitzer.domain.article.usecase

import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.entity.fakeArticleList
import com.senijoshua.pulitzer.domain.article.repository.TestArticleRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UnbookmarkArticlesUseCaseTest {
    private lateinit var repository: TestArticleRepository
    private lateinit var useCase: UnbookmarkArticlesUseCase
    private val articleIds =
        listOf(fakeArticleList[0].id, fakeArticleList[2].id, fakeArticleList[4].id)

    @Before
    fun setUp() {
        repository = TestArticleRepository()
        useCase = UnbookmarkArticlesUseCase(repository)
    }

    @Test
    fun `Given bookmarked article ids, useCase unbookmarks them on successful call`() =
        runTest {
            verifyArticlesAreBookmarked()

            useCase(articleIds)

            verifyArticlesAreUnBookmarked()
        }

    @Test
    fun `Given bookmarked article ids, useCase does not unBookmark articles if error occurs`() =
        runTest {
            verifyArticlesAreBookmarked()
            repository.shouldThrowError = true

            useCase(articleIds)

            repository.shouldThrowError = false
            verifyArticlesAreBookmarked()
        }

    private suspend fun verifyArticlesAreBookmarked() {
        assertBookmarkedStatus(articleIds[0])
        assertBookmarkedStatus(articleIds[1])
        assertBookmarkedStatus(articleIds[2])
    }

    private suspend fun verifyArticlesAreUnBookmarked() {
        assertUnBookmarkedStatus(articleIds[0])
        assertUnBookmarkedStatus(articleIds[1])
        assertUnBookmarkedStatus(articleIds[2])
    }

    private suspend fun assertBookmarkedStatus(articleId: String) {
        val articleState = repository.getArticleGivenId(articleId).first()
        check(articleState is Result.Success)
        assertTrue(articleState.data.isBookmarked)
    }

    private suspend fun assertUnBookmarkedStatus(articleId: String) {
        val articleState = repository.getArticleGivenId(articleId).first()
        check(articleState is Result.Success)
        assertFalse(articleState.data.isBookmarked)
    }
}
