package com.senijoshua.pulitzer.data.article.local

import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DbCacheLimitImpl @Inject constructor() : DbCacheLimit {
    /**
     * The cache limit is the max amount of time for which we
     * can serve article data in the DB after which, it will be
     * considered old and fresh will be requested from the server.
     */
    override val refreshCacheLimit: Long =
        TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

    /**
     * The clear limit is  the max amount of time for which we
     * can store article data in the DB after which it will be
     * considered stale and deleted from the DB.
     */
    override val clearCacheLimit: Long =
        TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
}
