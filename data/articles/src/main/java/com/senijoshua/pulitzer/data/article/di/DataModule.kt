package com.senijoshua.pulitzer.data.article.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import com.senijoshua.pulitzer.data.article.local.DbCacheLimit
import com.senijoshua.pulitzer.data.article.local.DbCacheLimitImpl
import com.senijoshua.pulitzer.data.article.local.article.LocalArticleDataSource
import com.senijoshua.pulitzer.data.article.local.article.LocalArticleDataSourceImpl
import com.senijoshua.pulitzer.data.article.local.remotekey.LocalRemoteKeyDataSource
import com.senijoshua.pulitzer.data.article.local.remotekey.LocalRemoteKeyDataSourceImpl
import com.senijoshua.pulitzer.data.article.remote.RemoteArticleDataSource
import com.senijoshua.pulitzer.data.article.remote.RemoteArticleDataSourceImpl
import com.senijoshua.pulitzer.data.article.repository.ArticleRemoteMediator
import com.senijoshua.pulitzer.data.article.repository.OfflineFirstArticleRepository
import com.senijoshua.pulitzer.domain.article.repository.ArticleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {
    @Singleton
    @Binds
    internal abstract fun provideLocalArticleDataSource(localDataSourceImpl: LocalArticleDataSourceImpl): LocalArticleDataSource

    @Singleton
    @Binds
    internal abstract fun provideLocalRemoteKeyDataSource(
        localRemoteKeyDataSourceImpl: LocalRemoteKeyDataSourceImpl
    ): LocalRemoteKeyDataSource

    @Singleton
    @Binds
    internal abstract fun provideRemoteArticleDataSource(remoteDataSourceImpl: RemoteArticleDataSourceImpl): RemoteArticleDataSource

    @Singleton
    @Binds
    internal abstract fun provideDbCache(dbCacheLimitImpl: DbCacheLimitImpl): DbCacheLimit

    @Singleton
    @Binds
    internal abstract fun provideArticleRepository(repository: OfflineFirstArticleRepository): ArticleRepository

    @OptIn(ExperimentalPagingApi::class)
    @Singleton
    @Binds
    internal abstract fun provideRemoteMediator(remoteMediator: ArticleRemoteMediator): RemoteMediator<Int, ArticleEntity>
}
