package com.senijoshua.pulitzer.data.article.di

import com.senijoshua.pulitzer.data.article.local.DbCacheLimit
import com.senijoshua.pulitzer.data.article.local.DbCacheLimitImpl
import com.senijoshua.pulitzer.data.article.local.article.LocalArticleDataSource
import com.senijoshua.pulitzer.data.article.local.article.LocalArticleDataSourceImpl
import com.senijoshua.pulitzer.data.article.remote.RemoteArticleDataSource
import com.senijoshua.pulitzer.data.article.remote.RemoteArticleDataSourceImpl
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
    internal abstract fun provideLocalDataSource(localDataSourceImpl: LocalArticleDataSourceImpl): LocalArticleDataSource

    @Singleton
    @Binds
    internal abstract fun provideRemoteDataSource(remoteDataSourceImpl: RemoteArticleDataSourceImpl): RemoteArticleDataSource

    @Singleton
    @Binds
    internal abstract fun provideDbCache(dbCacheLimitImpl: DbCacheLimitImpl): DbCacheLimit

    @Singleton
    @Binds
    internal abstract fun provideArticleRepository(repository: OfflineFirstArticleRepository): ArticleRepository
}
