package com.senijoshua.pulitzer.core.datastore

import androidx.datastore.preferences.core.intPreferencesKey

/**
 * Keys for the values to be stored in the app-level Preferences Datastore instance.
 */
object PagingPreferenceKey {
    val PAGE_NUMBER = intPreferencesKey("page_number")
}
