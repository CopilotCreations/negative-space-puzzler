package com.negativespace.puzzler.util

import org.junit.Assert.*
import org.junit.Test

class GameUtilsTest {
    
    @Test
    fun `formatTime formats seconds correctly`() {
        assertEquals("0:30", GameUtils.formatTime(30_000))
        assertEquals("1:00", GameUtils.formatTime(60_000))
        assertEquals("1:30", GameUtils.formatTime(90_000))
        assertEquals("5:45", GameUtils.formatTime(345_000))
    }
    
    @Test
    fun `formatTime handles zero`() {
        assertEquals("0:00", GameUtils.formatTime(0))
    }
    
    @Test
    fun `formatTime handles large values`() {
        assertEquals("60:00", GameUtils.formatTime(3600_000))
    }
    
    @Test
    fun `formatPercentage formats correctly`() {
        assertEquals("0%", GameUtils.formatPercentage(0f))
        assertEquals("50%", GameUtils.formatPercentage(0.5f))
        assertEquals("100%", GameUtils.formatPercentage(1f))
        assertEquals("75%", GameUtils.formatPercentage(0.75f))
    }
    
    @Test
    fun `distance calculates correctly`() {
        assertEquals(0f, GameUtils.distance(0f, 0f, 0f, 0f), 0.001f)
        assertEquals(1f, GameUtils.distance(0f, 0f, 1f, 0f), 0.001f)
        assertEquals(1f, GameUtils.distance(0f, 0f, 0f, 1f), 0.001f)
        assertEquals(1.414f, GameUtils.distance(0f, 0f, 1f, 1f), 0.01f)
    }
    
    @Test
    fun `clamp keeps value in range`() {
        assertEquals(0.5f, GameUtils.clamp(0.5f, 0f, 1f), 0.001f)
        assertEquals(0f, GameUtils.clamp(-0.5f, 0f, 1f), 0.001f)
        assertEquals(1f, GameUtils.clamp(1.5f, 0f, 1f), 0.001f)
    }
    
    @Test
    fun `lerp interpolates correctly`() {
        assertEquals(0f, GameUtils.lerp(0f, 1f, 0f), 0.001f)
        assertEquals(0.5f, GameUtils.lerp(0f, 1f, 0.5f), 0.001f)
        assertEquals(1f, GameUtils.lerp(0f, 1f, 1f), 0.001f)
        assertEquals(2.5f, GameUtils.lerp(0f, 5f, 0.5f), 0.001f)
    }
    
    @Test
    fun `map transforms range correctly`() {
        // Map 0-1 to 0-100
        assertEquals(50f, GameUtils.map(0.5f, 0f, 1f, 0f, 100f), 0.001f)
        
        // Map 0-10 to 0-1
        assertEquals(0.5f, GameUtils.map(5f, 0f, 10f, 0f, 1f), 0.001f)
        
        // Map with offset
        assertEquals(60f, GameUtils.map(0.5f, 0f, 1f, 10f, 110f), 0.001f)
    }
}
