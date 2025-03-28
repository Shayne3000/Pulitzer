package com.senijoshua.pulitzer.data.article.repository

import androidx.paging.ExperimentalPagingApi
import app.cash.turbine.test
import com.senijoshua.pulitzer.core.test.rules.MainDispatcherRule
import com.senijoshua.pulitzer.core.test.utils.collectPagingData
import com.senijoshua.pulitzer.data.article.local.TestLocalArticleDataSource
import com.senijoshua.pulitzer.data.article.mapper.toDomainFormat
import com.senijoshua.pulitzer.domain.article.entity.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    fun `getPagedArticles returns empty list on unsuccessful request`() = runTest {
        // arrange

        // act

        // assert
    }
}
