package com.senijoshua.pulitzer.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.pulitzer.feature.details.model.DetailArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val articleId = checkNotNull(savedStateHandle[ARTICLE_ARG])
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    // Start a coroutine to call the getarticle use case to get article details given the article
    fun getArticle() {
        viewModelScope.launch {

        }
    }

    fun bookmarkArticle() {
        viewModelScope.launch {
            // use the article Id to call the bookmark article use case
        }
    }

    fun errorMessageShown() {

    }
}

/**
 * Representation of the screen UI State
 */
internal data class DetailUiState(
    val details: DetailArticle? = null, // TODO Consider sub-classing presentation article or removing it completely if tests are gated to each module
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
