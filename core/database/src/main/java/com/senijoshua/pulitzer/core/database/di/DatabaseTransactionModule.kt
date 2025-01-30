package com.senijoshua.pulitzer.core.database.di

import com.senijoshua.pulitzer.core.database.utils.PulitzerDbTransactionProvider
import com.senijoshua.pulitzer.core.database.utils.PulitzerDbTransactionProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DatabaseTransactionModule {
    @Singleton
    @Binds
    internal abstract fun providePulitzerDbTransaction(
        dbTransactionProvider: PulitzerDbTransactionProviderImpl
    ): PulitzerDbTransactionProvider
}
