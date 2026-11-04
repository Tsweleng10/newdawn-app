package com.example.newdawn.data.local

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.newdawn.data.models.Job
import com.example.newdawn.data.remote.RetrofitInstance
import com.example.newdawn.utils.TokenManager

class SyncWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    private val TAG = "SyncWorker"

    override suspend fun doWork(): Result {
        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.jobDao()
        val tokenManager = TokenManager(applicationContext)

        val authHeader = tokenManager.getAuthHeader()
        if (authHeader.isEmpty()) return Result.failure()

        val unsyncedJobs = dao.getUnsyncedJobs()
        if (unsyncedJobs.isEmpty()) return Result.success()

        Log.d(TAG, "Found ${unsyncedJobs.size} jobs to sync...")

        for (localJob in unsyncedJobs) {
            try {
                // Convert local job to the API Job model
                val apiJob = Job(
                    title = localJob.title,
                    category = localJob.category,
                    description = localJob.description,
                    location = localJob.location,
                    budget = localJob.budget
                )

                val response = RetrofitInstance.api.createJob(authHeader, apiJob)
                if (response.isSuccessful) {
                    // Success! Delete from local Room database
                    dao.deleteJob(localJob.localId)
                    Log.d(TAG, "Successfully synced: ${localJob.title}")
                } else {
                    Log.e(TAG, "Failed to sync: ${localJob.title}, Code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Network error during sync", e)
                // If it fails due to network, return retry so WorkManager tries again later
                return Result.retry()
            }
        }
        return Result.success()
    }
}