package com.senijoshua.pulitzer.core.datastore.repository

import com.senijoshua.pulitzer.core.model.GlobalConstants

/**
 * Abstraction through which we interact with the Preferences Datastore
 * for persisting and retrieving the current page number of articles to
 * load from the data layer.
 */
interface PagingPreferencesRepository {
    suspend fun getPageNumber(defaultPageNumber: Int = GlobalConstants.INITIAL_PAGE): Int?
    suspend fun updatePageNumber(pageNumber: Int)
}
