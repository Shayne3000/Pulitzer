package com.senijoshua.pulitzer.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.usecase.BookmarkArticleUseCase
import com.senijoshua.pulitzer.domain.article.usecase.GetArticlesUseCase
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
    private val bookmarkArticleUseCase: BookmarkArticleUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

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
    val errorMessage: String? = null
)
