package com.negativespace.puzzler.ui.screens

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.negativespace.puzzler.domain.model.GameStatus
import com.negativespace.puzzler.domain.model.Level
import com.negativespace.puzzler.ui.components.*

/**
 * Main game screen where the puzzle gameplay happens.
 */
@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
    onNavigateToMenu: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService<Vibrator>() }
    
    var showLevelCompleteDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(uiState.gameState?.status) {
        if (uiState.gameState?.status == GameStatus.COMPLETED || 
            uiState.gameState?.status == GameStatus.FAILED) {
            showLevelCompleteDialog = true
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Game HUD
        uiState.currentLevel?.let { level ->
            GameHUD(
                level = level,
                coveragePercentage = uiState.gameState?.coveragePercentage ?: 0f,
                touchCount = uiState.gameState?.touchCount ?: 0,
                elapsedTime = uiState.gameState?.elapsedTime ?: 0L,
                canReveal = uiState.canRevealSolution,
                onRevealClick = { viewModel.revealSolution() },
                onResetClick = { viewModel.resetLevel() },
                onSettingsClick = onNavigateToSettings
            )
        }
        
        // Puzzle canvas
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            PuzzleCanvas(
                heatmap = uiState.gameState?.heatmap ?: com.negativespace.puzzler.domain.model.Heatmap.empty(),
                solutionZone = uiState.currentLevel?.solutionZone,
                showSolution = uiState.gameState?.solutionRevealed == true,
                onTouch = { touchPoint ->
                    viewModel.processTouch(touchPoint)
                    if (settings.hapticFeedbackEnabled) {
                        vibrator?.vibrate(
                            VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE)
                        )
                    }
                },
                isEnabled = uiState.gameState?.status == GameStatus.PLAYING,
                highContrastMode = settings.highContrastMode,
                reducedPrecisionMode = settings.reducedPrecisionMode
            )
        }
        
        // Ad banner
        AdBanner(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            isVisible = settings.showAds
        )
    }
    
    // Level complete dialog
    if (showLevelCompleteDialog) {
        LevelCompleteDialog(
            isSuccess = uiState.gameState?.status == GameStatus.COMPLETED,
            score = uiState.gameState?.score ?: 0,
            onNextLevel = {
                showLevelCompleteDialog = false
                viewModel.nextLevel()
            },
            onRetry = {
                showLevelCompleteDialog = false
                viewModel.resetLevel()
            },
            onDismiss = {
                showLevelCompleteDialog = false
            }
        )
    }
}
