package com.negativespace.puzzler.domain.usecase

import com.negativespace.puzzler.domain.model.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GenerateHintUseCaseTest {
    
    private lateinit var useCase: GenerateHintUseCase
    
    @Before
    fun setup() {
        useCase = GenerateHintUseCase()
    }
    
    @Test
    fun `returns static hint at hint level 0`() {
        val level = Level(
            id = 0,
            name = "Test",
            description = "Test",
            solutionZone = SolutionZone(0.5f, 0.5f, 0.2f, 0.2f),
            hints = listOf("First hint", "Second hint")
        )
        val heatmap = Heatmap.empty()
        
        val result = useCase(heatmap, level, hintLevel = 0)
        
        assertTrue(result is HintResult.TextHint)
        assertEquals("First hint", (result as HintResult.TextHint).text)
    }
    
    @Test
    fun `returns static hint at hint level 1`() {
        val level = Level(
            id = 0,
            name = "Test",
            description = "Test",
            solutionZone = SolutionZone(0.5f, 0.5f, 0.2f, 0.2f),
            hints = listOf("First hint", "Second hint")
        )
        val heatmap = Heatmap.empty()
        
        val result = useCase(heatmap, level, hintLevel = 1)
        
        assertTrue(result is HintResult.TextHint)
        assertEquals("Second hint", (result as HintResult.TextHint).text)
    }
    
    @Test
    fun `returns dynamic hint when static hints exhausted`() {
        val level = Level(
            id = 0,
            name = "Test",
            description = "Test",
            solutionZone = SolutionZone(0.5f, 0.5f, 0.2f, 0.2f),
            hints = listOf("First hint"),
            minCoverageRequired = 0.5f
        )
        val heatmap = Heatmap.empty()
        
        val result = useCase(heatmap, level, hintLevel = 1)
        
        assertTrue(result is HintResult.TextHint)
    }
    
    @Test
    fun `suggests exploration when coverage is low`() {
        val level = Level(
            id = 0,
            name = "Test",
            description = "Test",
            solutionZone = SolutionZone(0.5f, 0.5f, 0.2f, 0.2f),
            hints = emptyList()
        )
        val heatmap = Heatmap.empty()
        
        val result = useCase(heatmap, level, hintLevel = 0)
        
        assertTrue(result is HintResult.TextHint)
        assertTrue((result as HintResult.TextHint).text.contains("Explore"))
    }
    
    @Test
    fun `returns directional hint for top-left solution`() {
        val level = Level(
            id = 0,
            name = "Test",
            description = "Test",
            solutionZone = SolutionZone(0.1f, 0.1f, 0.1f, 0.1f),
            hints = emptyList(),
            minCoverageRequired = 0.1f
        )
        
        // Create heatmap with sufficient coverage
        var heatmap = Heatmap.empty(10, 10)
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                heatmap = heatmap.addTouch(TouchPoint(x = x * 0.2f + 0.3f, y = y * 0.2f + 0.3f))
            }
        }
        
        val result = useCase(heatmap, level, hintLevel = 0)
        
        assertTrue(result is HintResult.DirectionalHint)
        assertEquals(Direction.TOP_LEFT, (result as HintResult.DirectionalHint).direction)
    }
    
    @Test
    fun `returns directional hint for bottom-right solution`() {
        val level = Level(
            id = 0,
            name = "Test",
            description = "Test",
            solutionZone = SolutionZone(0.9f, 0.9f, 0.1f, 0.1f),
            hints = emptyList(),
            minCoverageRequired = 0.1f
        )
        
        var heatmap = Heatmap.empty(10, 10)
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                heatmap = heatmap.addTouch(TouchPoint(x = x * 0.15f, y = y * 0.15f))
            }
        }
        
        val result = useCase(heatmap, level, hintLevel = 0)
        
        assertTrue(result is HintResult.DirectionalHint)
        assertEquals(Direction.BOTTOM_RIGHT, (result as HintResult.DirectionalHint).direction)
    }
    
    @Test
    fun `returns directional hint for center solution`() {
        val level = Level(
            id = 0,
            name = "Test",
            description = "Test",
            solutionZone = SolutionZone(0.5f, 0.5f, 0.1f, 0.1f),
            hints = emptyList(),
            minCoverageRequired = 0.1f
        )
        
        var heatmap = Heatmap.empty(10, 10)
        for (x in 0 until 5) {
            for (y in 0 until 5) {
                heatmap = heatmap.addTouch(TouchPoint(x = x * 0.1f, y = y * 0.1f))
            }
        }
        
        val result = useCase(heatmap, level, hintLevel = 0)
        
        assertTrue(result is HintResult.DirectionalHint)
        assertEquals(Direction.CENTER, (result as HintResult.DirectionalHint).direction)
    }
    
    @Test
    fun `coverage hint includes percentage remaining`() {
        val level = Level(
            id = 0,
            name = "Test",
            description = "Test",
            solutionZone = SolutionZone(0.5f, 0.5f, 0.2f, 0.2f),
            hints = emptyList(),
            minCoverageRequired = 0.5f
        )
        
        // Some coverage but not enough
        var heatmap = Heatmap.empty(10, 10)
        for (x in 0 until 3) {
            heatmap = heatmap.addTouch(TouchPoint(x = x * 0.1f, y = 0.1f))
        }
        
        val result = useCase(heatmap, level, hintLevel = 0)
        
        assertTrue(result is HintResult.TextHint)
        assertTrue((result as HintResult.TextHint).text.contains("%"))
    }
}
