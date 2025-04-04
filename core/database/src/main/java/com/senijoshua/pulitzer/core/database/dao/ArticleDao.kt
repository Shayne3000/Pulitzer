package com.senijoshua.pulitzer.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.senijoshua.pulitzer.core.database.entity.ArticleEntity
import com.senijoshua.pulitzer.core.database.entity.BookmarkedArticles
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Upsert
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles")
    fun getPagedArticles(): PagingSource<Int, ArticleEntity>

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticleById(id: String): Flow<ArticleEntity>

    @Query("UPDATE articles SET isBookmarked = CASE WHEN isBookmarked = 1 THEN 0 ELSE 1 END WHERE id = :articleId")
    suspend fun bookmarkArticle(articleId: String)

    @Query("SELECT id, thumbnail, title, author FROM articles WHERE title LIKE '%' || :searchQuery || '%' AND isBookmarked = 1")
    fun getBookmarkedArticles(searchQuery: String): Flow<List<BookmarkedArticles>>

    @Query("UPDATE articles SET isBookmarked = 0 WHERE id IN (:articleIds)")
    suspend fun unBookmarkArticles(articleIds: List<String>)

    @Query("SELECT created_at FROM articles ORDER BY created_at DESC LIMIT 1")
    fun getTimeCreated(): Long?

    @Query("DELETE FROM articles")
    fun clearArticles()
}
