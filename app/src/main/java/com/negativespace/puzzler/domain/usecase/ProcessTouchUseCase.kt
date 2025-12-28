package com.negativespace.puzzler.domain.usecase

import com.negativespace.puzzler.domain.model.GameState
import com.negativespace.puzzler.domain.model.GameStatus
import com.negativespace.puzzler.domain.model.Level
import com.negativespace.puzzler.domain.model.TouchPoint
import javax.inject.Inject

/**
 * Use case for processing touch input during gameplay.
 */
class ProcessTouchUseCase @Inject constructor() {
    
    /**
     * Processes a touch event and returns the updated game state.
     * Checks if the touch hit the solution zone.
     */
    operator fun invoke(
        currentState: GameState,
        level: Level,
        touchPoint: TouchPoint
    ): TouchResult {
        // Check if game is still active
        if (currentState.status != GameStatus.PLAYING) {
            return TouchResult.Ignored(currentState)
        }
        
        // Add the touch to the state
        val newState = currentState.addTouch(touchPoint)
        
        // Check if the touch hit the solution zone
        val hitSolution = level.solutionZone.containsTouch(touchPoint)
        
        return if (hitSolution) {
            // Player touched the solution zone - they lose this attempt
            TouchResult.SolutionTouched(newState.copy(status = GameStatus.FAILED))
        } else {
            TouchResult.Success(newState)
        }
    }
}

sealed class TouchResult {
    abstract val state: GameState
    
    data class Success(override val state: GameState) : TouchResult()
    data class SolutionTouched(override val state: GameState) : TouchResult()
    data class Ignored(override val state: GameState) : TouchResult()
}
