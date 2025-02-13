package com.senijoshua.pulitzer.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.pulitzer.core.model.GlobalConstants
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.usecase.BookmarkArticleUseCase
import com.senijoshua.pulitzer.domain.article.usecase.GetArticlesUseCase
import com.senijoshua.pulitzer.domain.article.usecase.GetPagedArticlesUseCase
import com.senijoshua.pulitzer.feature.home.model.HomeArticle
import com.senijoshua.pulitzer.feature.home.model.toPresentationFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getNewsArticles: GetArticlesUseCase,
    private val getPagedNewsArticles: GetPagedArticlesUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState
    private var page: Int = 0 // persist currently loaded page in DB or DataStore

    init {
        // retrieve page from preferences data store, if null, return GlobalConstants.INITIAL_PAGE
        // update page
    }

    fun getPagedArticles(isRefresh: Boolean = false, isPaging: Boolean = false) {
        viewModelScope.launch {
            getPagedNewsArticles(
                page = page,
                isRefresh = isRefresh,
                isPaging = isPaging
            ).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        val articles = result.data
                        _uiState.update { currentState ->
                            val updatedArticleList = if (page == GlobalConstants.INITIAL_PAGE || !isPaging) {
                                // How do we handle when we're on the same page and we update a bookmark and we return the whole list
                                articles.toPresentationFormat()
                            } else {
                                currentState.articles + articles.toPresentationFormat()
                            }
                            page++
                            // set isPaging or canPaginate to false
                            currentState.copy(
                                articles = updatedArticleList,
                                isLoading = false,
                                isRefreshing = false,
                                isPaging = false,
                            )
                        }
                    }

                    is Result.Error -> {
                        val errorMessage = result.error.localizedMessage
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isRefreshing = false,
                                isPaging = false,
                                errorMessage = errorMessage,
                            )
                        }
                        // set canPaginate or isPaging to false
                    }
                }
            }
        }
    }

    fun refreshPagedArticles() {
        _uiState.update { currentState ->
            currentState.copy(articles = emptyList(), isRefreshing = true)
        }
        page = GlobalConstants.INITIAL_PAGE
        getPagedArticles(isRefresh = true)
    }

    fun pageArticles() {
        _uiState.update { currentState ->
            currentState.copy(isPaging = true)
        }
        getPagedArticles(isPaging = true)
    }

    fun retryPagedArticleRetrieval() {
        getPagedArticles(isPaging = true)
    }

    fun getArticles() {
        viewModelScope.launch { 
            getNewsArticles().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                articles = result.data.toPresentationFormat(),
                                isLoading = false
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                articles = emptyList(),
                                isLoading = false,
                                errorMessage = result.error.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun bookmarkArticle(articleId: String) {
        viewModelScope.launch {
            bookmarkArticleUseCase(articleId)
        }
    }

    fun onErrorMessageShown() {
        _uiState.update { currentUiState ->
            currentUiState.copy(errorMessage = null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Store in preferences data store
    }
}

/**
 * Representation of the UI State of the Home Screen at any instant in time
 */
internal data class HomeUiState(
    val articles: List<HomeArticle> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPaging: Boolean = false,
    val errorMessage: String? = null
)
