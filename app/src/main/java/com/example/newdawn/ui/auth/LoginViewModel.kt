package com.example.newdawn.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newdawn.data.models.LoginRequest
import com.example.newdawn.data.remote.RetrofitInstance
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "LoginViewModel"
    private val tokenManager = TokenManager(app.applicationContext)
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(error = "Please fill in all fields")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = LoginUiState(error = "Invalid email format")
            return
        }

        _uiState.value = LoginUiState(isLoading = true)

        viewModelScope.launch {
            try {
                Log.d(TAG, "Attempting login for $email")
                val response = RetrofitInstance.api.login(LoginRequest(email, password))

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    tokenManager.saveToken(body.token, body.user.id, body.user.full_name)
                    tokenManager.saveUserEmail(body.user.email)
                    tokenManager.saveLocation(body.user.location ?: "")

                    Log.d(TAG, "Login successful")
                    _uiState.value = LoginUiState(isLoggedIn = true)
                } else {
                    val msg = when (response.code()) {
                        401 -> "Invalid email or password"
                        400 -> "Missing required fields"
                        else -> "Login failed (${response.code()})"
                    }
                    _uiState.value = LoginUiState(error = msg)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Network error", e)
                // Catch specific network errors and show a friendly message
                val errorMsg = if (e is UnknownHostException || e is SocketTimeoutException || e is IOException) {
                    "No internet connection. Please check your network."
                } else {
                    "An unexpected error occurred. Please try again."
                }
                _uiState.value = LoginUiState(error = errorMsg)
            }
        }
    }
}