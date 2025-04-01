package com.senijoshua.pulitzer.data.article.repository

import androidx.paging.ExperimentalPagingApi
import app.cash.turbine.test
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.core.test.rules.MainDispatcherRule
import com.senijoshua.pulitzer.core.test.utils.collectPagingData
import com.senijoshua.pulitzer.data.article.local.TestLocalArticleDataSource
import com.senijoshua.pulitzer.data.article.mapper.toDomainFormat
import com.senijoshua.pulitzer.domain.article.entity.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
class OfflineFirstArticleRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var remoteMediator: TestRemoteMediator
    private lateinit var local: TestLocalArticleDataSource
    private lateinit var repo: OfflineFirstArticleRepository
    private val testArticleId = "1"

    @Before
    fun setUp() {
        local = TestLocalArticleDataSource()
        remoteMediator = TestRemoteMediator(local)
        repo = OfflineFirstArticleRepository(local, remoteMediator, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `getPagedArticles returns paged list of articles on successful request`() =
        runTest {
            val expectedPagedList = remoteMediator.articles.toDomainFormat()
            var actualPagedList: MutableList<Article>

            repo.getPagedArticles().test {
                actualPagedList = awaitItem().collectPagingData()[0].toMutableList()
                assertTrue(actualPagedList.isNotEmpty())
                assertEquals(expectedPagedList, actualPagedList)
            }
        }

    @Test
    fun `getPagedArticles returns no paged data on unsuccessful request`() = runTest {
        remoteMediator.shouldThrowError = true

        repo.getPagedArticles().test {
            val pagedList = awaitItem().collectPagingData()
            assertTrue(pagedList.isEmpty())
        }
    }

    @Test
    fun `getArticleGivenId returns domain article on success`() = runTest {
        local.insertArticles(remoteMediator.articles)

        repo.getArticleGivenId(testArticleId).test {
            val result = awaitItem()
            check(result is Result.Success)
            assertEquals(
                remoteMediator.articles.toDomainFormat()[testArticleId.toInt()],
                result.data
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getArticleGivenId returns error on unsuccessful request`() = runTest {
        local.shouldThrowError = true

        repo.getArticleGivenId(testArticleId).test {
            val result = awaitItem()
            check(result is Result.Error)
            assertEquals(local.errorMessage, result.error.message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getBookmarkedArticles returns list of bookmarked articles on success`() = runTest {
        // arrange
        val searchQuery = "2 title"
        local.insertArticles(remoteMediator.articles)

        // act
        repo.getBookmarkedArticles(remoteMediator.articles[2].title).test {
            val result = awaitItem()
            check(result is Result.Success)
            val bookmarkedArticle = result.data[0]
            assertEquals(searchQuery, bookmarkedArticle.title)
            assertTrue(bookmarkedArticle.isBookmarked)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getBookmarkedArticles returns empty list on failure`() = runTest {
        local.shouldThrowError = true

        repo.getBookmarkedArticles(remoteMediator.articles[0].title).test {
            val result = awaitItem()
            check(result is Result.Success)
            val articles = result.data
            assertTrue(articles.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `bookmarkArticle sets article bookmarked status to true on success`() = runTest {
        val articleId = remoteMediator.articles[1].id
        local.insertArticles(remoteMediator.articles)

        verifyBookmarkedStatus(articleId, shouldAssertTrue = false)

        repo.bookmarkArticle(articleId)

        verifyBookmarkedStatus(articleId, shouldAssertTrue = true)
    }

    @Test
    fun `bookmarkArticle does not update article bookmarked status on failure`() = runTest {
        val articleId = remoteMediator.articles[1].id
        local.insertArticles(remoteMediator.articles)

        verifyBookmarkedStatus(articleId, shouldAssertTrue = false)

        local.shouldThrowError = true

        repo.bookmarkArticle(articleId)

        local.shouldThrowError = false
        verifyBookmarkedStatus(articleId, shouldAssertTrue = false)
    }

    @Test
    fun `unBookmarkArticles sets the status of a list of bookmarkedArticles to false on success`() =
        runTest {
            val articleZeroId = remoteMediator.articles[0].id
            val articleFourId = remoteMediator.articles[4].id

            val bookmarkedArticles = listOf(articleZeroId, articleFourId)

            local.insertArticles(remoteMediator.articles)

            verifyBookmarkedStatus(articleZeroId, shouldAssertTrue = true)
            verifyBookmarkedStatus(articleFourId, shouldAssertTrue = true)

            repo.unBookmarkArticles(bookmarkedArticles)

            verifyBookmarkedStatus(articleZeroId, shouldAssertTrue = false)
            verifyBookmarkedStatus(articleFourId, shouldAssertTrue = false)
        }

    @Test
    fun `unBookmarkArticles does not modify the bookmarked status of bookmarkedArticles on error `() =
        runTest {
            val articleZeroId = remoteMediator.articles[0].id
            val articleFourId = remoteMediator.articles[4].id

            val bookmarkedArticles = listOf(articleZeroId, articleFourId)

            local.insertArticles(remoteMediator.articles)

            verifyBookmarkedStatus(articleZeroId, shouldAssertTrue = true)
            verifyBookmarkedStatus(articleFourId, shouldAssertTrue = true)

            local.shouldThrowError = true

            repo.unBookmarkArticles(bookmarkedArticles)

            local.shouldThrowError = false

            verifyBookmarkedStatus(articleZeroId, shouldAssertTrue = true)
            verifyBookmarkedStatus(articleFourId, shouldAssertTrue = true)
        }

    private suspend fun verifyBookmarkedStatus(articleId: String, shouldAssertTrue: Boolean) {
        val article = repo.getArticleGivenId(articleId).first()
        check(article is Result.Success)
        if (shouldAssertTrue) {
            assertTrue(article.data.isBookmarked)
        } else {
            assertFalse(article.data.isBookmarked)
        }
    }
}
