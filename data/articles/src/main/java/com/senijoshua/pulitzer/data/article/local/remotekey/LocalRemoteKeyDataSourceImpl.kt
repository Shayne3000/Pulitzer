package com.senijoshua.pulitzer.data.article.local.remotekey

import com.senijoshua.pulitzer.core.database.dao.RemoteKeyDao
import com.senijoshua.pulitzer.core.database.entity.RemoteKeyEntity
import javax.inject.Inject

internal class LocalRemoteKeyDataSourceImpl @Inject constructor(private val dao: RemoteKeyDao): LocalRemoteKeyDataSource{
    override suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeyEntity>) =
        dao.insertRemoteKeys(remoteKeys)

    override suspend fun getRemoteKeyGivenArticleId(articleId: String) =
        dao.getRemoteKeyByArticleId(articleId)

    override suspend fun getCreatedTime() = dao.getCreatedTime()

    override suspend fun clearRemoteKeys() = dao.clearRemoteKeys()
}
