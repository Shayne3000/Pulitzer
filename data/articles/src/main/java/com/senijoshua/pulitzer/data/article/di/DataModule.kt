package com.senijoshua.pulitzer.data.article.di

import com.senijoshua.pulitzer.data.article.local.LocalDataSource
import com.senijoshua.pulitzer.data.article.local.LocalDataSourceImpl
import com.senijoshua.pulitzer.data.article.remote.RemoteDataSource
import com.senijoshua.pulitzer.data.article.remote.RemoteDataSourceImpl
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
    internal abstract fun provideLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Singleton
    @Binds
    internal abstract fun provideRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Singleton
    @Binds
    internal abstract fun provideArticleRepository(repository: OfflineFirstArticleRepository): ArticleRepository
}
