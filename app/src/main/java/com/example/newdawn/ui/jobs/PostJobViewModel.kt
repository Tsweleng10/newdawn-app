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

data class PostJobUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPosted: Boolean = false
)

class PostJobViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "PostJobVM"
    private val tokenManager = TokenManager(app.applicationContext)
    private val _uiState = MutableStateFlow(PostJobUiState())
    val uiState: StateFlow<PostJobUiState> = _uiState

    fun post(title: String, category: String, description: String,
             location: String, budget: String) {
        val budgetVal = budget.toDoubleOrNull()
        if (title.isBlank() || category.isBlank() || description.isBlank() ||
            location.isBlank() || budgetVal == null || budgetVal <= 0) {
            _uiState.value = PostJobUiState(error = "Please fill all fields with valid values")
            return
        }
        _uiState.value = PostJobUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val auth = tokenManager.getAuthHeader()
                val job = Job(
                    title = title, category = category,
                    description = description, location = location,
                    budget = budgetVal
                )
                val res = RetrofitInstance.api.createJob(auth, job)
                if (res.isSuccessful) {
                    Log.d(TAG, "Job posted")
                    _uiState.value = PostJobUiState(isPosted = true)
                } else {
                    _uiState.value = PostJobUiState(error = "Failed (${res.code()})")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error", e)
                _uiState.value = PostJobUiState(error = e.message)
            }
        }
    }
}