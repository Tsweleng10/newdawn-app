package com.example.newdawn.ui.jobs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.newdawn.ui.navigation.Screen

@Composable
fun FindWorkScreen(
    navController: NavController,
    viewModel: FindWorkViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("FIND WORK",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = viewModel::onSearchChange,
            label = { Text("Search jobs") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)

        Spacer(Modifier.height(12.dp))

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Column {
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { viewModel.loadJobs() }) { Text("Retry") }
            }
        } else if (state.jobs.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No jobs found. Try a different search.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.jobs, key = { it.id }) { job ->
                    JobCard(job = job, onClick = {
                        navController.navigate(Screen.JobDetails.createRoute(job.id))
                    })
                }
            }
        }
    }
}