package com.senijoshua.pulitzer.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val id: String,
    val thumbnail: String,
    val title: String,
    val author: String,
    val body: String,
    val isBookmarked: Boolean,
    @ColumnInfo(name = "last_modified_date")
    val lastModifiedDate: Date,
    @ColumnInfo(name = "created_at")
    val createdAt: Long? = System.currentTimeMillis()
)

data class BookmarkedArticles(
    val id: String,
    val thumbnail: String,
    val title: String,
    val body: String
)
