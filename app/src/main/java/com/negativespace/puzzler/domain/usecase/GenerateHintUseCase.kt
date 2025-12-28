package com.negativespace.puzzler.domain.usecase

import com.negativespace.puzzler.domain.model.Heatmap
import com.negativespace.puzzler.domain.model.Level
import com.negativespace.puzzler.domain.model.SolutionZone
import javax.inject.Inject

/**
 * Use case for generating hints based on the negative space.
 */
class GenerateHintUseCase @Inject constructor() {
    
    /**
     * Generates a hint for the player based on their current progress.
     */
    operator fun invoke(
        heatmap: Heatmap,
        level: Level,
        hintLevel: Int = 0
    ): HintResult {
        val solutionZone = level.solutionZone
        
        // Get available hints from level
        val staticHints = level.hints
        if (hintLevel < staticHints.size) {
            return HintResult.TextHint(staticHints[hintLevel])
        }
        
        // Generate dynamic hints based on gameplay
        val coverage = heatmap.getCoveragePercentage()
        
        return when {
            coverage < 0.2f -> {
                HintResult.TextHint("Explore more of the canvas")
            }
            coverage < level.minCoverageRequired -> {
                val remaining = ((level.minCoverageRequired - coverage) * 100).toInt()
                HintResult.TextHint("Cover $remaining% more to reveal the solution")
            }
            else -> {
                // Provide directional hint
                val direction = getDirectionalHint(solutionZone)
                HintResult.DirectionalHint(direction)
            }
        }
    }
    
    private fun getDirectionalHint(zone: SolutionZone): Direction {
        val centerX = zone.centerX
        val centerY = zone.centerY
        
        return when {
            centerY < 0.33f && centerX < 0.33f -> Direction.TOP_LEFT
            centerY < 0.33f && centerX > 0.66f -> Direction.TOP_RIGHT
            centerY < 0.33f -> Direction.TOP
            centerY > 0.66f && centerX < 0.33f -> Direction.BOTTOM_LEFT
            centerY > 0.66f && centerX > 0.66f -> Direction.BOTTOM_RIGHT
            centerY > 0.66f -> Direction.BOTTOM
            centerX < 0.33f -> Direction.LEFT
            centerX > 0.66f -> Direction.RIGHT
            else -> Direction.CENTER
        }
    }
}

sealed class HintResult {
    data class TextHint(val text: String) : HintResult()
    data class DirectionalHint(val direction: Direction) : HintResult()
    data class AreaHighlight(
        val x: Float,
        val y: Float,
        val radius: Float
    ) : HintResult()
}

enum class Direction {
    TOP_LEFT,
    TOP,
    TOP_RIGHT,
    LEFT,
    CENTER,
    RIGHT,
    BOTTOM_LEFT,
    BOTTOM,
    BOTTOM_RIGHT
}
