package com.negativespace.puzzler.domain.usecase

import com.negativespace.puzzler.domain.model.GameState
import com.negativespace.puzzler.domain.model.GameStatus
import com.negativespace.puzzler.domain.model.Heatmap
import com.negativespace.puzzler.domain.model.Level
import javax.inject.Inject

/**
 * Use case for checking if the puzzle is solved.
 * The puzzle is solved when the player has sufficient coverage
 * and has NOT touched the solution zone.
 */
class CheckSolutionUseCase @Inject constructor() {
    
    /**
     * Checks if the current game state represents a valid solution.
     */
    operator fun invoke(
        currentState: GameState,
        level: Level
    ): SolutionCheckResult {
        // Check minimum coverage requirement
        val coverage = currentState.heatmap.getCoveragePercentage()
        if (coverage < level.minCoverageRequired) {
            return SolutionCheckResult.InsufficientCoverage(
                currentCoverage = coverage,
                requiredCoverage = level.minCoverageRequired
            )
        }
        
        // Check if solution zone was touched
        val solutionTouched = currentState.touchPoints.any { 
            level.solutionZone.containsTouch(it) 
        }
        
        return if (solutionTouched) {
            SolutionCheckResult.Failed
        } else {
            // Calculate how much of the solution zone is untouched
            val solutionCells = level.solutionZone.getCoveredCells(
                currentState.heatmap.gridWidth,
                currentState.heatmap.gridHeight
            )
            val untouchedSolutionCells = solutionCells.count { (x, y) ->
                currentState.heatmap.getIntensity(x, y) < Heatmap.UNTOUCHED_THRESHOLD
            }
            val solutionPreservation = if (solutionCells.isNotEmpty()) {
                untouchedSolutionCells.toFloat() / solutionCells.size
            } else 1f
            
            SolutionCheckResult.Solved(
                score = currentState.complete(level).score,
                solutionPreservation = solutionPreservation
            )
        }
    }
    
    /**
     * Checks if the player can attempt to reveal the solution.
     */
    fun canRevealSolution(currentState: GameState, level: Level): Boolean {
        return currentState.heatmap.getCoveragePercentage() >= level.minCoverageRequired
    }
}

sealed class SolutionCheckResult {
    data class Solved(
        val score: Int,
        val solutionPreservation: Float
    ) : SolutionCheckResult()
    
    data class InsufficientCoverage(
        val currentCoverage: Float,
        val requiredCoverage: Float
    ) : SolutionCheckResult()
    
    object Failed : SolutionCheckResult()
}
