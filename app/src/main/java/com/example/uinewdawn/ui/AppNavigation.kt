package com.example.uinewdawn.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uinewdawn.data.local.TokenManager
import com.example.uinewdawn.ui.auth.LoginScreen
import com.example.uinewdawn.ui.auth.RegisterScreen
import com.example.uinewdawn.ui.profile.ProfileScreen
import com.example.uinewdawn.ui.settings.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val startDestination = remember {
        if (TokenManager.isLoggedIn()) "home" else "login"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
                }
            )
        }

        composable("home") {
            androidx.compose.foundation.layout.Column {
                androidx.compose.material3.Text("Home")
                androidx.compose.material3.Button(onClick = { navController.navigate("profile") }) {
                    androidx.compose.material3.Text("Go to Profile")
                }
            }
        }

        composable("profile") {
            ProfileScreen(
                onLogout = {
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }

        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                }
            )
        }
    }
}