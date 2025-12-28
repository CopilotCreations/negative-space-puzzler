package com.negativespace.puzzler.domain.model

import org.junit.Assert.*
import org.junit.Test

class GameStateTest {
    
    @Test
    fun `create game state for level`() {
        val state = GameState.forLevel(5)
        
        assertEquals(5, state.currentLevelId)
        assertEquals(GameStatus.PLAYING, state.status)
        assertEquals(0, state.touchCount)
        assertFalse(state.solutionRevealed)
    }
    
    @Test
    fun `addTouch increases touch count`() {
        val state = GameState.forLevel(0)
        val point = TouchPoint(x = 0.5f, y = 0.5f)
        
        val updated = state.addTouch(point)
        
        assertEquals(1, updated.touchCount)
        assertEquals(1, updated.touchPoints.size)
    }
    
    @Test
    fun `addTouch updates heatmap`() {
        val state = GameState.forLevel(0)
        val point = TouchPoint(x = 0.5f, y = 0.5f)
        
        val updated = state.addTouch(point)
        
        assertTrue(updated.heatmap.getCoveragePercentage() > 0f)
    }
    
    @Test
    fun `multiple touches accumulate`() {
        var state = GameState.forLevel(0)
        
        repeat(5) { i ->
            state = state.addTouch(TouchPoint(x = i * 0.2f, y = 0.5f))
        }
        
        assertEquals(5, state.touchCount)
    }
    
    @Test
    fun `revealSolution sets flag`() {
        val state = GameState.forLevel(0)
        
        val revealed = state.revealSolution()
        
        assertTrue(revealed.solutionRevealed)
    }
    
    @Test
    fun `complete with untouched solution returns COMPLETED`() {
        val state = GameState.forLevel(0)
            .addTouch(TouchPoint(x = 0.1f, y = 0.1f))
        
        val level = Level.tutorial()
        val completed = state.complete(level)
        
        assertEquals(GameStatus.COMPLETED, completed.status)
        assertTrue(completed.score > 0)
    }
    
    @Test
    fun `complete with touched solution returns FAILED`() {
        val level = Level.tutorial() // Solution at center (0.5, 0.5)
        val state = GameState.forLevel(0)
            .addTouch(TouchPoint(x = 0.5f, y = 0.5f)) // Touch the solution
        
        val completed = state.complete(level)
        
        assertEquals(GameStatus.FAILED, completed.status)
        assertEquals(0, completed.score)
    }
    
    @Test
    fun `elapsedTime increases over time`() {
        val state = GameState(
            currentLevelId = 0,
            startTime = System.currentTimeMillis() - 5000
        )
        
        assertTrue(state.elapsedTime >= 5000)
    }
    
    @Test
    fun `coveragePercentage reflects heatmap`() {
        var state = GameState.forLevel(0)
        
        assertEquals(0f, state.coveragePercentage, 0.001f)
        
        state = state.addTouch(TouchPoint(x = 0.5f, y = 0.5f))
        
        assertTrue(state.coveragePercentage > 0f)
    }
    
    @Test
    fun `score calculation includes coverage bonus`() {
        val level = Level(
            id = 0,
            name = "Test",
            description = "Test level",
            solutionZone = SolutionZone(0.9f, 0.9f, 0.1f, 0.1f),
            difficulty = Difficulty.EASY
        )
        
        // Add many touches for high coverage
        var state = GameState.forLevel(0)
        for (x in 0 until 10) {
            for (y in 0 until 8) {
                state = state.addTouch(TouchPoint(x = x * 0.1f, y = y * 0.1f))
            }
        }
        
        val completed = state.complete(level)
        
        assertTrue(completed.score > 1000) // Base score + bonuses
    }
    
    @Test
    fun `difficulty multiplier affects score`() {
        val easyLevel = Level(
            id = 0,
            name = "Easy",
            description = "Easy",
            solutionZone = SolutionZone(0.9f, 0.9f, 0.05f, 0.05f),
            difficulty = Difficulty.EASY
        )
        val expertLevel = Level(
            id = 1,
            name = "Expert",
            description = "Expert",
            solutionZone = SolutionZone(0.9f, 0.9f, 0.05f, 0.05f),
            difficulty = Difficulty.EXPERT
        )
        
        val state = GameState.forLevel(0)
            .addTouch(TouchPoint(x = 0.1f, y = 0.1f))
        
        val easyScore = state.complete(easyLevel).score
        val expertScore = state.complete(expertLevel).score
        
        assertTrue(expertScore > easyScore)
    }
}
