package com.negativespace.puzzler.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the current state of a game session.
 */
@Serializable
data class GameState(
    val currentLevelId: Int = 0,
    val heatmap: Heatmap = Heatmap.empty(),
    val touchPoints: List<TouchPoint> = emptyList(),
    val startTime: Long = System.currentTimeMillis(),
    val status: GameStatus = GameStatus.PLAYING,
    val solutionRevealed: Boolean = false,
    val score: Int = 0
) {
    val touchCount: Int get() = touchPoints.size
    
    val elapsedTime: Long get() = System.currentTimeMillis() - startTime
    
    val coveragePercentage: Float get() = heatmap.getCoveragePercentage()
    
    fun addTouch(point: TouchPoint): GameState {
        return copy(
            touchPoints = touchPoints + point,
            heatmap = heatmap.addTouch(point)
        )
    }
    
    fun revealSolution(): GameState {
        return copy(solutionRevealed = true)
    }
    
    fun complete(level: Level): GameState {
        val solutionTouched = touchPoints.any { level.solutionZone.containsTouch(it) }
        val newStatus = if (solutionTouched) GameStatus.FAILED else GameStatus.COMPLETED
        val calculatedScore = if (newStatus == GameStatus.COMPLETED) {
            calculateScore(level)
        } else 0
        
        return copy(
            status = newStatus,
            score = calculatedScore
        )
    }
    
    private fun calculateScore(level: Level): Int {
        val baseScore = 1000
        val coverageBonus = (coveragePercentage * 500).toInt()
        val efficiencyBonus = maxOf(0, 300 - (touchCount * 2))
        val difficultyMultiplier = when (level.difficulty) {
            Difficulty.EASY -> 1.0f
            Difficulty.NORMAL -> 1.5f
            Difficulty.HARD -> 2.0f
            Difficulty.EXPERT -> 3.0f
        }
        
        return ((baseScore + coverageBonus + efficiencyBonus) * difficultyMultiplier).toInt()
    }
    
    companion object {
        fun forLevel(levelId: Int): GameState {
            return GameState(currentLevelId = levelId)
        }
    }
}

@Serializable
enum class GameStatus {
    PLAYING,
    COMPLETED,
    FAILED,
    PAUSED
}
