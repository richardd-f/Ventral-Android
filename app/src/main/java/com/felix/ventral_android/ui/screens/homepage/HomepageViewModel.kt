package com.felix.ventral_android.ui.screens.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    // Hilt will inject the repository you have defined elsewhere in your project
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomepageUiState())
    val uiState: StateFlow<HomepageUiState> = _uiState.asStateFlow()

    init {
        // Automatically fetch events when the ViewModel is first created
        fetchAllEvents()
    }

    private fun fetchAllEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Fetch all events from the repository
            val result = eventRepository.getAllEvents()

            if (result.isSuccess) {
                // On success, update the state with the list of events
                _uiState.update {
                    it.copy(isLoading = false, events = result.getOrThrow())
                }
            } else {
                // On failure, update the state with an error message
                val errorMessage = result.exceptionOrNull()?.message ?: "An unknown error occurred"
                _uiState.update {
                    it.copy(isLoading = false, error = errorMessage)
                }
            }
        }
    }
}

/**
 * Data class to represent the UI state of the Homepage.
 * It is defined here to be co-located with its ViewModel.
 */
data class HomepageUiState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val error: String? = null
)
