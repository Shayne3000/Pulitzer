@file:OptIn(FlowPreview::class)

package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.usecase.GetBookmarkedArticlesUseCase
import com.senijoshua.pulitzer.feature.bookmarks.model.BookmarksArticle
import com.senijoshua.pulitzer.feature.bookmarks.model.toPresentationFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class BookmarksViewModel @Inject constructor(
    private val getBookmarkedArticles: GetBookmarkedArticlesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookmarksUiState())
    val uiState: StateFlow<BookmarksUiState> = _uiState

    // store the state of the search textfield (i.e. search query) here in the viewmodel
    // using mutableState to survive configuration changes and circumvent the undesirable
    // behaviour of updating text field state asynchronously whilst typing resulting in the
    // query text being shown out of order.
    var searchQuery by mutableStateOf("")
        private set

    // On start call the initialise search function with the current textfield state being empty
    fun initialiseSearch() {
        snapshotFlow { searchQuery }
            .debounce(500)
            .onEach { searchQuery ->
                getBookmarkedArticles(searchQuery)
            }.launchIn(viewModelScope)
    }

    private suspend fun getBookmarkedArticles(query: String) {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = true,
            )
        }

        if (query.isBlank()) {
            _uiState.update { currentUiState ->
                currentUiState.copy(
                    isLoading = false,
                )
            }
        } else {
            getBookmarkedArticles(searchQuery = query).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                isLoading = false,
                                bookmarkedArticles = result.data.toPresentationFormat(),
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                isLoading = false,
                                errorMessage = result.error.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateErrorState() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
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
    val errorMessage: String? = null,
)
