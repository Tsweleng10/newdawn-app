package com.example.newdawn.ui.offers

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
fun ViewOffersScreen(
    jobId: Int,
    viewModel: ViewOffersViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(jobId) { viewModel.load(jobId) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("OFFERS RECEIVED",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))

        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else if (state.offers.isEmpty()) {
            Text("No offers yet", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.offers) { offer ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text(offer.worker_name ?: "Worker",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold)
                            Text("Offer: R${offer.proposed_price}")
                            Text("Availability: ${offer.availability}")
                            Spacer(Modifier.height(4.dp))
                            Text(offer.message, style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(8.dp))
                            Text("Status: ${offer.status}",
                                color = if (offer.status == "ACCEPTED")
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface)
                            Spacer(Modifier.height(8.dp))
                            if (offer.status == "PENDING") {
                                Button(
                                    onClick = { viewModel.accept(offer.id, jobId) },
                                    modifier = Modifier.fillMaxWidth()
                                ) { Text("ACCEPT OFFER") }
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