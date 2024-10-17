@file:OptIn(FlowPreview::class)

package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.pulitzer.feature.bookmarks.model.BookmarksArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class BookmarksViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(BookmarksUiState())
    val uiState: StateFlow<BookmarksUiState> = _uiState

    // store the state (i.e. search query) of the search textfield in the viewmodel using mutableState
    // to survive configuration changes and circumvent the undesirable behaviour of
    // updating text field state asynchronously whilst typing.
    var searchQuery by mutableStateOf("")
        private set

    // TODO Observe any changes to the search field's state i.e. the search query by converting it to a flow and trigger
    //  a search debounced by 500ms via a use case when the query changes (i.e. on each emission)
    //  and then Update the uiState with the search result from the use case

    // On start call the initialise search function with the current textfield state being empty
    fun initialiseSearch() {
        snapshotFlow { searchQuery }
            .debounce(500)
            .onEach { searchQuery ->
            getBookmarkedArticles(searchQuery)
        }.launchIn(viewModelScope)
    }

    private fun getBookmarkedArticles(query: String) {
        viewModelScope.launch {
            // call GetBookmarkedArticleUseCase and make the repository manipulation main-safe
            if (searchQuery.isBlank()) {
                // get the list of all bookmarked articles given no filter
                // Update the Screen state
            } else {
                // get bookmarked articles whose title contains searchQuery
                // Update the Screen state
            }
        }
    }

    // Setup a coroutine to load the list of bookmarks from the DB

    // Setup a coroutine to implement the search with a search query.

    fun updateErrorState() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = false,
                errorMessage = null,
            )
        }
    }

    fun updateSearchQuery(newQuery: String) {
        searchQuery = newQuery
    }
}

/**
 * Representation of the Screen's UI State at any instant in time.
 */
internal data class BookmarksUiState(
    val bookmarkedArticles: List<BookmarksArticle> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? =  null,
)
