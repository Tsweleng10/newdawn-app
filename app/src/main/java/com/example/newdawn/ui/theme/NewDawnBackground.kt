package com.example.newdawn.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NewDawnBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite) // Set the base background to white
    ) {
        // Draw the subtle cream diagonal lines
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 150.dp.toPx()
            val strokeWidth = 40.dp.toPx()

            // Draw diagonal lines starting from the top-left going down-right
            var startX = -size.height
            while (startX < size.width) {
                drawLine(
                    color = CreamWhite,
                    start = Offset(startX, 0f),
                    end = Offset(startX + size.height, size.height),
                    strokeWidth = strokeWidth
                )
                startX += step
            }
        }

        // This places your screens ON TOP of the pattern
        content()
    }
}