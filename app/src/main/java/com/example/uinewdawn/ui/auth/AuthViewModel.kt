package com.example.uinewdawn.ui.auth


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uinewdawn.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.login(email, password)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message ?: "Login failed") }
            }
        }
    }

    fun register(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.register(fullName, email, password)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message ?: "Registration failed") }
            }
        }
    }
}