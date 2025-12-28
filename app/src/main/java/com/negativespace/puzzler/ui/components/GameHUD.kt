package com.negativespace.puzzler.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.negativespace.puzzler.domain.model.Difficulty
import com.negativespace.puzzler.domain.model.Level
import com.negativespace.puzzler.ui.theme.Success
import com.negativespace.puzzler.ui.theme.Failure

/**
 * Game HUD displaying level info, progress, and controls.
 */
@Composable
fun GameHUD(
    level: Level,
    coveragePercentage: Float,
    touchCount: Int,
    elapsedTime: Long,
    canReveal: Boolean,
    onRevealClick: () -> Unit,
    onResetClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Top row: Level info and settings
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = level.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = level.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Row {
                IconButton(onClick = onResetClick) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset level"
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Progress indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatChip(
                icon = Icons.Default.TouchApp,
                label = "Touches",
                value = touchCount.toString()
            )
            StatChip(
                icon = Icons.Default.GridOn,
                label = "Coverage",
                value = "${(coveragePercentage * 100).toInt()}%"
            )
            StatChip(
                icon = Icons.Default.Timer,
                label = "Time",
                value = formatTime(elapsedTime)
            )
            DifficultyChip(difficulty = level.difficulty)
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Coverage progress bar
        CoverageProgressBar(
            current = coveragePercentage,
            required = level.minCoverageRequired
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Reveal button
        Button(
            onClick = onRevealClick,
            enabled = canReveal,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Icon(
                imageVector = if (canReveal) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (canReveal) "Reveal Solution" else "Cover ${((level.minCoverageRequired - coveragePercentage) * 100).toInt()}% more"
            )
        }
    }
}

@Composable
fun StatChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun DifficultyChip(
    difficulty: Difficulty,
    modifier: Modifier = Modifier
) {
    val (color, text) = when (difficulty) {
        Difficulty.EASY -> Pair(Color(0xFF4CAF50), "Easy")
        Difficulty.NORMAL -> Pair(Color(0xFF2196F3), "Normal")
        Difficulty.HARD -> Pair(Color(0xFFFF9800), "Hard")
        Difficulty.EXPERT -> Pair(Color(0xFFF44336), "Expert")
    }
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CoverageProgressBar(
    current: Float,
    required: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Coverage Progress",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Required: ${(required * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { current.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = if (current >= required) Success else MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun LevelCompleteDialog(
    isSuccess: Boolean,
    score: Int,
    onNextLevel: () -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isSuccess) Icons.Default.EmojiEvents else Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = if (isSuccess) Success else Failure
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = if (isSuccess) "Level Complete!" else "Try Again",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                if (isSuccess) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Score: $score",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You touched the solution zone!",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Retry")
                    }
                    
                    if (isSuccess) {
                        Button(
                            onClick = onNextLevel,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Next Level")
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / 1000) / 60
    return String.format("%d:%02d", minutes, seconds)
}
