package com.example.newdawn.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newdawn.data.models.Job
import com.example.newdawn.data.remote.RetrofitInstance
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val userName: String = "",
    val recommendedJobs: List<Job> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "HomeVM"
    private val tokenManager = TokenManager(app.applicationContext)
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init { loadHome() }

    fun loadHome() {
        _uiState.value = HomeUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val name = tokenManager.getUserName() ?: "there"
                val auth = tokenManager.getAuthHeader()
                if (auth.isEmpty()) {
                    _uiState.value = HomeUiState(error = "Not logged in")
                    return@launch
                }
                val res = RetrofitInstance.api.getJobs(auth)
                if (res.isSuccessful) {
                    val jobs = res.body().orEmpty().take(3)
                    Log.d(TAG, "Loaded ${jobs.size} recommended jobs")
                    _uiState.value = HomeUiState(
                        userName = name,
                        recommendedJobs = jobs,
                        isLoading = false
                    )
                } else {
                    _uiState.value = HomeUiState(
                        error = "HTTP ${res.code()}", isLoading = false)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading home", e)
                _uiState.value = HomeUiState(
                    error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }
}