package com.example.newdawn.ui.jobs

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

data class FindWorkUiState(
    val searchQuery: String = "",
    val jobs: List<Job> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class FindWorkViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "FindWorkVM"
    private val tokenManager = TokenManager(app.applicationContext)
    private val _uiState = MutableStateFlow(FindWorkUiState())
    val uiState: StateFlow<FindWorkUiState> = _uiState

    init { loadJobs() }

    fun onSearchChange(q: String) {
        _uiState.value = _uiState.value.copy(searchQuery = q)
        loadJobs()
    }

    fun loadJobs() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val auth = tokenManager.getAuthHeader()
                if (auth.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false, error = "Not logged in")
                    return@launch
                }
                val res = RetrofitInstance.api.getJobs(
                    token = auth,
                    search = _uiState.value.searchQuery.ifBlank { null }
                )
                if (res.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        jobs = res.body().orEmpty(),
                        isLoading = false, error = null)
                    Log.d(TAG, "Loaded ${res.body()?.size} jobs")
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false, error = "HTTP ${res.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false, error = e.message)
            }
        }
    }
}