package com.example.newdawn.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onLoginSuccess()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("NEWDAWN",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(8.dp))
        Text("Welcome Back", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password") },
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
            onClick = { viewModel.login(email.trim(), password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) CircularProgressIndicator(
                Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            ) else Text("LOG IN")
        }
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register")
        }
    }
}