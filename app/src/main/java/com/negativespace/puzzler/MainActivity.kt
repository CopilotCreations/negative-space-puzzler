package com.negativespace.puzzler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.negativespace.puzzler.data.repository.GameRepository
import com.negativespace.puzzler.data.repository.SettingsRepository
import com.negativespace.puzzler.domain.model.Level
import com.negativespace.puzzler.ui.screens.*
import com.negativespace.puzzler.ui.theme.NegativeSpacePuzzlerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var settingsRepository: SettingsRepository
    
    @Inject
    lateinit var gameRepository: GameRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val settings by settingsRepository.settings.collectAsStateWithLifecycle(
                initialValue = com.negativespace.puzzler.domain.model.UserSettings()
            )
            
            NegativeSpacePuzzlerTheme(
                highContrast = settings.highContrastMode
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        levels = gameRepository.getLevels(),
                        currentProgress = settings.currentLevel,
                        highScores = settings.highScores
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    levels: List<Level>,
    currentProgress: Int,
    highScores: Map<Int, Int>
) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "menu"
    ) {
        composable("menu") {
            val gameViewModel: GameViewModel = hiltViewModel()
            val settings by gameViewModel.settings.collectAsStateWithLifecycle()
            val uiState by gameViewModel.uiState.collectAsStateWithLifecycle()
            
            MainMenuScreen(
                currentLevel = settings.currentLevel,
                totalLevels = levels.size,
                highestScore = settings.highScores.values.maxOrNull() ?: 0,
                onStartGame = {
                    gameViewModel.startLevel(0)
                    navController.navigate("game")
                },
                onContinueGame = {
                    navController.navigate("game")
                },
                onLevelSelect = {
                    navController.navigate("levels")
                },
                onSettings = {
                    navController.navigate("settings")
                },
                onPrivacy = {
                    navController.navigate("privacy")
                },
                hasSavedGame = uiState.gameState != null
            )
        }
        
        composable("game") {
            GameScreen(
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onNavigateToMenu = {
                    navController.popBackStack("menu", inclusive = false)
                }
            )
        }
        
        composable("levels") {
            val gameViewModel: GameViewModel = hiltViewModel()
            val settings by gameViewModel.settings.collectAsStateWithLifecycle()
            
            LevelSelectScreen(
                levels = levels,
                currentProgress = settings.currentLevel,
                highScores = settings.highScores,
                onLevelSelect = { levelId ->
                    gameViewModel.startLevel(levelId)
                    navController.navigate("game") {
                        popUpTo("levels") { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToPrivacy = {
                    navController.navigate("privacy")
                }
            )
        }
        
        composable("privacy") {
            PrivacyScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
