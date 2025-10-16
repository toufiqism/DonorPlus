package com.tofiq.blood.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)

class AuthViewModel(
    private val repository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState(isLoggedIn = repository.isLoggedIn()))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun updateEmail(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

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
                _uiState.value = _uiState.value.copy(errorMessage = e.localizedMessage)
            }
        }
    }

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
                _uiState.value = _uiState.value.copy(errorMessage = e.localizedMessage)
            }
        }
    }
}


