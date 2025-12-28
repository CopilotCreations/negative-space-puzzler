package com.negativespace.puzzler.domain.model

import org.junit.Assert.*
import org.junit.Test

class TouchPointTest {
    
    @Test
    fun `create touch point with valid coordinates`() {
        val point = TouchPoint(x = 0.5f, y = 0.5f)
        
        assertEquals(0.5f, point.x, 0.001f)
        assertEquals(0.5f, point.y, 0.001f)
    }
    
    @Test
    fun `create touch point at origin`() {
        val point = TouchPoint(x = 0f, y = 0f)
        
        assertEquals(0f, point.x, 0.001f)
        assertEquals(0f, point.y, 0.001f)
    }
    
    @Test
    fun `create touch point at max bounds`() {
        val point = TouchPoint(x = 1f, y = 1f)
        
        assertEquals(1f, point.x, 0.001f)
        assertEquals(1f, point.y, 0.001f)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception when x is negative`() {
        TouchPoint(x = -0.1f, y = 0.5f)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception when x exceeds 1`() {
        TouchPoint(x = 1.1f, y = 0.5f)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception when y is negative`() {
        TouchPoint(x = 0.5f, y = -0.1f)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception when y exceeds 1`() {
        TouchPoint(x = 0.5f, y = 1.1f)
    }
    
    @Test
    fun `fromRawCoordinates normalizes correctly`() {
        val point = TouchPoint.fromRawCoordinates(
            rawX = 500f,
            rawY = 250f,
            canvasWidth = 1000f,
            canvasHeight = 500f
        )
        
        assertEquals(0.5f, point.x, 0.001f)
        assertEquals(0.5f, point.y, 0.001f)
    }
    
    @Test
    fun `fromRawCoordinates clamps values at bounds`() {
        val point = TouchPoint.fromRawCoordinates(
            rawX = 1500f,
            rawY = -100f,
            canvasWidth = 1000f,
            canvasHeight = 500f
        )
        
        assertEquals(1f, point.x, 0.001f)
        assertEquals(0f, point.y, 0.001f)
    }
    
    @Test
    fun `touch point stores timestamp`() {
        val before = System.currentTimeMillis()
        val point = TouchPoint(x = 0.5f, y = 0.5f)
        val after = System.currentTimeMillis()
        
        assertTrue(point.timestamp in before..after)
    }
    
    @Test
    fun `touch point stores pressure`() {
        val point = TouchPoint(x = 0.5f, y = 0.5f, pressure = 0.8f)
        
        assertEquals(0.8f, point.pressure, 0.001f)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception when pressure is negative`() {
        TouchPoint(x = 0.5f, y = 0.5f, pressure = -0.1f)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception when pressure exceeds 1`() {
        TouchPoint(x = 0.5f, y = 0.5f, pressure = 1.5f)
    }
}
