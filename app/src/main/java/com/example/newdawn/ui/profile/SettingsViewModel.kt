package com.example.newdawn.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.newdawn.data.remote.RetrofitInstance
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val language: String = "en",
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)

class SettingsViewModel(app: Application) : AndroidViewModel(app)
{
    private val TAG = "SettingsViewModel"
    private val tokenManager = TokenManager(app.applicationContext)
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        // Read the current language when the screen opens
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (!currentLocales.isEmpty) {
            val currentLang = currentLocales.toLanguageTags()
            _uiState.value = _uiState.value.copy(language = currentLang)
        }
    }
    fun setLanguage(lang: String) {
        _uiState.value = _uiState.value.copy(language = lang)
        //change Language
        val locales = LocaleListCompat.forLanguageTags(lang)
        AppCompatDelegate.setApplicationLocales(locales)
    }
    fun setNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
    }

    fun saveSettings() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null, message = null)
        viewModelScope.launch {
            try {
                val auth = tokenManager.getAuthHeader()
                val body = mapOf<String, Any>(
                    "language" to _uiState.value.language,
                    "notifications_enabled" to _uiState.value.notificationsEnabled
                )
                val res = RetrofitInstance.api.updateSettings(auth, body)
                if (res.isSuccessful) {
                    Log.d(TAG, "Settings saved")
                    _uiState.value = _uiState.value.copy(isLoading = false, message = "Settings saved")
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed (${res.code()})")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun changePassword(old: String, new: String) {
        if (old.isBlank() || new.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Fill both fields")
            return
        }
        if (new.length < 6) {
            _uiState.value = _uiState.value.copy(error = "Password must be 6+ characters")
            return
        }
        _uiState.value = _uiState.value.copy(isLoading = true, error = null, message = null)
        viewModelScope.launch {
            try {
                val auth = tokenManager.getAuthHeader()
                val body = mapOf<String, Any>(
                    "old_password" to old,
                    "new_password" to new
                )
                val res = RetrofitInstance.api.changePassword(auth, body)
                if (res.isSuccessful) {
                    Log.d(TAG, "Password changed")
                    _uiState.value = _uiState.value.copy(isLoading = false, message = "Password changed")
                } else {
                    val err = if (res.code() == 401) "Old password incorrect"
                    else "Failed (${res.code()})"
                    _uiState.value = _uiState.value.copy(isLoading = false, error = err)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}