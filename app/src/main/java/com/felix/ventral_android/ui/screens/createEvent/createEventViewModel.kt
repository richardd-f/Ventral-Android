package com.felix.ventral_android.ui.screens.createEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felix.ventral_android.data.dto.CreateEventRequestDto
import com.felix.ventral_android.domain.model.Category
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.domain.repository.EventRepository
import com.felix.ventral_android.utils.toIsoString
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
    private val eventRepository: EventRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    init {
        fetchCategories()
    }

    /** Fetch categories from API */
    private fun fetchCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            eventRepository.getAllCategories()
                .onSuccess { categories ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            availableCategories = categories
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load categories"
                        )
                    }
                }
        }
    }

    // --- UI field updates ---
    fun onEventNameChange(text: String) = update { it.copy(eventName = text) }
    fun onDescriptionChange(text: String) = update { it.copy(description = text) }
    fun onStartDateChange(text: String) = update { it.copy(startDate = text) }
    fun onStartTimeChange(text: String) = update { it.copy(startTime = text) }
    fun onEndDateChange(text: String) = update { it.copy(endDate = text) }
    fun onEndTimeChange(text: String) = update { it.copy(endTime = text) }
    fun onPriceChange(text: String) = update { it.copy(price = text) }
    fun onQuotaChange(text: String) = update { it.copy(quota = text) }

    fun onImagesSelected(uris: List<String>) =
        update { it.copy(images = uris) }

    /** Toggle category selection by ID */
    fun onCategoryToggled(categoryId: String) =
        update {
            val updated = if (it.selectedCategoryIds.contains(categoryId)) {
                it.selectedCategoryIds - categoryId
            } else {
                it.selectedCategoryIds + categoryId
            }
            it.copy(selectedCategoryIds = updated)
        }

    /** Create Event, sending category IDs instead of names */
    fun createEvent(onSuccess: () -> Unit) {
        val state = uiState.value

        val event = CreateEventRequestDto(
            name = state.eventName.trim(),
            description = state.description.trim(),
            dateStart = toIsoString(
                date = state.startDate,
                time = state.startTime
            ),
            dateEnd = toIsoString(
                date = state.endDate,
                time = state.endTime
            ),
            price = state.price.toIntOrNull() ?: 0,
            status = "OPEN",
            quota = state.quota.toIntOrNull(),
            images = state.images,
            categories = state.selectedCategoryIds // <-- use IDs here
        )

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            eventRepository.createEvent(event)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to create event"
                        )
                    }
                }
        }
    }

    private fun update(block: (CreateEventUiState) -> CreateEventUiState) {
        _uiState.update(block)
    }
}

/** UI State updated to hold Category objects and selected IDs */
data class CreateEventUiState(
    val eventName: String = "",
    val description: String = "",
    val startDate: String = "",
    val startTime: String = "",
    val endDate: String = "",
    val endTime: String = "",
    val price: String = "",
    val quota: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    // Images
    val images: List<String> = emptyList(),

    // Categories fetched from API
    val availableCategories: List<Category> = emptyList(),
    val selectedCategoryIds: List<String> = emptyList() // store IDs
)
