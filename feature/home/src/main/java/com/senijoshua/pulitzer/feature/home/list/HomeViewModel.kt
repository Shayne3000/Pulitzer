package com.senijoshua.pulitzer.feature.home.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.usecase.GetArticlesUseCase
import com.senijoshua.pulitzer.feature.home.list.model.HomeArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(private val getArticles: GetArticlesUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState
    private val mutex = Mutex()

    fun getNewsArticles() {
        viewModelScope.launch {
            // This is to make this thread-safe and prevent this from being called (by the
            // Compose compiler) from multiple threads at the same time i.e a race condition
            // execute this with a mutex lock preventing other threads from calling this whilst its already executing.
            // In short, lock access to this critical section
            mutex.withLock {
                getArticles().collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            // TODO Add a mapper class for the home feature to map from domain model to home article, make it an extension of domain article
                            _uiState.update { currentUiState ->
                                currentUiState.copy(
                                    articles = result.data,
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
