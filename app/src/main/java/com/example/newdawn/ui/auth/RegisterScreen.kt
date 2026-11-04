package com.example.newdawn.ui.auth

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
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    LaunchedEffect(state.isRegistered) {
        if (state.isRegistered) onRegisterSuccess()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text("CREATE ACCOUNT",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(fullName, { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(email, { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(location, { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(password, { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(confirm, { confirm = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true)

        state.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { viewModel.register(fullName, email, password, confirm, location) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) CircularProgressIndicator(
                Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            ) else Text("REGISTER")
        }
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onBackToLogin) {
            Text("Already have an account? Login")
        }
    }
}