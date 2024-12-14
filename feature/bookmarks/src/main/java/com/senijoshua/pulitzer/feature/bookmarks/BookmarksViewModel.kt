package com.senijoshua.pulitzer.feature.bookmarks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.usecase.GetBookmarkedArticlesUseCase
import com.senijoshua.pulitzer.feature.bookmarks.model.BookmarksArticle
import com.senijoshua.pulitzer.feature.bookmarks.model.toPresentationFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    var searchQuery: String by mutableStateOf("")
        private set

    fun triggerSearch() {
        viewModelScope.launch {
            delay(500)
            getBookmarkedArticles(searchQuery)
        }
    }

    private suspend fun getBookmarkedArticles(query: String) {
        getBookmarkedArticles(searchQuery = query).collectLatest { result ->
            when (result) {
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
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
