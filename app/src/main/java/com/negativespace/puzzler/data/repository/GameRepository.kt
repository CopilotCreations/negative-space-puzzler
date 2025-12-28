package com.negativespace.puzzler.data.repository

import com.negativespace.puzzler.data.local.GameStateDataStore
import com.negativespace.puzzler.domain.model.GameState
import com.negativespace.puzzler.domain.model.Level
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for game state and level data.
 * Manages game persistence and level loading.
 */
@Singleton
class GameRepository @Inject constructor(
    private val gameStateDataStore: GameStateDataStore
) {
    val gameState: Flow<GameState?> = gameStateDataStore.gameStateFlow
    
    private val levels: List<Level> = Level.generateLevels()
    
    fun getLevels(): List<Level> = levels
    
    fun getLevel(id: Int): Level? = levels.find { it.id == id }
    
    fun getNextLevel(currentId: Int): Level? {
        val currentIndex = levels.indexOfFirst { it.id == currentId }
        return if (currentIndex >= 0 && currentIndex < levels.size - 1) {
            levels[currentIndex + 1]
        } else null
    }
    
    fun getTotalLevelCount(): Int = levels.size
    
    suspend fun saveGameState(state: GameState) {
        gameStateDataStore.saveGameState(state)
    }
    
    suspend fun clearGameState() {
        gameStateDataStore.clearGameState()
    }
}
