package com.negativespace.puzzler.domain.usecase

import com.negativespace.puzzler.domain.model.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CheckSolutionUseCaseTest {
    
    private lateinit var useCase: CheckSolutionUseCase
    private lateinit var testLevel: Level
    
    @Before
    fun setup() {
        useCase = CheckSolutionUseCase()
        testLevel = Level(
            id = 0,
            name = "Test Level",
            description = "Test",
            solutionZone = SolutionZone(
                centerX = 0.9f,
                centerY = 0.9f,
                width = 0.15f,
                height = 0.15f
            ),
            minCoverageRequired = 0.3f
        )
    }
    
    @Test
    fun `returns InsufficientCoverage when coverage is too low`() {
        val state = GameState.forLevel(0)
            .addTouch(TouchPoint(x = 0.1f, y = 0.1f))
        
        val result = useCase(state, testLevel)
        
        assertTrue(result is SolutionCheckResult.InsufficientCoverage)
        val insufficientResult = result as SolutionCheckResult.InsufficientCoverage
        assertEquals(0.3f, insufficientResult.requiredCoverage, 0.001f)
    }
    
    @Test
    fun `returns Solved when coverage is sufficient and solution untouched`() {
        var state = GameState.forLevel(0)
        
        // Add many touches to cover the canvas (avoiding solution zone at 0.9, 0.9)
        for (x in 0 until 8) {
            for (y in 0 until 8) {
                state = state.addTouch(TouchPoint(x = x * 0.1f, y = y * 0.1f))
            }
        }
        
        val result = useCase(state, testLevel)
        
        assertTrue(result is SolutionCheckResult.Solved)
        val solvedResult = result as SolutionCheckResult.Solved
        assertTrue(solvedResult.score > 0)
    }
    
    @Test
    fun `returns Failed when solution zone was touched`() {
        var state = GameState.forLevel(0)
        
        // Add touches including the solution zone
        for (x in 0 until 10) {
            for (y in 0 until 10) {
                state = state.addTouch(TouchPoint(x = x * 0.1f, y = y * 0.1f))
            }
        }
        
        val result = useCase(state, testLevel)
        
        assertTrue(result is SolutionCheckResult.Failed)
    }
    
    @Test
    fun `canRevealSolution returns false when coverage insufficient`() {
        val state = GameState.forLevel(0)
        
        assertFalse(useCase.canRevealSolution(state, testLevel))
    }
    
    @Test
    fun `canRevealSolution returns true when coverage sufficient`() {
        var state = GameState.forLevel(0)
        
        // Add touches to exceed coverage requirement
        for (x in 0 until 6) {
            for (y in 0 until 6) {
                state = state.addTouch(TouchPoint(x = x * 0.15f, y = y * 0.15f))
            }
        }
        
        assertTrue(useCase.canRevealSolution(state, testLevel))
    }
    
    @Test
    fun `solved result includes solution preservation metric`() {
        var state = GameState.forLevel(0)
        
        // Touch everywhere except solution zone
        for (x in 0 until 8) {
            for (y in 0 until 8) {
                state = state.addTouch(TouchPoint(x = x * 0.1f, y = y * 0.1f))
            }
        }
        
        val result = useCase(state, testLevel) as SolutionCheckResult.Solved
        
        assertTrue(result.solutionPreservation > 0f)
    }
    
    @Test
    fun `higher coverage gives higher score`() {
        var lowCoverageState = GameState.forLevel(0)
        var highCoverageState = GameState.forLevel(0)
        
        val cornerLevel = Level(
            id = 0,
            name = "Corner",
            description = "Test",
            solutionZone = SolutionZone(0.95f, 0.95f, 0.08f, 0.08f),
            minCoverageRequired = 0.1f
        )
        
        // Low coverage
        for (x in 0 until 3) {
            for (y in 0 until 3) {
                lowCoverageState = lowCoverageState.addTouch(TouchPoint(x = x * 0.1f, y = y * 0.1f))
            }
        }
        
        // High coverage
        for (x in 0 until 8) {
            for (y in 0 until 8) {
                highCoverageState = highCoverageState.addTouch(TouchPoint(x = x * 0.1f, y = y * 0.1f))
            }
        }
        
        val lowResult = useCase(lowCoverageState, cornerLevel) as SolutionCheckResult.Solved
        val highResult = useCase(highCoverageState, cornerLevel) as SolutionCheckResult.Solved
        
        assertTrue(highResult.score >= lowResult.score)
    }
}
