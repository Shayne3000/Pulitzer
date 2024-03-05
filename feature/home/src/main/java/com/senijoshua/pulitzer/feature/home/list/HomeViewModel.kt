package com.senijoshua.pulitzer.feature.home.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senijoshua.pulitzer.feature.home.list.model.HomeArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor() : ViewModel() {
    // setup use cases that contain and represent the business logic with dependency inversion

    // In the domain layer, add an entity representation of an article with a repository interface there

    // setup observable data holding container - stateflow
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState
    private val mutex = Mutex()

    fun getArticles() {
        viewModelScope.launch {
            // This is to make this thread-safe and prevent this from being called (by the
            // compiler) from multiple threads at the same time i.e a race condition
            // execute this with a mutex lock preventing other threads from calling this whilst its already executing
            // lock access to this critical section
            mutex.withLock {
                // call the use case which returns a flow and update the stateflow with the result
                // We return a flow to be able to get the updated list of data from the DB anytime an article is bookmarked
            }
        }
    }

    fun onErrorMessageShown() {
        _uiState.update { currentUiState ->
            currentUiState.copy(errorMessage = null)
        }
    }
}

// setup representation of the UI State of the home screen at any instant in time
/**
 * Representation of the UI State of the Home Screen at any instant in time
 */
internal data class HomeUiState(
    val articles: List<HomeArticle> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
