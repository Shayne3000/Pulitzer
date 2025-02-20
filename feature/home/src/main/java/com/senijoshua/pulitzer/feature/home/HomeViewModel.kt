package com.senijoshua.pulitzer.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.pulitzer.core.datastore.repository.PagingPreferences
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
    private val preferences: PagingPreferences,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState
    private var page: Int = 0

    fun getPagedArticles(isRefresh: Boolean = false, isPaging: Boolean = false) {
        viewModelScope.launch {
            page = if (isRefresh) {
                GlobalConstants.INITIAL_PAGE
            } else {
                preferences.getPageToLoad() ?: GlobalConstants.INITIAL_PAGE
            }

            getPagedNewsArticles(
                page = page,
                isRefresh = isRefresh,
                isPaging = isPaging
            ).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        val articles = result.data
                        _uiState.update { currentState ->
                            val updatedArticleList = if (!isRefresh || !isPaging) {
                                articles.toPresentationFormat()
                            } else {
                                currentState.articles + articles.toPresentationFormat()
                            }
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
                    }
                }
            }
        }
    }

    fun refreshPagedArticles() {
        _uiState.update { currentState ->
            currentState.copy(articles = emptyList(), isRefreshing = true)
        }
        getPagedArticles(isRefresh = true)
    }

    fun pageArticles() {
        _uiState.update { currentState ->
            currentState.copy(isPaging = true)
        }
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
