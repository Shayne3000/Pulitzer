package com.senijoshua.pulitzer.data.article.local

import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DbCacheLimitImpl @Inject constructor() : DbCacheLimit {
    /**
     * The clear limit is the max amount of time for which we
     * can cache paged article data in the DB after which it will be
     * considered stale and deleted.
     */
    override val clearCacheLimit: Long =
        TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS)
}
