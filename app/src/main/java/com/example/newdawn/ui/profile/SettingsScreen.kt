package com.example.newdawn.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showPasswordDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.message) {
        state.message?.let { snackbarHostState.showSnackbar(it) }
    }
    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("SETTINGS",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(24.dp))

            Text("Language", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("en" to "English", "zu" to "isiZulu", "af" to "Afrikaans")
                    .forEach { (code, label) ->
                        FilterChip(
                            selected = state.language == code,
                            onClick = { viewModel.setLanguage(code) },
                            label = { Text(label) }
                        )
                    }
            }

            Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Notifications", modifier = Modifier.weight(1f))
                Switch(
                    checked = state.notificationsEnabled,
                    onCheckedChange = { viewModel.setNotifications(it) }
                )
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { viewModel.saveSettings() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) { Text("SAVE SETTINGS") }

            Spacer(Modifier.height(16.dp))
            OutlinedButton(
                onClick = { showPasswordDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) { Text("CHANGE PASSWORD") }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) { Text("LOGOUT") }

            if (state.isLoading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }

    if (showPasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showPasswordDialog = false },
            onConfirm = { old, new ->
                viewModel.changePassword(old, new)
                showPasswordDialog = false
            }
        )
    }
}

@Composable
private fun ChangePasswordDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var oldPw by remember { mutableStateOf("") }
    var newPw by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password") },
        text = {
            Column {
                OutlinedTextField(oldPw, { oldPw = it },
                    label = { Text("Old Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(newPw, { newPw = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(oldPw, newPw) }) { Text("CONFIRM") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}