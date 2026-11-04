package com.example.uinewdawn.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uinewdawn.ui.theme.NewDawnGreen
import com.example.uinewdawn.ui.theme.NewDawnLightGray

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NewDawnLightGray)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(NewDawnGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "S",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Sarah Lekoane", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "sarah@example.com", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically // Keeps the lines centered with the text
            ) {
                StatItem(number = "12", label = "Jobs Posted")

                // Replaced VerticalDivider with Box
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color.LightGray)
                )

                StatItem(number = "4.7", label = "Rating", numberColor = NewDawnGreen)

                // Replaced VerticalDivider with Box
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color.LightGray)
                )

                StatItem(number = "8", label = "Reviews")
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(50),
                border = androidx.compose.foundation.BorderStroke(1.dp, NewDawnGreen),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NewDawnGreen),
                modifier = Modifier.width(160.dp)
            ) {
                Text("EDIT PROFILE", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 8.dp)
        ) {
            ProfileMenuItem(icon = Icons.Default.Bolt, title = "My Skills") {}
            ProfileMenuItem(icon = Icons.Default.Work, title = "My Active Jobs") {}
            ProfileMenuItem(icon = Icons.Default.List, title = "My Offers") {}
            ProfileMenuItem(icon = Icons.Default.Star, title = "My Reviews") {}

            // THIS IS THE SETTINGS BUTTON
            ProfileMenuItem(icon = Icons.Default.Settings, title = "Settings") {
                onNavigateToSettings()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = Color.Red),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Logout", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun StatItem(number: String, label: String, numberColor: Color = MaterialTheme.colorScheme.onSurface) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = number, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = numberColor)
        Text(text = label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = Color(0xFFE6A817), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Go", tint = Color.Gray)
    }
}