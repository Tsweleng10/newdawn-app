package com.example.newdawn.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newdawn.data.models.RegisterRequest
import com.example.newdawn.data.remote.RetrofitInstance
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false
)

class RegisterViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "RegisterViewModel"
    private val tokenManager = TokenManager(app.applicationContext)
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(fullName: String, email: String, password: String, confirm: String, location: String) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank() || location.isBlank()) {
            _uiState.value = RegisterUiState(error = "Please fill in all fields")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = RegisterUiState(error = "Invalid email format")
            return
        }
        if (password.length < 6) {
            _uiState.value = RegisterUiState(error = "Password must be at least 6 characters")
            return
        }
        if (password != confirm) {
            _uiState.value = RegisterUiState(error = "Passwords do not match")
            return
        }

        _uiState.value = RegisterUiState(isLoading = true)

        viewModelScope.launch {
            try {
                Log.d(TAG, "Registering $email")
                val response = RetrofitInstance.api.register(
                    RegisterRequest(fullName.trim(), email.trim(), password, location.trim())
                )

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    tokenManager.saveToken(body.token, body.user.id, body.user.full_name)
                    tokenManager.saveUserEmail(body.user.email)
                    tokenManager.saveLocation(body.user.location ?: "")

                    Log.d(TAG, "Registration successful")
                    _uiState.value = RegisterUiState(isRegistered = true)
                } else {
                    val msg = when (response.code()) {
                        409 -> "Email already registered"
                        400 -> "Missing required fields"
                        else -> "Registration failed (${response.code()})"
                    }
                    _uiState.value = RegisterUiState(error = msg)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Network error", e)
                // Catch specific network errors and show a friendly message
                val errorMsg = if (e is UnknownHostException || e is SocketTimeoutException || e is IOException) {
                    "No internet connection. Please check your network."
                } else {
                    "An unexpected error occurred. Please try again."
                }
                _uiState.value = RegisterUiState(error = errorMsg)
            }
        }
    }
}