package com.example.newdawn.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToMyJobs: () -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tokenManager = remember { TokenManager(context.applicationContext) }

    var userName by remember { mutableStateOf("Loading...") }
    var userEmail by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        userName = tokenManager.getUserName() ?: "User"
        userEmail = tokenManager.getUserEmail() ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        // Avatar with first initial
        Surface(
            modifier = Modifier.size(96.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = userName.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    //  Changed to onPrimary so it's readable on the green avatar
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = userEmail,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = onNavigateToEditProfile,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("EDIT PROFILE")
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        // My Jobs
        ListItem(
            headlineContent = { Text("My Jobs") },
            supportingContent = { Text("View jobs you've posted") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onNavigateToMyJobs,
            modifier = Modifier.fillMaxWidth()
        ) { Text("VIEW MY JOBS") }

        Spacer(Modifier.height(16.dp))

        // Settings (Cleaned up duplicate button)
        ListItem(
            headlineContent = { Text("Settings") },
            supportingContent = { Text("Language, notifications, password") },
            leadingContent = {
                Icon(Icons.Filled.Settings, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onNavigateToSettings,
            modifier = Modifier.fillMaxWidth()
        ) { Text("OPEN SETTINGS") }

        Spacer(Modifier.height(32.dp))

        // Logout
        OutlinedButton(
            onClick = {
                scope.launch {
                    tokenManager.clear()
                    onLogout()
                }
            },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) { Text("LOGOUT") }

        Spacer(Modifier.height(24.dp))
    }
}