package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.senijoshua.pulitzer.feature.bookmarks.model.BookmarksArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class BookmarksViewModel @Inject constructor() : ViewModel() {
    // Setup a StateFlow to hold the Screen's UI State
    private val _uiState = MutableStateFlow(BookmarksScreenState())
    val uiState: StateFlow<BookmarksScreenState> = _uiState

    // using stateflow might be an issue if you introduce async behaviours to updating the text in the textfield if not, use it.
    var searchQuery by mutableStateOf("")
        private set

    // TODO Add a compose state instance for the search query that will be converted to stateflow using a debounced snapshot flow when we trigger a search

    // Setup a coroutine to load the list of bookmarks from the DB

    // Setup a coroutine to implement the search with a search query.

    // TODO add a function to update the search query
    fun updateSearchQuery(newQuery: String) {
        searchQuery = newQuery
    }
}

/**
 * Representation of the Screen's UI State at any instant in time.
 */
internal data class BookmarksScreenState( // TODO Convert to sealed interface
    val articles: List<BookmarksArticle> = emptyList(),
    val isLoading: Boolean = true, // or isSearching. Maybe build this around searching
    val errorMessage: String? =  null
)
