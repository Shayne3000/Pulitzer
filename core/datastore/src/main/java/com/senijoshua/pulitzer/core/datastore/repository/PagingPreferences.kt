package com.senijoshua.pulitzer.core.datastore.repository

/**
 * Abstraction through which we interact with the Preferences Datastore
 * for persisting and retrieving the current page number of articles to
 * load from the data layer.
 */
interface PagingPreferences {
    suspend fun getPageToLoad(): Int?
    suspend fun setNextPageToLoad(pageNumber: Int)
}
