package com.example.newdawn.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define your new professional palette
val CreamWhite = Color(0xFFFDFBF7) // The subtle pattern color
val PureWhite = Color(0xFFFFFFFF)  // Main background
val NewDawnGreen = Color(0xFF5DBB63) // Your accent
val DarkText = Color(0xFF1E1E1E)

private val LightColorScheme = lightColorScheme(
    primary = NewDawnGreen,
    onPrimary = Color.White,
    background = PureWhite,
    onBackground = DarkText,
    surface = PureWhite,
    onSurface = DarkText,
    surfaceVariant = CreamWhite // Used for cards
)

@Composable
fun NewDawnTheme(
    // We force darkTheme to false so the app always looks professional and white
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            // Makes status bar icons dark so you can see them on the white background
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}