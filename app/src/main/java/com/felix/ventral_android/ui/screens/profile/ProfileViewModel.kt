package com.felix.ventral_android.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felix.ventral_android.data.local.LocalDataStore
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.domain.repository.EventRepository
import com.felix.ventral_android.domain.repository.UserRepository
import com.felix.ventral_android.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val localDataStore: LocalDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Screen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        fetchProfileData()
    }

    fun fetchProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // 1. Initial ID Check
            val userId = localDataStore.getUserId().first()
            if (userId.isNullOrEmpty()) {
                _navigationEvent.emit(Screen.Login)
                return@launch
            }

            // 2. Parallel API Calls
            val userDeferred = async { userRepository.getUserById(userId) }
            val eventsDeferred = async { eventRepository.getEventsByUserId(userId) }

            val userResult = userDeferred.await()
            val eventsResult = eventsDeferred.await()

            // 3. Result Handling
            if (userResult.isSuccess && eventsResult.isSuccess) {
                val user = userResult.getOrThrow()
                val events = eventsResult.getOrThrow()

                _uiState.update { it.copy(
                    isLoading = false,
                    username = user.name,
                    bio = user.bio,
                    profileImage = user.imgUrl,
                    posts = events
                )}
            } else {
                val error = userResult.exceptionOrNull()?.message
                    ?: eventsResult.exceptionOrNull()?.message
                    ?: "Unknown error"

                // 4. Handle 401 Session Expiry
                if (error.contains("Session expired", ignoreCase = true)) {
                    _navigationEvent.emit(Screen.Login)
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = error) }
                }
            }
        }
    }

    // Manual Logout Function
    fun logout() {
        viewModelScope.launch {
            localDataStore.clearData()
            _navigationEvent.emit(Screen.Login)
        }
    }
}


data class ProfileUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val bio: String = "",
    val profileImage: String = "",
    val followingCount: Int = 0,
    val followersCount: Int = 0,
    val eventsCount: Int = 0,
    val posts: List<Event> = emptyList(),
    val errorMessage: String? = null
)

sealed class ProfileNavigationEvent {
    object NavigateToLogin : ProfileNavigationEvent()
}