package com.example.uinewdawn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import com.example.uinewdawn.data.local.TokenManager
import com.example.uinewdawn.ui.AppNavigation
import com.example.uinewdawn.ui.theme.UinewDawnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize TokenManager (CRITICAL: This loads your JWT and Dark Mode settings)
        TokenManager.init(applicationContext)

        setContent {
            // 2. Read the Dark Mode setting from DataStore
            var themeMode by remember { mutableStateOf("system") }

            LaunchedEffect(Unit) {
                themeMode = TokenManager.getThemeMode()
            }

            val isSystemDark = isSystemInDarkTheme()
            val darkTheme = when (themeMode) {
                "dark" -> true
                "light" -> false
                else -> isSystemDark
            }

            // 3. Apply the theme and load the App Navigation
            UinewDawnTheme(darkTheme = darkTheme) {
                AppNavigation()
            }
        }
    }
}