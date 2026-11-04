package com.example.newdawn.ui.jobs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PostJobScreen(
    onJobPosted: () -> Unit,
    viewModel: PostJobViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Gardening") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }

    val categories = listOf("Gardening","Cleaning","Plumbing","Electrical",
        "Painting","Moving","Childcare","Cooking","Other")

    LaunchedEffect(state.isPosted) { if (state.isPosted) onJobPosted() }

    Column(Modifier.fillMaxSize().padding(24.dp)
        .verticalScroll(rememberScrollState())) {
        Text("POST A JOB",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(title, { title = it },
            label = { Text("Job Title") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(12.dp))

        Text("Category", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(4.dp))
        ScrollableTabRow(selectedTabIndex = categories.indexOf(category)) {
            categories.forEach { cat ->
                Tab(selected = category == cat,
                    onClick = { category = cat },
                    text = { Text(cat, style = MaterialTheme.typography.bodySmall) })
            }
        }
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(description, { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth().height(120.dp))
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(location, { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(budget, { budget = it },
            label = { Text("Budget (R)") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(16.dp))

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = { viewModel.post(title, category, description, location, budget) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) CircularProgressIndicator(
                Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary)
            else Text("POST JOB")
        }
    }
}