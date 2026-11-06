package com.example.newdawn.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.newdawn.ui.jobs.JobCard
import com.example.newdawn.ui.navigation.Screen
import androidx.compose.ui.res.stringResource
import com.example.newdawn.R

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(stringResource(R.string.home_hello, state.userName),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(stringResource(R.string.home_prompt),
            style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate(Screen.FindWork.route) },
            modifier = Modifier.fillMaxWidth().height(64.dp)
        ) { Text(stringResource(R.string.home_find_work), style = MaterialTheme.typography.titleMedium) }

        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = { navController.navigate(Screen.PostJob.route) },
            modifier = Modifier.fillMaxWidth().height(64.dp)
        ) { Text(stringResource(R.string.home_post_job), style = MaterialTheme.typography.titleMedium) }

        Spacer(Modifier.height(32.dp))
        Text(stringResource(R.string.home_recommended),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(12.dp))

        when {
            state.isLoading -> Box(Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            state.error != null -> Column {
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { viewModel.loadHome() }) { Text("Retry") }
            }
            state.recommendedJobs.isEmpty() -> Text(
                "No jobs available yet. Be the first to post one!",
                style = MaterialTheme.typography.bodyMedium)
            else -> Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.recommendedJobs.forEach { job ->
                    JobCard(job = job,
                        onClick = { navController.navigate(
                            Screen.JobDetails.createRoute(job.id)) })
                }
            }
        }
    }
}