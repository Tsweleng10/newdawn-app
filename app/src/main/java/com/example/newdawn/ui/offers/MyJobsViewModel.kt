package com.example.newdawn.ui.offers

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

data class MyJobsUiState(
    val jobs: List<Job> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class MyJobsViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "MyJobsVM"
    private val tokenManager = TokenManager(app.applicationContext)
    private val _uiState = MutableStateFlow(MyJobsUiState())
    val uiState: StateFlow<MyJobsUiState> = _uiState

    fun load() {
        _uiState.value = MyJobsUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val auth = tokenManager.getAuthHeader()
                val res = RetrofitInstance.api.getMyJobs(auth)
                if (res.isSuccessful) {
                    Log.d(TAG, "Loaded ${res.body()?.size ?: 0} jobs")
                    _uiState.value = MyJobsUiState(
                        jobs = res.body() ?: emptyList(),
                        isLoading = false
                    )
                } else {
                    _uiState.value = MyJobsUiState(
                        isLoading = false,
                        error = "Failed (${res.code()})"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error", e)
                _uiState.value = MyJobsUiState(isLoading = false, error = e.message)
            }
        }
    }
}