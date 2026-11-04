package com.example.newdawn.ui.jobs

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.newdawn.data.local.AppDatabase
import com.example.newdawn.data.local.LocalJob
import com.example.newdawn.data.local.SyncWorker
import com.example.newdawn.data.models.Job
import com.example.newdawn.data.remote.RetrofitInstance
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PostJobUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPosted: Boolean = false,
    val isSavedOffline: Boolean = false // NEW: Tells UI it was saved offline
)

class PostJobViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "PostJobVM"
    private val tokenManager = TokenManager(app.applicationContext)
    private val db = AppDatabase.getDatabase(app.applicationContext)

    private val _uiState = MutableStateFlow(PostJobUiState())
    val uiState: StateFlow<PostJobUiState> = _uiState

    fun post(title: String, category: String, description: String, location: String, budget: String) {
        val budgetVal = budget.toDoubleOrNull()
        if (title.isBlank() || category.isBlank() || description.isBlank() || location.isBlank() || budgetVal == null || budgetVal <= 0) {
            _uiState.value = PostJobUiState(error = "Please fill all fields with valid values")
            return
        }

        _uiState.value = PostJobUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val auth = tokenManager.getAuthHeader()
                val job = Job(title = title, category = category, description = description, location = location, budget = budgetVal)

                // Try to send to the internet first
                val res = RetrofitInstance.api.createJob(auth, job)

                if (res.isSuccessful) {
                    Log.d(TAG, "Job posted online successfully")
                    _uiState.value = PostJobUiState(isPosted = true)
                } else {
                    _uiState.value = PostJobUiState(error = "Server error (${res.code()})")
                }
            } catch (e: Exception) {
                // 🚨 NO INTERNET DETECTED: Save to Room Database
                Log.e(TAG, "Network failed, saving offline", e)
                saveOffline(title, category, description, location, budgetVal)
            }
        }
    }

    private suspend fun saveOffline(title: String, category: String, description: String, location: String, budget: Double) {
        val localJob = LocalJob(
            title = title,
            category = category,
            description = description,
            location = location,
            budget = budget,
            isSynced = false
        )

        // 1. Save to Room
        db.jobDao().insertJob(localJob)

        // 2. Schedule WorkManager to sync when internet returns
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when internet is back
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(getApplication()).enqueue(syncRequest)

        // 3. Tell the UI it saved offline
        _uiState.value = PostJobUiState(isSavedOffline = true)
    }
}