package com.tofiq.blood.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tofiq.blood.data.model.BloodGroup
import com.tofiq.blood.data.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * UI State for authentication screens
 */
data class AuthUiState(
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val role: UserRole? = null,
    val agreedToTerms: Boolean = false,
    val bloodGroup: BloodGroup? = null,
    val lastDonationDate: LocalDate? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)

/**
 * ViewModel for handling authentication logic
 * Follows MVVM pattern with unidirectional data flow
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState(isLoggedIn = repository.isLoggedIn()))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /**
     * Update phone number in UI state
     */
    fun updatePhoneNumber(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value)
    }

    /**
     * Update email in UI state (for backward compatibility)
     */
    fun updateEmail(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    /**
     * Update password in UI state
     */
    fun updatePassword(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    /**
     * Login with phone number and password
     */
    fun loginWithPhone(onSuccess: () -> Unit) {
        val (phoneNumber, password) = _uiState.value.let { it.phoneNumber to it.password }
        
        // Validation
        if (phoneNumber.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Phone number is required")
            return
        }
        if (password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Password is required")
            return
        }
        
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.loginWithPhonePassword(phoneNumber, password)
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoggedIn = true)
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.localizedMessage ?: "Login failed"
                )
            }
        }
    }

    /**
     * Login with email and password (for backward compatibility)
     */
    fun login(onSuccess: () -> Unit) {
        val (email, password) = _uiState.value.let { it.email to it.password }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.loginWithEmailPassword(email, password)
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoggedIn = true)
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.localizedMessage ?: "Login failed"
                )
            }
        }
    }

    /**
     * Update full name in UI state
     */
    fun updateFullName(value: String) {
        _uiState.value = _uiState.value.copy(fullName = value)
    }

    /**
     * Update role in UI state
     */
    fun updateRole(value: UserRole?) {
        _uiState.value = _uiState.value.copy(role = value)
    }

    /**
     * Update agreed to terms in UI state
     */
    fun updateAgreedToTerms(value: Boolean) {
        _uiState.value = _uiState.value.copy(agreedToTerms = value)
    }

    /**
     * Update blood group in UI state
     */
    fun updateBloodGroup(value: BloodGroup?) {
        _uiState.value = _uiState.value.copy(bloodGroup = value)
    }

    /**
     * Update last donation date in UI state
     */
    fun updateLastDonationDate(value: LocalDate?) {
        _uiState.value = _uiState.value.copy(lastDonationDate = value)
    }

    /**
     * Update location in UI state
     */
    fun updateLocation(latitude: Double?, longitude: Double?) {
        _uiState.value = _uiState.value.copy(
            latitude = latitude,
            longitude = longitude
        )
    }

    /**
     * Register with email and password (for backward compatibility)
     */
    fun register(onSuccess: () -> Unit) {
        val (email, password) = _uiState.value.let { it.email to it.password }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.registerWithEmailPassword(email, password)
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoggedIn = true)
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.localizedMessage ?: "Registration failed"
                )
            }
        }
    }

    /**
     * Register a new user with all required fields
     */
    fun registerUser(onSuccess: () -> Unit) {
        val state = _uiState.value

        // Validation
        if (state.phoneNumber.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Phone number is required")
            return
        }
        if (!state.phoneNumber.matches(Regex("^\\+?[0-9]{10,15}$"))) {
            _uiState.value = state.copy(errorMessage = "Invalid phone number format")
            return
        }
        if (state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Password is required")
            return
        }
        if (state.password.length < 8) {
            _uiState.value = state.copy(errorMessage = "Password must be at least 8 characters")
            return
        }
        if (state.fullName.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Full name is required")
            return
        }
        if (state.role == null) {
            _uiState.value = state.copy(errorMessage = "Please select a role")
            return
        }
        if (!state.agreedToTerms) {
            _uiState.value = state.copy(errorMessage = "You must agree to the terms")
            return
        }
        if (state.bloodGroup == null) {
            _uiState.value = state.copy(errorMessage = "Blood group is required")
            return
        }
        if (state.latitude != null && state.longitude != null) {
            if (state.latitude !in -90.0..90.0 || state.longitude !in -180.0..180.0) {
                _uiState.value = state.copy(errorMessage = "Invalid location coordinates")
                return
            }
        }

        _uiState.value = state.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.register(
                phoneNumber = state.phoneNumber,
                password = state.password,
                fullName = state.fullName,
                role = state.role,
                agreedToTerms = state.agreedToTerms,
                bloodGroup = state.bloodGroup,
                lastDonationDate = state.lastDonationDate,
                latitude = state.latitude,
                longitude = state.longitude
            )
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess {
                // Clear form fields after successful registration
                // User will be navigated back to login screen
                _uiState.value = _uiState.value.copy(
                    phoneNumber = "",
                    password = "",
                    fullName = "",
                    role = null,
                    agreedToTerms = false,
                    bloodGroup = null,
                    lastDonationDate = null,
                    latitude = null,
                    longitude = null,
                    errorMessage = null,
                    isLoggedIn = false // Don't mark as logged in - user should log in manually
                )
                // Navigate back to login screen
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.localizedMessage ?: "Registration failed"
                )
            }
        }
    }
}


