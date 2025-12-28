package com.negativespace.puzzler.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Privacy policy screen explaining data handling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Privacy Policy",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Last updated: December 2024",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            PrivacySection(
                title = "Summary",
                content = """
                    The Thing You Didn't Touch is designed with privacy as a core principle. 
                    Your gameplay data stays on your device. We do not collect, transmit, 
                    or share any personal information.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "Data Collection",
                content = """
                    We do NOT collect:
                    • Personal information
                    • Gameplay data
                    • Touch heatmaps
                    • Location data
                    • Device identifiers
                    • Analytics or usage data
                    
                    All game data (progress, settings, heatmaps) is stored locally on your 
                    device using Android's secure DataStore mechanism.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "Touch Tracking",
                content = """
                    The game tracks your touch interactions solely for gameplay purposes:
                    
                    • Touch points are normalized (0-1 coordinates)
                    • Heatmaps are generated locally
                    • No raw touch data is stored long-term
                    • Data is only used to determine puzzle solutions
                    • All tracking data is deleted when you reset a level
                    
                    This behavioral data NEVER leaves your device.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "Advertising",
                content = """
                    We use Google AdMob to display non-intrusive banner ads:
                    
                    • Ads are clearly labeled
                    • You can disable ads in settings
                    • We use non-personalized ads by default
                    • AdMob may collect device information as per Google's privacy policy
                    
                    For more information about Google's data practices, visit:
                    https://policies.google.com/privacy
                """.trimIndent()
            )
            
            PrivacySection(
                title = "Data Storage",
                content = """
                    All game data is stored locally using:
                    
                    • Android DataStore (preferences)
                    • Encrypted shared preferences
                    • No cloud sync or backup of game data
                    
                    You can clear all data by:
                    • Uninstalling the app
                    • Clearing app data in Android settings
                """.trimIndent()
            )
            
            PrivacySection(
                title = "Children's Privacy",
                content = """
                    This app does not knowingly collect information from children under 13. 
                    The app is suitable for all ages and contains no inappropriate content.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "GDPR Compliance",
                content = """
                    For users in the European Union:
                    
                    • No personal data is collected
                    • No consent is required as no tracking occurs
                    • You have full control over local data
                    • Data portability: Game data can be backed up via Android
                    • Right to erasure: Uninstall the app to delete all data
                """.trimIndent()
            )
            
            PrivacySection(
                title = "CCPA Compliance",
                content = """
                    For California residents:
                    
                    • We do not sell personal information
                    • We do not share personal information
                    • No data collection means no data to disclose
                """.trimIndent()
            )
            
            PrivacySection(
                title = "Changes to This Policy",
                content = """
                    We may update this privacy policy from time to time. Any changes will 
                    be reflected in the app with an updated "Last updated" date.
                """.trimIndent()
            )
            
            PrivacySection(
                title = "Contact",
                content = """
                    If you have questions about this privacy policy, please contact us at:
                    privacy@negativespace.puzzler.example.com
                """.trimIndent()
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PrivacySection(
    title: String,
    content: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}
