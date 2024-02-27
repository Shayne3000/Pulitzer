package com.senijoshua.pulitzer.feature.home.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
internal class HomeViewModel : ViewModel() {
    // setup use cases that contain and represent the business logic with dependency inversion

    // In the domain layer, add an entity representation of an article with a repository interface there

    // setup observable data holding container - stateflow

    // call the use case which returns a flow and update the stateflow with the result

    // setup representation of the UI State of the home screen at any instant in time
}

/**
 * Representation of the UI State of the Home Screen at any instant in time
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
