package com.senijoshua.pulitzer.core.datastore.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.senijoshua.pulitzer.core.datastore.PagingPreferenceKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class PagingPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PagingPreferencesRepository {

    override suspend fun getPageNumber(defaultPageNumber: Int): Int? {
        return errorSafeCall {
            val preferences = dataStore.data.first()
            preferences[PagingPreferenceKey.PAGE_NUMBER] ?: defaultPageNumber
        }
    }

    override suspend fun updatePageNumber(pageNumber: Int) {
        errorSafeCall {
            dataStore.edit { preferences ->
                preferences[PagingPreferenceKey.PAGE_NUMBER] = pageNumber
            }
        }
    }

    private inline fun <T> errorSafeCall(executable: () -> T): T? {
        return try {
            executable()
        } catch (e: IOException) {
            Log.e(this.javaClass.name, "IOException: ${e.localizedMessage}")
            null
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Exception: ${e.localizedMessage}")
            null
        }
    }
}
