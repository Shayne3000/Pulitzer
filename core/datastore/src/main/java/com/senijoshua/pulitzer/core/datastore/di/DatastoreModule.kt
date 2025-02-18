package com.senijoshua.pulitzer.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.senijoshua.pulitzer.core.datastore.repository.PagingPreferencesRepository
import com.senijoshua.pulitzer.core.datastore.repository.PagingPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val PAGING_PREFERENCES = "paging_preferences"

@Module
@InstallIn(SingletonComponent::class)
internal object PreferencesDataStoreModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context,
        dispatcher: CoroutineDispatcher
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            scope = CoroutineScope(dispatcher + SupervisorJob()),
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            produceFile = { context.preferencesDataStoreFile(PAGING_PREFERENCES) }
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PagingPreferencesModule {
    @Binds
    @Singleton
    internal abstract fun providePagingPreferencesRepository(repository: PagingPreferencesRepositoryImpl): PagingPreferencesRepository
}
