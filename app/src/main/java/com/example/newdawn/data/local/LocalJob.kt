package com.example.newdawn.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_jobs")
data class LocalJob(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val title: String,
    val category: String,
    val description: String,
    val location: String,
    val budget: Double,
    val isSynced: Boolean = false // false means waiting to be sent to API
)