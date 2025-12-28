package com.negativespace.puzzler.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.negativespace.puzzler.data.repository.GameRepository
import com.negativespace.puzzler.data.repository.SettingsRepository
import com.negativespace.puzzler.domain.model.*
import com.negativespace.puzzler.domain.usecase.CheckSolutionUseCase
import com.negativespace.puzzler.domain.usecase.ProcessTouchUseCase
import com.negativespace.puzzler.domain.usecase.SolutionCheckResult
import com.negativespace.puzzler.domain.usecase.TouchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: SettingsRepository,
    private val processTouchUseCase: ProcessTouchUseCase,
    private val checkSolutionUseCase: CheckSolutionUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    
    val settings: StateFlow<UserSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())
    
    init {
        loadGame()
    }
    
    private fun loadGame() {
        viewModelScope.launch {
            gameRepository.gameState.collect { savedState ->
                if (savedState != null && savedState.status == GameStatus.PLAYING) {
                    val level = gameRepository.getLevel(savedState.currentLevelId)
                    _uiState.update { state ->
                        state.copy(
                            gameState = savedState,
                            currentLevel = level,
                            canRevealSolution = level?.let { 
                                checkSolutionUseCase.canRevealSolution(savedState, it) 
                            } ?: false
                        )
                    }
                } else {
                    // Start from current progress
                    settings.value.let { userSettings ->
                        startLevel(userSettings.currentLevel)
                    }
                }
            }
        }
    }
    
    fun startLevel(levelId: Int) {
        val level = gameRepository.getLevel(levelId) ?: gameRepository.getLevels().firstOrNull()
        level?.let {
            val gridSize = settings.value.effectiveGridSize
            val newState = GameState(
                currentLevelId = it.id,
                heatmap = Heatmap.empty(gridSize, gridSize)
            )
            _uiState.update { state ->
                state.copy(
                    gameState = newState,
                    currentLevel = level,
                    canRevealSolution = false
                )
            }
            viewModelScope.launch {
                gameRepository.saveGameState(newState)
            }
        }
    }
    
    fun processTouch(touchPoint: TouchPoint) {
        val currentState = _uiState.value.gameState ?: return
        val level = _uiState.value.currentLevel ?: return
        
        if (currentState.status != GameStatus.PLAYING) return
        
        val result = processTouchUseCase(currentState, level, touchPoint)
        
        val newState = result.state
        val canReveal = checkSolutionUseCase.canRevealSolution(newState, level)
        
        _uiState.update { state ->
            state.copy(
                gameState = newState,
                canRevealSolution = canReveal
            )
        }
        
        viewModelScope.launch {
            gameRepository.saveGameState(newState)
        }
        
        if (result is TouchResult.SolutionTouched) {
            handleGameEnd(newState, level, false)
        }
    }
    
    fun revealSolution() {
        val currentState = _uiState.value.gameState ?: return
        val level = _uiState.value.currentLevel ?: return
        
        val result = checkSolutionUseCase(currentState, level)
        
        when (result) {
            is SolutionCheckResult.Solved -> {
                val completedState = currentState.copy(
                    status = GameStatus.COMPLETED,
                    solutionRevealed = true,
                    score = result.score
                )
                _uiState.update { state ->
                    state.copy(gameState = completedState)
                }
                handleGameEnd(completedState, level, true)
            }
            is SolutionCheckResult.Failed -> {
                val failedState = currentState.copy(
                    status = GameStatus.FAILED,
                    solutionRevealed = true
                )
                _uiState.update { state ->
                    state.copy(gameState = failedState)
                }
                handleGameEnd(failedState, level, false)
            }
            is SolutionCheckResult.InsufficientCoverage -> {
                // This shouldn't happen if button is disabled correctly
            }
        }
    }
    
    private fun handleGameEnd(state: GameState, level: Level, success: Boolean) {
        viewModelScope.launch {
            gameRepository.saveGameState(state)
            
            if (success) {
                settingsRepository.saveHighScore(level.id, state.score)
                settingsRepository.updateLevelProgress(level.id + 1)
            }
            
            settingsRepository.addPlayTime(state.elapsedTime)
        }
    }
    
    fun resetLevel() {
        val level = _uiState.value.currentLevel ?: return
        startLevel(level.id)
    }
    
    fun nextLevel() {
        val currentLevel = _uiState.value.currentLevel ?: return
        val nextLevel = gameRepository.getNextLevel(currentLevel.id)
        
        if (nextLevel != null) {
            startLevel(nextLevel.id)
        } else {
            // Game completed - return to first level or show completion
            startLevel(0)
        }
    }
}

data class GameUiState(
    val gameState: GameState? = null,
    val currentLevel: Level? = null,
    val canRevealSolution: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
