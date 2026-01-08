package com.felix.ventral_android.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    // Holds the original, unfiltered list of all events
    private var allEvents: List<Event> = emptyList()

    init {
        fetchAllEventsForSearch()
    }

    // Fetches all events once and stores them locally in the ViewModel
    private fun fetchAllEventsForSearch() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = eventRepository.getAllEvents()

            result.onSuccess { events ->
                allEvents = events
                _uiState.update { it.copy(isLoading = false, events = events) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    // This function is called from the UI whenever the search text changes
    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterEvents(query)
    }

    // Filters the locally stored list of events based on the query
    private fun filterEvents(query: String) {
        val filteredList = if (query.isBlank()) {
            allEvents // If search is empty, show all events
        } else {
            allEvents.filter { event ->
                event.name.contains(query, ignoreCase = true)
            }
        }
        _uiState.update { it.copy(events = filteredList) }
    }

    // Can be used to manually trigger a refresh from the UI
    fun onRetry() {
        fetchAllEventsForSearch()
    }
}

// UI State class for the Search screen
data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val events: List<Event> = emptyList(),
    val errorMessage: String? = null
)
