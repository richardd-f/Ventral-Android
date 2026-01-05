package com.felix.ventral_android.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(

) : ViewModel(){
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        fetchProfileData()
    }

    private fun fetchProfileData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Mocking API call delay
            delay(1500)

            _uiState.value = ProfileUiState(
                isLoading = false,
                username = "Felix Venture",
                bio = "Full-stack Developer | Exploring the intersection of design and code. Coffee fueled.",
                followingCount = 124,
                followersCount = 1850,
                eventsCount = 12,
                posts = listOf(
                    EventPost("1", "Surabaya Tech Fest", "Annual developer conference in East Java", "https://images.pexels.com/photos/674010/pexels-photo-674010.jpeg?cs=srgb&dl=pexels-anjana-c-169994-674010.jpg&fm=jpg", "OPEN", 123),
                    EventPost("2", "Jetpack Compose Workshop", "Deep dive into state management", "https://images.pexels.com/photos/674010/pexels-photo-674010.jpeg?cs=srgb&dl=pexels-anjana-c-169994-674010.jpg&fm=jpg", "OPEN" , 85),
                    EventPost("3", "Laravel Meetup", "Discussing PHP-FPM and Nginx optimization", "https://images.pexels.com/photos/674010/pexels-photo-674010.jpeg?cs=srgb&dl=pexels-anjana-c-169994-674010.jpg&fm=jpg","CLOSED", 150)
                )
            )
        }
    }
}

data class EventPost(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val status: String, // e.g., "LIVE", "UPCOMING", "CLOSED"
    val likes: Int
)

data class ProfileUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val bio: String = "",
    val profileImage: String = "",
    val followingCount: Int = 0,
    val followersCount: Int = 0,
    val eventsCount: Int = 0,
    val posts: List<EventPost> = emptyList(),
    val errorMessage: String? = null
)