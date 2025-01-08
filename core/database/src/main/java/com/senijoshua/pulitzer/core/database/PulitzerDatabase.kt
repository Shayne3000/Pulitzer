package com.senijoshua.pulitzer.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.senijoshua.pulitzer.core.database.converters.DateConverter
import com.senijoshua.pulitzer.core.database.dao.ArticleDao
import com.senijoshua.pulitzer.core.database.dao.RemoteKeyDao
import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import com.senijoshua.pulitzer.core.database.entity.RemoteKeyEntity

@Database(version = 1, entities = [ArticleEntity::class, RemoteKeyEntity::class], exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class PulitzerDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        const val DATABASE_NAME = "pulitzer_db"
    }
}
