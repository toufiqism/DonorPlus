package com.tofiq.blood.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import com.tofiq.blood.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * UI State for Profile screen
 */
data class ProfileUiState(
    val isLoggedIn: Boolean = true
)

/**
 * ViewModel for ProfileScreen
 * Handles user profile operations including logout
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoggedIn = authRepository.isLoggedIn()))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    /**
     * Signs out the current user by clearing auth data
     * Sets isLoggedIn to false and invokes callback for navigation
     * @param onComplete Callback invoked after sign out completes
     */
    fun logout(onComplete: () -> Unit) {
        // Clear auth data from repository
        authRepository.signOut()
        // Update UI state to reflect logged out status
        _uiState.value = _uiState.value.copy(isLoggedIn = false)
        // Trigger navigation callback
        onComplete()
    }
}
