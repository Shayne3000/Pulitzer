package com.senijoshua.pulitzer.data.article.local

interface DbCacheLimit {
    val refreshCacheLimit: Long
    val clearCacheLimit: Long
}
