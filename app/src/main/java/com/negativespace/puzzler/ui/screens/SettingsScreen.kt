package com.negativespace.puzzler.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Settings screen for accessibility and preferences.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToPrivacy: () -> Unit
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Accessibility Section
            SettingsSection(title = "Accessibility") {
                SettingsSwitch(
                    title = "Reduced Precision Mode",
                    description = "Larger touch targets for easier interaction",
                    icon = Icons.Default.TouchApp,
                    checked = settings.reducedPrecisionMode,
                    onCheckedChange = { viewModel.setReducedPrecision(it) }
                )
                
                SettingsSwitch(
                    title = "Alternative Input Mode",
                    description = "Use swipe gestures instead of taps",
                    icon = Icons.Default.SwipeRight,
                    checked = settings.alternativeInputMode,
                    onCheckedChange = { viewModel.setAlternativeInput(it) }
                )
                
                SettingsSwitch(
                    title = "High Contrast",
                    description = "Increase color contrast for better visibility",
                    icon = Icons.Default.Contrast,
                    checked = settings.highContrastMode,
                    onCheckedChange = { viewModel.setHighContrast(it) }
                )
            }
            
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            
            // Feedback Section
            SettingsSection(title = "Feedback") {
                SettingsSwitch(
                    title = "Haptic Feedback",
                    description = "Vibration feedback on interactions",
                    icon = Icons.Default.Vibration,
                    checked = settings.hapticFeedbackEnabled,
                    onCheckedChange = { viewModel.setHapticFeedback(it) }
                )
                
                SettingsSwitch(
                    title = "Sound Effects",
                    description = "Play sounds during gameplay",
                    icon = Icons.Default.VolumeUp,
                    checked = settings.soundEnabled,
                    onCheckedChange = { viewModel.setSoundEnabled(it) }
                )
            }
            
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            
            // Ads Section
            SettingsSection(title = "Support") {
                SettingsSwitch(
                    title = "Show Ads",
                    description = "Support development with non-intrusive ads",
                    icon = Icons.Default.Favorite,
                    checked = settings.showAds,
                    onCheckedChange = { viewModel.setShowAds(it) }
                )
            }
            
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            
            // Privacy Section
            SettingsSection(title = "Privacy") {
                SettingsClickable(
                    title = "Privacy Policy",
                    description = "Read our privacy policy",
                    icon = Icons.Default.PrivacyTip,
                    onClick = onNavigateToPrivacy
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Your Stats",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(
                            label = "Levels",
                            value = "${settings.currentLevel + 1}"
                        )
                        StatItem(
                            label = "Best Scores",
                            value = "${settings.highScores.size}"
                        )
                        StatItem(
                            label = "Play Time",
                            value = formatPlayTime(settings.totalPlayTime)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun SettingsSwitch(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingsClickable(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun formatPlayTime(milliseconds: Long): String {
    val hours = milliseconds / (1000 * 60 * 60)
    val minutes = (milliseconds / (1000 * 60)) % 60
    return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
}
