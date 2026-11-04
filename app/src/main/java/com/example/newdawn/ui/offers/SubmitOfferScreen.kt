package com.example.newdawn.ui.offers

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SubmitOfferScreen(
    jobId: Int,
    jobTitle: String,
    onSubmitted: () -> Unit,
    viewModel: SubmitOfferViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var price by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var availability by remember { mutableStateOf("") }

    LaunchedEffect(state.submitted) {
        if (state.submitted) onSubmitted()
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("SUBMIT OFFER",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text(jobTitle, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = price, onValueChange = { price = it },
            label = { Text("Proposed Price (R)") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = message, onValueChange = { message = it },
            label = { Text("Message") },
            modifier = Modifier.fillMaxWidth().height(120.dp))
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = availability, onValueChange = { availability = it },
            label = { Text("Availability (e.g. Saturday)") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)

        state.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { viewModel.submit(jobId, price, message, availability) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) CircularProgressIndicator(
                Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            ) else Text("SUBMIT OFFER")
        }
    }
}