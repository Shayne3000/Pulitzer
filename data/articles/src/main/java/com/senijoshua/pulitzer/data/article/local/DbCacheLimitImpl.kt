package com.senijoshua.pulitzer.data.article.local

import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DbCacheLimitImpl @Inject constructor() : DbCacheLimit {
    /**
     * The cache limit is the max amount of time for which we
     * can serve the same article data set from the DB after which, it will be
     * considered old and fresh data will be requested from the server.
     */
    override val refreshCacheLimit: Long =
        TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

    /**
     * The clear limit is the max amount of time for which we
     * can append article data to the DB after which it will be
     * considered stale and deleted from the DB.
     */
    override val clearCacheLimit: Long =
        TimeUnit.MILLISECONDS.convert(48, TimeUnit.HOURS)
}
