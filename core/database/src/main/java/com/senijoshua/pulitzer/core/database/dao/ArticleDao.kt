package com.senijoshua.pulitzer.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Upsert
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles")
    fun getArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id")
    suspend fun getArticleById(id: String): ArticleEntity

    @Query("UPDATE articles SET isBookmarked = CASE WHEN isBookmarked = 1 THEN 0 ELSE 1 END WHERE id = :articleId")
    suspend fun bookmarkArticle(articleId: String)

    @Query("SELECT thumbnail, title, body FROM articles WHERE isBookmarked = 1")
    fun getBookmarkedArticles(): Flow<List<ArticleEntity>>

    @Update
    suspend fun removeArticlesFromBookmarks(vararg articleEntity: ArticleEntity)

    @Query("SELECT created_at FROM articles ORDER BY created_at DESC LIMIT 1")
    suspend fun getTimeCreated(): Long?
}
