package com.senijoshua.pulitzer.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.usecase.BookmarkArticleUseCase
import com.senijoshua.pulitzer.domain.article.usecase.GetArticleGivenIdUseCase
import com.senijoshua.pulitzer.feature.details.model.DetailArticle
import com.senijoshua.pulitzer.feature.details.model.toPresentationFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getArticleById: GetArticleGivenIdUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase,
) : ViewModel() {
    private val detail: DetailRoute = savedStateHandle.toRoute()
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    fun getArticle() {
        viewModelScope.launch {
            getArticleById(detail.articleId).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update { currentUiState ->
                            currentUiState.copy(
                                details = result.data.toPresentationFormat(),
                                isLoading = false
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

    fun bookmarkArticle(articleId: String) {
        viewModelScope.launch {
            bookmarkArticleUseCase(articleId)
        }
    }

    fun errorMessageShown() {
        _uiState.update { currentUiState ->
            currentUiState.copy(
                isLoading = false,
                errorMessage = null
            )
        }
    }
}

/**
 * Representation of the screen UI State
 */
internal data class DetailUiState(
    val details: DetailArticle? = null, // TODO Consider removing presentation article completely since tests are gated to each module
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
