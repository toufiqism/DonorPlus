package com.tofiq.blood.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI State for authentication screens
 */
data class AuthUiState(
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
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
     * Register with email and password
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
}


