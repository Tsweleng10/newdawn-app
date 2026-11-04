package com.example.newdawn.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val fullName: String = "",
    val email: String = "",
    val location: String = "",
    val isLoading: Boolean = true,
    val saved: Boolean = false,
    val error: String? = null
)

class EditProfileViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "EditProfileVM"
    private val tokenManager = TokenManager(app.applicationContext)
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState

    init { load() }

    private fun load() {
        viewModelScope.launch {
            val name = tokenManager.getUserName() ?: ""
            val email = tokenManager.getUserEmail() ?: ""
            val loc = tokenManager.getLocation() ?: ""
            Log.d(TAG, "Loaded profile: $name, $email, $loc")
            _uiState.value = EditProfileUiState(
                fullName = name,
                email = email,
                location = loc,
                isLoading = false
            )
        }
    }

    fun onNameChange(v: String) {
        _uiState.value = _uiState.value.copy(fullName = v)
    }
    fun onLocationChange(v: String) {
        _uiState.value = _uiState.value.copy(location = v)
    }

    fun save() {
        val name = _uiState.value.fullName.trim()
        val loc = _uiState.value.location.trim()
        if (name.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Name cannot be empty")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            // Save to DataStore (local persistence)
            tokenManager.updateProfile(name, loc)
            Log.d(TAG, "Profile updated locally: $name, $loc")
            _uiState.value = _uiState.value.copy(isLoading = false, saved = true)
        }
    }
}