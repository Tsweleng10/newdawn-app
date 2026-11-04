package com.example.newdawn.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EditProfileScreen(
    onSaved: () -> Unit,
    onBack: () -> Unit,
    viewModel: EditProfileViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.saved) {
        if (state.saved) onSaved()
    }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("EDIT PROFILE",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(24.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text("Full Name", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = state.fullName,
                onValueChange = viewModel::onNameChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))
            Text("Email (cannot be changed)",
                style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = state.email,
                onValueChange = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(Modifier.height(16.dp))
            Text("Location", style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = state.location,
                onValueChange = viewModel::onLocationChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            state.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { viewModel.save() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("SAVE CHANGES") }

            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Cancel") }
        }
    }
}