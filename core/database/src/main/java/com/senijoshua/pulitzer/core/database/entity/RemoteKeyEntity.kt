package com.senijoshua.pulitzer.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a row in the Remote key table for
 * persisting pagination keys from the remote service.
 */
@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey
    val articleId: String,
    val prevPageKey: Int?,
    val currentPageKey: Int,
    val nextPageKey: Int?,
    val createdAt: Long = System.currentTimeMillis()
)
