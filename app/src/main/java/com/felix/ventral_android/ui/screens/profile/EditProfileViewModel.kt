package com.felix.ventral_android.ui.screens.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(v: String) =
        _uiState.update { it.copy(name = v, errorMessage = null) }

    fun onEmailChange(v: String) =
        _uiState.update { it.copy(email = v, errorMessage = null) }

    fun onPhoneChange(v: String) =
        _uiState.update { it.copy(phone = v, errorMessage = null) }

    fun onSaveProfile() {
        val state = _uiState.value

        if (state.name.isBlank() || state.email.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Name and email are required")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onVerifyAccount() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1200)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isVerified = true
                )
            }
        }
    }
}

data class EditProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val isVerified: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
