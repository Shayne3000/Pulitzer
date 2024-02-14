package com.senijoshua.pulitzer.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.senijoshua.pulitzer.core.database.converters.DateConverter
import com.senijoshua.pulitzer.core.database.dao.ArticleDao

@Database(version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class PulitzerDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        const val DATABASE_NAME = "pulitzer_db"
    }
}
