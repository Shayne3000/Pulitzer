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
    @ColumnInfo(name = "section_name")
    val sectionName: String,
    val title: String,
    val headline: String,
    val main: String,
    val body: String,
    val isBookmarked: Boolean,
    @ColumnInfo(name = "publication_date")
    val publicationDate: Date,
    @ColumnInfo(name = "created_at")
    val createdAt: Long? = System.currentTimeMillis()
)

data class BookmarkedArticles(
    val id: String,
    val thumbnail: String,
    val title: String,
    val body: String
)
