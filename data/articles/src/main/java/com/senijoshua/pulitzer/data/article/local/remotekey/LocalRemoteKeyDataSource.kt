package com.senijoshua.pulitzer.data.article.local.remotekey

import com.senijoshua.pulitzer.core.database.entity.RemoteKeyEntity

/**
 * Interface through which higher elements in the architectural
 * hierarchy can interact with the RemoteKeys table in the local database.
 */
internal interface LocalRemoteKeyDataSource {
    suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeyEntity>)
    suspend fun getRemoteKeyByArticleId(articleId: String): RemoteKeyEntity
    suspend fun getCreatedTime(): Long?
    suspend fun clearRemoteKeys()
}
