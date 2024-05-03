package com.senijoshua.pulitzer.feature.bookmarks

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

    // Setup a coroutine to load the list of bookmarks from the DB

    // Setup a coroutine to implement the search with a search query.
}

/**
 * Representation of the Screen's UI State at any instant in time.
 */
internal data class BookmarksScreenState(
    val articles: List<BookmarksArticle> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? =  null
)
