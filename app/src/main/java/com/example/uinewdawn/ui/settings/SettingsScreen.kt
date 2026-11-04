package com.example.uinewdawn.ui.settings
import com.example.uinewdawn.data.local.TokenManager
import com.example.uinewdawn.ui.theme.NewDawnGreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    var themeMode by remember { mutableStateOf("system") }
    val scope = rememberCoroutineScope()
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SETTINGS", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {

            // 1. ACCOUNT & PROFILE
            SettingsExpandableCard(title = "Account & Profile", icon = Icons.Default.Person) {
                SettingsSubItem(
                    title = "Edit Profile",
                    subtitle = "Update name, photo, and bio"
                ) { /* TODO */ }
                SettingsSubItem(
                    title = "Change Password",
                    subtitle = "Update your login credentials"
                ) { /* TODO */ }
                SettingsSubItem(
                    title = "Verify Identity",
                    subtitle = "Upload ID or verification documents"
                ) { /* TODO */ }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. APPEARANCE & DISPLAY
            SettingsExpandableCard(
                title = "Appearance & Display",
                icon = Icons.Default.Brightness6,
                initiallyExpanded = true
            ) {
                SettingsSubItem(
                    title = "Theme",
                    subtitle = when (themeMode) {
                        "light" -> "Light Mode"
                        "dark" -> "Dark Mode"
                        else -> "System Default"
                    }
                ) { showThemeDialog = true }

                SettingsSubItem(
                    title = "Text Size",
                    subtitle = "Adjust app font size"
                ) { /* TODO */ }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. NOTIFICATIONS & COMMUNICATIONS
            SettingsExpandableCard(
                title = "Notifications & Comms",
                icon = Icons.Default.Notifications
            ) {
                SettingsSubItem(
                    title = "Push Notifications",
                    subtitle = "Jobs, offers, and messages"
                ) { /* TODO */ }
                SettingsSubItem(
                    title = "Email Preferences",
                    subtitle = "Manage email alerts"
                ) { /* TODO */ }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. DATA STORAGE & PRIVACY
            SettingsExpandableCard(
                title = "Data Storage & Privacy",
                icon = Icons.Default.Security
            ) {
                SettingsSubItem(
                    title = "Clear Cache",
                    subtitle = "Free up storage space"
                ) { /* TODO */ }
                SettingsSubItem(
                    title = "Download My Data",
                    subtitle = "Export your account data"
                ) { /* TODO */ }
                SettingsSubItem(
                    title = "Privacy Policy",
                    subtitle = "How we handle your data"
                ) { /* TODO */ }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 5. SUPPORT & LEGAL
            SettingsExpandableCard(title = "Support & Legal", icon = Icons.Default.HelpOutline) {
                SettingsSubItem(title = "Help Center", subtitle = "FAQs and guides") { /* TODO */ }
                SettingsSubItem(
                    title = "Terms of Service",
                    subtitle = "Read our terms"
                ) { /* TODO */ }
                SettingsSubItem(title = "About NewDawn", subtitle = "Version 1.0.0") { /* TODO */ }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // LOGOUT BUTTON
            Button(
                onClick = {
                    scope.launch {
                        TokenManager.clear()
                        onLogout()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                )
            ) {
                Text("LOG OUT", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // THEME PICKER DIALOG (Kept from previous step)
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Choose Theme") },
            text = {
                Column {
                    ThemeOption("system", "Follow system", themeMode) {
                        themeMode = "system"
                        showThemeDialog = false
                    }
                    ThemeOption("light", "Light", themeMode) {
                        themeMode = "light"
                        showThemeDialog = false
                    }
                    ThemeOption("dark", "Dark", themeMode) {
                        themeMode = "dark"
                        showThemeDialog = false
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) { Text("CANCEL") }
            }
        )
    }
@Composable
fun SettingsExpandableCard(
    title: String,
    icon: ImageVector,
    initiallyExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = NewDawnGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Expandable Content
            AnimatedVisibility(visible = expanded) {
                Column {
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                    content()
                }
            }
        }
    }
}

@Composable
fun SettingsSubItem(title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Go",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ThemeOption(value: String, label: String, selected: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = value == selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = NewDawnGreen)
        )
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}