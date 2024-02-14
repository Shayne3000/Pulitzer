package com.senijoshua.pulitzer.core.database.di

import android.content.Context
import androidx.room.Room
import com.senijoshua.pulitzer.core.database.PulitzerDatabase
import com.senijoshua.pulitzer.core.database.dao.ArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePulitzerDatabase(@ApplicationContext context: Context): PulitzerDatabase {
        return Room.databaseBuilder(
            context,
            PulitzerDatabase::class.java,
            PulitzerDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(db: PulitzerDatabase): ArticleDao = db.articleDao()
}
