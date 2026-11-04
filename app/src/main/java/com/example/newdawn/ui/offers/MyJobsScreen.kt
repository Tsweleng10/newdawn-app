package com.example.newdawn.ui.offers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MyJobsScreen(
    onJobClick: (Int) -> Unit,
    viewModel: MyJobsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("OPEN", "ACTIVE", "COMPLETED")

    LaunchedEffect(Unit) { viewModel.load() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("MY JOBS",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(8.dp))

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else {
            val statusFilter = when (selectedTab) {
                0 -> "OPEN"
                1 -> "ACTIVE"
                else -> "COMPLETED"
            }
            val filtered = state.jobs.filter {
                it.status.equals(statusFilter, ignoreCase = true)
            }
            if (filtered.isEmpty()) {
                Text("No $statusFilter jobs")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filtered) { job ->
                        Card(Modifier.fillMaxWidth().clickable { onJobClick(job.id) }) {
                            Column(Modifier.padding(16.dp)) {
                                Text(job.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold)
                                Text("${job.location} • R${job.budget}")
                                Spacer(Modifier.height(4.dp))
                                Text("Status: ${job.status}",
                                    color = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.height(8.dp))
                                Button(
                                    onClick = { onJobClick(job.id) },
                                    modifier = Modifier.fillMaxWidth()
                                ) { Text("VIEW OFFERS") }
                            }
                        }
                    }
                }
            }
        }
        state.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}