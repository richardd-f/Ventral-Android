package com.felix.ventral_android.ui.screens.createEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateEventViewModel @Inject constructor(

) : ViewModel(){
    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    fun onEventNameChange(text: String) { _uiState.update { it.copy(eventName = text) } }
    fun onDescriptionChange(text: String) { _uiState.update { it.copy(description = text) } }
    fun onStartDateChange(text: String) { _uiState.update { it.copy(startDate = text) } }
    fun onStartTimeChange(text: String) { _uiState.update { it.copy(startTime = text) } }
    fun onEndDateChange(text: String) { _uiState.update { it.copy(endDate = text) } }
    fun onEndTimeChange(text: String) { _uiState.update { it.copy(endTime = text) } }

    // Only allow numeric input logic could be added here if not handled in UI
    fun onPriceChange(text: String) { _uiState.update { it.copy(price = text) } }
    fun onQuotaChange(text: String) { _uiState.update { it.copy(quota = text) } }

    fun createEvent(onSuccess: () -> Unit) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        // Simulate API Call
        viewModelScope.launch {
            try {
                delay(1500) // Fake delay
                // Here you would validate inputs and call repository
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}

data class CreateEventUiState(
    val eventName: String = "",
    val description: String = "",
    val startDate: String = "", // Format: DD/MM/YYYY
    val startTime: String = "", // Format: HH:MM
    val endDate: String = "",
    val endTime: String = "",
    val price: String = "",
    val quota: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)