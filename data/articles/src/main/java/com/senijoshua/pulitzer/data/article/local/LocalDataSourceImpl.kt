package com.senijoshua.pulitzer.data.article.local

import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    // TODO Add iODispatcher
) : LocalDataSource{
    override suspend fun getArticles(): Flow<List<ArticleEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertArticles(articles: List<ArticleEntity>) {
        TODO("Not yet implemented")
    }
}
