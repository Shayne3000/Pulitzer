package com.senijoshua.pulitzer.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.senijoshua.pulitzer.core.database.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Upsert
    suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remote_keys WHERE articleId = :articeId")
    suspend fun getRemoteKeyByArticleId(articeId: String): RemoteKeyEntity

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT createdAt FROM remote_keys ORDER BY createdAt DESC LIMIT 1")
    suspend fun getCreatedTime(): Long
}
