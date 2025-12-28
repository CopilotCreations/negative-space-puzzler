package com.negativespace.puzzler.domain.usecase

import com.negativespace.puzzler.domain.model.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProcessTouchUseCaseTest {
    
    private lateinit var useCase: ProcessTouchUseCase
    private lateinit var testLevel: Level
    
    @Before
    fun setup() {
        useCase = ProcessTouchUseCase()
        testLevel = Level(
            id = 0,
            name = "Test Level",
            description = "Test",
            solutionZone = SolutionZone(
                centerX = 0.5f,
                centerY = 0.5f,
                width = 0.2f,
                height = 0.2f
            )
        )
    }
    
    @Test
    fun `touch outside solution zone returns Success`() {
        val state = GameState.forLevel(0)
        val touch = TouchPoint(x = 0.1f, y = 0.1f)
        
        val result = useCase(state, testLevel, touch)
        
        assertTrue(result is TouchResult.Success)
        assertEquals(1, result.state.touchCount)
    }
    
    @Test
    fun `touch inside solution zone returns SolutionTouched`() {
        val state = GameState.forLevel(0)
        val touch = TouchPoint(x = 0.5f, y = 0.5f)
        
        val result = useCase(state, testLevel, touch)
        
        assertTrue(result is TouchResult.SolutionTouched)
        assertEquals(GameStatus.FAILED, result.state.status)
    }
    
    @Test
    fun `touch on edge of solution zone returns SolutionTouched`() {
        val state = GameState.forLevel(0)
        // Touch at edge of solution zone (0.4 to 0.6)
        val touch = TouchPoint(x = 0.45f, y = 0.45f)
        
        val result = useCase(state, testLevel, touch)
        
        assertTrue(result is TouchResult.SolutionTouched)
    }
    
    @Test
    fun `touch just outside solution zone returns Success`() {
        val state = GameState.forLevel(0)
        // Touch just outside solution zone
        val touch = TouchPoint(x = 0.35f, y = 0.35f)
        
        val result = useCase(state, testLevel, touch)
        
        assertTrue(result is TouchResult.Success)
    }
    
    @Test
    fun `touch when game is COMPLETED returns Ignored`() {
        val state = GameState.forLevel(0).copy(status = GameStatus.COMPLETED)
        val touch = TouchPoint(x = 0.1f, y = 0.1f)
        
        val result = useCase(state, testLevel, touch)
        
        assertTrue(result is TouchResult.Ignored)
        assertEquals(0, result.state.touchCount) // Touch not added
    }
    
    @Test
    fun `touch when game is FAILED returns Ignored`() {
        val state = GameState.forLevel(0).copy(status = GameStatus.FAILED)
        val touch = TouchPoint(x = 0.1f, y = 0.1f)
        
        val result = useCase(state, testLevel, touch)
        
        assertTrue(result is TouchResult.Ignored)
    }
    
    @Test
    fun `touch when game is PAUSED returns Ignored`() {
        val state = GameState.forLevel(0).copy(status = GameStatus.PAUSED)
        val touch = TouchPoint(x = 0.1f, y = 0.1f)
        
        val result = useCase(state, testLevel, touch)
        
        assertTrue(result is TouchResult.Ignored)
    }
    
    @Test
    fun `multiple touches outside solution are all Success`() {
        var state = GameState.forLevel(0)
        
        for (i in 0 until 5) {
            val touch = TouchPoint(x = 0.1f + i * 0.05f, y = 0.1f)
            val result = useCase(state, testLevel, touch)
            
            assertTrue(result is TouchResult.Success)
            state = result.state
        }
        
        assertEquals(5, state.touchCount)
    }
    
    @Test
    fun `touch updates heatmap`() {
        val state = GameState.forLevel(0)
        val touch = TouchPoint(x = 0.1f, y = 0.1f)
        
        val result = useCase(state, testLevel, touch)
        
        assertTrue(result.state.heatmap.getCoveragePercentage() > 0f)
    }
    
    @Test
    fun `touch with ellipse solution zone`() {
        val ellipseLevel = Level(
            id = 0,
            name = "Ellipse Test",
            description = "Test",
            solutionZone = SolutionZone(
                centerX = 0.5f,
                centerY = 0.5f,
                width = 0.4f,
                height = 0.4f,
                shape = ZoneShape.ELLIPSE
            )
        )
        
        val state = GameState.forLevel(0)
        
        // Touch at corner of bounding box (outside ellipse)
        val cornerTouch = TouchPoint(x = 0.31f, y = 0.31f)
        val cornerResult = useCase(state, ellipseLevel, cornerTouch)
        assertTrue(cornerResult is TouchResult.Success)
        
        // Touch at center (inside ellipse)
        val centerTouch = TouchPoint(x = 0.5f, y = 0.5f)
        val centerResult = useCase(state, ellipseLevel, centerTouch)
        assertTrue(centerResult is TouchResult.SolutionTouched)
    }
}
