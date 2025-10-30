package com.tofiq.blood.auth

import androidx.lifecycle.ViewModel
import com.tofiq.blood.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * UI State for settings screen
 */
data class SettingsUiState(
    val baseUrl: String = "",
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for handling settings logic
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(baseUrl = preferencesManager.getBaseUrl())
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    /**
     * Update base URL in UI state
     */
    fun updateBaseUrl(value: String) {
        _uiState.value = _uiState.value.copy(
            baseUrl = value,
            isSaved = false,
            errorMessage = null
        )
    }

    /**
     * Save base URL to preferences
     */
    fun saveBaseUrl() {
        val url = _uiState.value.baseUrl.trim()
        
        // Validation
        if (url.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Base URL cannot be empty"
            )
            return
        }
        
        // Basic URL validation
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "URL must start with http:// or https://"
            )
            return
        }
        
        preferencesManager.saveBaseUrl(url)
        _uiState.value = _uiState.value.copy(
            isSaved = true,
            errorMessage = null
        )
    }

    /**
     * Reset to default base URL
     */
    fun resetToDefault() {
        val defaultUrl = "http://192.168.103.177:8080"
        preferencesManager.saveBaseUrl(defaultUrl)
        _uiState.value = _uiState.value.copy(
            baseUrl = defaultUrl,
            isSaved = true,
            errorMessage = null
        )
    }

    /**
     * Clear saved message
     */
    fun clearSavedMessage() {
        _uiState.value = _uiState.value.copy(isSaved = false)
    }
}

