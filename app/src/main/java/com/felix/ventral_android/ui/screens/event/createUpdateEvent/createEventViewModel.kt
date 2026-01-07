package com.felix.ventral_android.ui.screens.event.createUpdateEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felix.ventral_android.data.dto.CreateEventRequestDto
import com.felix.ventral_android.data.dto.UpdateEventRequestDto
import com.felix.ventral_android.domain.model.Category
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.domain.repository.EventRepository
import com.felix.ventral_android.utils.fromHumanToDateIso
import com.felix.ventral_android.utils.fromHumanToTime
import com.felix.ventral_android.utils.toIsoString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class FormMode { CREATE, UPDATE }

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    init {
        fetchCategories()
    }

    /** * Call this when entering the screen to edit an existing event
     */
    fun setEditMode(event: Event) {
        _uiState.update {
            it.copy(
                mode = FormMode.UPDATE,
                targetEventId = event.id,
                eventName = event.name,
                description = event.description,
                // Note: You'll need to parse your human-readable back to state fields
                // or handle the ISO conversion here if needed

                startDate = fromHumanToDateIso(event.dateStart.split(",")[0]),
                endDate = fromHumanToDateIso(event.dateEnd.split(",")[0]),
                startTime = fromHumanToTime(event.dateStart.split(", ")[1],),
                endTime = fromHumanToTime(event.dateEnd.split(", ")[1]),

                price = event.price.toString(),
                quota = event.quota?.toString() ?: "",
                selectedCategoryIds = event.categories.map { cat -> cat.id },
                images = event.images
            )
        }
    }

    // --- Existing logic stays the same ---
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
    fun onEventNameChange(text: String) = update { it.copy(eventName = text) }
    fun onDescriptionChange(text: String) = update { it.copy(description = text) }
    fun onStartDateChange(text: String) = update { it.copy(startDate = text) }
    fun onStartTimeChange(text: String) = update { it.copy(startTime = text) }
    fun onEndDateChange(text: String) = update { it.copy(endDate = text) }
    fun onEndTimeChange(text: String) = update { it.copy(endTime = text) }
    fun onPriceChange(text: String) = update { it.copy(price = text) }
    fun onQuotaChange(text: String) = update { it.copy(quota = text) }
    fun onImagesSelected(uris: List<String>) = update { it.copy(images = uris) }
    fun onCategoryToggled(categoryId: String) = update {
        val updated = if (it.selectedCategoryIds.contains(categoryId)) {
            it.selectedCategoryIds - categoryId
        } else {
            it.selectedCategoryIds + categoryId
        }
        it.copy(selectedCategoryIds = updated)
    }

    /** * Combined Save Function
     */
    fun submit(onSuccess: () -> Unit) {
        val state = uiState.value
        if (state.mode == FormMode.CREATE) {
            createEvent(onSuccess)
        } else {
            updateExistingEvent(onSuccess)
        }
    }

    /** Keep existing Create logic untouched */
    fun createEvent(onSuccess: () -> Unit) {
        val state = uiState.value
        val request = CreateEventRequestDto(
            name = state.eventName.trim(),
            description = state.description.trim(),
            dateStart = toIsoString(state.startDate, state.startTime),
            dateEnd = toIsoString(state.endDate, state.endTime),
            price = state.price.toIntOrNull() ?: 0,
            status = "OPEN",
            quota = state.quota.toIntOrNull(),
            images = state.images,
            categories = state.selectedCategoryIds.toList()
        )

        performAction(onSuccess) { eventRepository.createEvent(request) }
    }

    /** New Update logic */
    private fun updateExistingEvent(onSuccess: () -> Unit) {
        val state = uiState.value
        val eventId = state.targetEventId ?: return

        val request = UpdateEventRequestDto(
            name = state.eventName.trim(),
            description = state.description.trim(),
            dateStart = toIsoString(state.startDate, state.startTime),
            dateEnd = toIsoString(state.endDate, state.endTime),
            price = state.price.toIntOrNull() ?: 0,
            quota = state.quota.toIntOrNull(),
        )

        performAction(onSuccess) { eventRepository.updateEvent(eventId, request) }
    }

    /** Helper to avoid repeating loading/error logic */
    private fun <T> performAction(onSuccess: () -> Unit, action: suspend () -> Result<T>) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            action().onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Action failed"
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
    val mode: FormMode = FormMode.CREATE,
    val targetEventId: String? = null,
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
