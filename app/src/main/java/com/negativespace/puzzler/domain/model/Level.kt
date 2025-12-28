package com.negativespace.puzzler.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a single puzzle level.
 */
@Serializable
data class Level(
    val id: Int,
    val name: String,
    val description: String,
    val solutionZone: SolutionZone,
    val difficulty: Difficulty = Difficulty.NORMAL,
    val timeLimit: Long? = null, // in milliseconds, null = no limit
    val minCoverageRequired: Float = 0.4f, // minimum canvas coverage before solution can be revealed
    val hints: List<String> = emptyList()
) {
    companion object {
        fun tutorial(): Level = Level(
            id = 0,
            name = "Tutorial",
            description = "Learn the basics - touch everywhere EXCEPT the hidden zone",
            solutionZone = SolutionZone(
                centerX = 0.5f,
                centerY = 0.5f,
                width = 0.2f,
                height = 0.2f
            ),
            difficulty = Difficulty.EASY,
            minCoverageRequired = 0.3f,
            hints = listOf("The solution is in the center", "Avoid the middle!")
        )
        
        fun generateLevels(): List<Level> = listOf(
            tutorial(),
            Level(
                id = 1,
                name = "Corner Secret",
                description = "Something hides in a corner",
                solutionZone = SolutionZone(0.1f, 0.1f, 0.15f, 0.15f),
                difficulty = Difficulty.EASY,
                minCoverageRequired = 0.35f,
                hints = listOf("Check the corners")
            ),
            Level(
                id = 2,
                name = "Edge Walker",
                description = "The answer lies at the edge",
                solutionZone = SolutionZone(0.5f, 0.95f, 0.3f, 0.08f),
                difficulty = Difficulty.EASY,
                minCoverageRequired = 0.4f,
                hints = listOf("Look at the boundaries")
            ),
            Level(
                id = 3,
                name = "Circle of Mystery",
                description = "A round secret awaits",
                solutionZone = SolutionZone(0.7f, 0.3f, 0.2f, 0.2f, ZoneShape.ELLIPSE),
                difficulty = Difficulty.NORMAL,
                minCoverageRequired = 0.45f,
                hints = listOf("Not everything is square")
            ),
            Level(
                id = 4,
                name = "The Thin Line",
                description = "A narrow path to victory",
                solutionZone = SolutionZone(0.5f, 0.5f, 0.05f, 0.6f),
                difficulty = Difficulty.NORMAL,
                minCoverageRequired = 0.5f,
                hints = listOf("Think vertical")
            ),
            Level(
                id = 5,
                name = "Bottom Dweller",
                description = "Look down for the answer",
                solutionZone = SolutionZone(0.3f, 0.85f, 0.25f, 0.12f),
                difficulty = Difficulty.NORMAL,
                minCoverageRequired = 0.5f,
                hints = listOf("Gravity pulls secrets down")
            ),
            Level(
                id = 6,
                name = "Tiny Target",
                description = "A small secret in a big space",
                solutionZone = SolutionZone(0.25f, 0.6f, 0.1f, 0.1f),
                difficulty = Difficulty.HARD,
                minCoverageRequired = 0.55f,
                hints = listOf("It's smaller than you think")
            ),
            Level(
                id = 7,
                name = "Top Secret",
                description = "Classified at the highest level",
                solutionZone = SolutionZone(0.8f, 0.15f, 0.15f, 0.15f, ZoneShape.ELLIPSE),
                difficulty = Difficulty.HARD,
                minCoverageRequired = 0.55f,
                hints = listOf("Rise to the occasion")
            ),
            Level(
                id = 8,
                name = "The Wide Path",
                description = "A broad secret hides in plain sight",
                solutionZone = SolutionZone(0.5f, 0.5f, 0.6f, 0.08f),
                difficulty = Difficulty.HARD,
                minCoverageRequired = 0.55f,
                hints = listOf("Think horizontal")
            ),
            Level(
                id = 9,
                name = "Corner Master",
                description = "Master the corners to win",
                solutionZone = SolutionZone(0.9f, 0.9f, 0.12f, 0.12f),
                difficulty = Difficulty.EXPERT,
                minCoverageRequired = 0.6f,
                hints = listOf("The last corner is the key")
            ),
            Level(
                id = 10,
                name = "Final Challenge",
                description = "The ultimate test of negative space",
                solutionZone = SolutionZone(0.42f, 0.73f, 0.08f, 0.08f, ZoneShape.ELLIPSE),
                difficulty = Difficulty.EXPERT,
                timeLimit = 60000L,
                minCoverageRequired = 0.65f,
                hints = listOf("Trust your instincts", "Time is of the essence")
            )
        )
    }
}

@Serializable
enum class Difficulty {
    EASY,
    NORMAL,
    HARD,
    EXPERT
}
