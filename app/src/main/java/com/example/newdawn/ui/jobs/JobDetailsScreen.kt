package com.example.newdawn.ui.jobs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newdawn.data.models.Job
import com.example.newdawn.data.remote.RetrofitInstance
import com.example.newdawn.ui.navigation.Screen
import com.example.newdawn.utils.TokenManager
import androidx.compose.ui.platform.LocalContext

@Composable
fun JobDetailsScreen(
    jobId: Int,
    navController: NavController
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context.applicationContext) }
    var job by remember { mutableStateOf<Job?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(jobId) {
        try {
            val auth = tokenManager.getAuthHeader()
            val res = RetrofitInstance.api.getJobById(auth, jobId)
            if (res.isSuccessful) job = res.body()
            else error = "Failed (${res.code()})"
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoading = false
        }
    }

    Column(Modifier.fillMaxSize().padding(24.dp)
        .verticalScroll(rememberScrollState())) {
        Text("JOB DETAILS",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(24.dp))

        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(error!!, color = MaterialTheme.colorScheme.error)
            job != null -> {
                val j = job!!
                Text(j.title, style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Text("Category: ${j.category}")
                Text("Location: ${j.location}")
                Text("Budget: R${j.budget}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold)
                Text("Status: ${j.status}")
                Text("Posted by: ${j.poster_name ?: "Unknown"}")
                Spacer(Modifier.height(16.dp))
                Text("Description", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(j.description)

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        navController.navigate(
                            Screen.SubmitOffer.createRoute(j.id, j.title))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("SUBMIT OFFER") }
            }
        }
    }
}