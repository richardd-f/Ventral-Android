package com.felix.ventral_android.ui.screens.event.eventDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felix.ventral_android.data.local.LocalDataStore
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val localDataStore: LocalDataStore
) : ViewModel() {

    private val _eventState = MutableStateFlow<Event?>(null)
    val eventState: StateFlow<Event?> = _eventState.asStateFlow()

    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked.asStateFlow()


    private val _isRegistering = MutableStateFlow(false)
    val isRegistering: StateFlow<Boolean> = _isRegistering.asStateFlow()

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting.asStateFlow()

    // Reactive isAuthor check
    val isAuthor: StateFlow<Boolean> = combine(
        _eventState,
        localDataStore.getUserId()
    ) { event, userId ->
        // Return true if IDs match, false otherwise
        event != null && userId != null && event.authorId == userId
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun setEvent(event: Event) {
        if (_eventState.value == null) {
            _eventState.value = event
        }
    }

    fun toggleLike() {
        _isLiked.value = !_isLiked.value
    }

    fun registerEvent(onSuccess: () -> Unit) {
        val eventId = _eventState.value?.id ?: return

        viewModelScope.launch {
            _isRegistering.value = true

            val result = eventRepository.applyEvent(eventId)

            _isRegistering.value = false

            result.onSuccess {
                onSuccess()
            }.onFailure { error ->
                // You might want to expose an error message state here
                println("Error registering: ${error.message}")
            }
        }
    }

    fun deleteEvent(onDeleteSuccess:()->Unit){
        val eventId = _eventState.value?.id ?: return

        viewModelScope.launch {
            _isDeleting.value = true

            val result = eventRepository.deleteEvent(eventId)

            _isDeleting.value = false

            result.onSuccess {
                onDeleteSuccess()
            }.onFailure { error ->
                // You might want to expose an error message state here
                println("Error delete event: ${error.message}\nEventId = $eventId")
            }
        }
    }
}