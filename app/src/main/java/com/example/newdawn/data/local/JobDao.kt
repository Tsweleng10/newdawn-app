package com.example.newdawn.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface JobDao {
    // Save a new offline job
    @Insert
    suspend fun insertJob(job: LocalJob)

    // Get all jobs that haven't been sent to the internet yet
    @Query("SELECT * FROM offline_jobs WHERE isSynced = 0")
    suspend fun getUnsyncedJobs(): List<LocalJob>

    // Delete a job once it successfully syncs (keeps the DB clean)
    @Query("DELETE FROM offline_jobs WHERE localId = :id")
    suspend fun deleteJob(id: Int)
}