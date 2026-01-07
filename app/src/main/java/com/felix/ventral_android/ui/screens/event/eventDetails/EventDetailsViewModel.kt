package com.felix.ventral_android.ui.screens.event.eventDetails

import androidx.lifecycle.ViewModel
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : ViewModel() {
    // Internal state
    private val _eventState = MutableStateFlow<Event?>(null)
    // Exposed to UI
    val eventState: StateFlow<Event?> = _eventState.asStateFlow()

    // Track if user liked the event locally
    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked.asStateFlow()

    fun setEvent(event: Event) {
        if (_eventState.value == null) {
            _eventState.value = event
        }
    }

    fun toggleLike() {
        val currentLikeStatus = _isLiked.value
        _isLiked.value = !currentLikeStatus

        // Here you would typically trigger an API call to your Laravel backend
        // e.g., repository.updateLike(eventId, !currentLikeStatus)
    }
}