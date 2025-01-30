package com.senijoshua.pulitzer.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.senijoshua.pulitzer.core.model.Result
import com.senijoshua.pulitzer.domain.article.usecase.BookmarkArticleUseCase
import com.senijoshua.pulitzer.domain.article.usecase.GetArticlesUseCase
import com.senijoshua.pulitzer.domain.article.usecase.GetPagedArticlesUseCase
import com.senijoshua.pulitzer.feature.home.model.HomeArticle
import com.senijoshua.pulitzer.feature.home.model.toPresentationFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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

    val pagedArticles: Flow<PagingData<HomeArticle>> by lazy {
        flow {
            emitAll(
                getPagedNewsArticles().map { pagingData ->
                    pagingData.map { it.toPresentationFormat() }
                }
            )
        }.cachedIn(viewModelScope)
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

    /*
     * Remote mediator loads the data from the network and persists it to DB when we've scrolled to the end and run out of cached paged data.
     * PagingSource then loads the paged cached data from the DB and supplies it to the UI through the ViewModel.
     *
     * The problem with the paging library is their technical choice where pagingData can only be collected once in the UI
     * which messes up our architecture by increasing coupling/reducing separation of concerns as the UI
     * now directly accesses and manages screen/Paging Library state (i.e. Error, Loading ) and behaviour (Refresh, append) and the fact
     * that they force that technical choice on you. This approach gives us lesser control over the UI State e.g. when I need to reset error states
     * Ideally, the UI shouldn't care about how its state is generated or what behaviours generate it. Its job is to just
     * display state and call its own events that will change state. A paging library is a detail/framework. The UI shouldn't know about
     * such details. Making it know about such makes things less maintainable and testable. This approach also makes it difficult to
     * test the ViewModel especially if the application screen that uses pagination is complex. it gives us less control over the UI State.
     */

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
    val errorMessage: String? = null,
)
