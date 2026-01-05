package com.felix.ventral_android.ui.screens.auth.register

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felix.ventral_android.data.repository.UserRepositoryImpl
import com.felix.ventral_android.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) :ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // --- Field Updaters ---
    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, errorMessage = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, errorMessage = null) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v, errorMessage = null) }
    fun onBioChange(v: String) = _uiState.update { it.copy(bio = v, errorMessage = null) }

    fun onProfileImageSelected(uri: Uri?) = _uiState.update { it.copy(profileImageUri = uri) }
    fun onDateSelected(millis: Long?) {
        if (millis != null) {
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
            val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
            _uiState.update { it.copy(birthDateMillis = millis, birthDateDisplay = date.format(formatter), errorMessage = null) }
        }
    }

    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v, errorMessage = null) }
    fun onConfirmPasswordChange(v: String) = _uiState.update { it.copy(confirmPassword = v, errorMessage = null) }
    fun togglePasswordVisibility() = _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

    // --- Navigation Logic ---
    fun onNextStep() {
        val state = _uiState.value

        // Validate based on current step
        when (state.currentStep) {
            1 -> {
                if (state.name.isBlank() || state.email.isBlank() || state.phone.isBlank()) {
                    _uiState.update { it.copy(errorMessage = "Please fill in all required fields") }
                    return
                }
            }
            2 -> {
                if (state.birthDateMillis == null || state.profileImageUri == null) {
                    _uiState.update { it.copy(errorMessage = "All fields is required") }
                    return
                }
            }
        }

        // Advance Step
        if (state.currentStep < state.totalSteps) {
            _uiState.update { it.copy(currentStep = it.currentStep + 1, errorMessage = null) }
        }
    }

    fun onPreviousStep() {
        if (_uiState.value.currentStep > 1) {
            _uiState.update { it.copy(currentStep = it.currentStep - 1, errorMessage = null) }
        }
    }

    fun onFinalRegister(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.password.isBlank() || state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Passwords do not match or are empty") }
            return
        }

        // Call API
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // 1. Convert date to YYYY-MM-DD
            val birthDateString = state.birthDateMillis?.let { millis ->
                Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } ?: ""

            // 2. Call Repository (Now a direct suspend call)
            // We remove .collect because this returns a Result<Unit> directly
            val result = userRepository.register(
                name = state.name,
                email = state.email,
                password = state.password,
                phone = state.phone,
                bio = state.bio,
                imgUri = state.profileImageUri.toString(),
                birth_date = birthDateString
            )

            // 3. Handle the Result directly
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                onSuccess() // Navigate to the next screen
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Registration failed"
                    )
                }
            }
        }
    }
}

data class RegisterUiState(
    val currentStep: Int = 1, // 1, 2, or 3
    val totalSteps: Int = 3,

    // Step 1 Data
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val bio: String = "",

    // Step 2 Data
    val profileImageUri: Uri? = null,
    val birthDateMillis: Long? = null,
    val birthDateDisplay: String = "",

    // Step 3 Data
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,

    // Meta
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)