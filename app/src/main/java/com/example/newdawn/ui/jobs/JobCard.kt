package com.example.newdawn.ui.jobs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newdawn.data.models.Job

@Composable
fun JobCard(job: Job, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Column(Modifier.padding(16.dp)) {
            Text(job.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("${job.location} • R${job.budget}",
                style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Text("Posted by ${job.poster_name ?: "User"}",
                style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Text("Status: ${job.status}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall)
        }
    }
}