package com.negativespace.puzzler.domain.model

import org.junit.Assert.*
import org.junit.Test

class SolutionZoneTest {
    
    @Test
    fun `create rectangular solution zone`() {
        val zone = SolutionZone(
            centerX = 0.5f,
            centerY = 0.5f,
            width = 0.2f,
            height = 0.3f
        )
        
        assertEquals(0.5f, zone.centerX, 0.001f)
        assertEquals(0.5f, zone.centerY, 0.001f)
        assertEquals(0.2f, zone.width, 0.001f)
        assertEquals(0.3f, zone.height, 0.001f)
        assertEquals(ZoneShape.RECTANGLE, zone.shape)
    }
    
    @Test
    fun `create elliptical solution zone`() {
        val zone = SolutionZone(
            centerX = 0.5f,
            centerY = 0.5f,
            width = 0.2f,
            height = 0.2f,
            shape = ZoneShape.ELLIPSE
        )
        
        assertEquals(ZoneShape.ELLIPSE, zone.shape)
    }
    
    @Test
    fun `calculate bounds correctly`() {
        val zone = SolutionZone(
            centerX = 0.5f,
            centerY = 0.5f,
            width = 0.4f,
            height = 0.2f
        )
        
        assertEquals(0.3f, zone.left, 0.001f)
        assertEquals(0.7f, zone.right, 0.001f)
        assertEquals(0.4f, zone.top, 0.001f)
        assertEquals(0.6f, zone.bottom, 0.001f)
    }
    
    @Test
    fun `bounds are clamped at canvas edges`() {
        val zone = SolutionZone(
            centerX = 0.1f,
            centerY = 0.9f,
            width = 0.4f,
            height = 0.3f
        )
        
        assertEquals(0f, zone.left, 0.001f)
        assertTrue(zone.bottom <= 1f)
    }
    
    @Test
    fun `containsPoint returns true for point inside rectangle`() {
        val zone = SolutionZone(
            centerX = 0.5f,
            centerY = 0.5f,
            width = 0.2f,
            height = 0.2f
        )
        
        assertTrue(zone.containsPoint(0.5f, 0.5f))
        assertTrue(zone.containsPoint(0.45f, 0.55f))
    }
    
    @Test
    fun `containsPoint returns false for point outside rectangle`() {
        val zone = SolutionZone(
            centerX = 0.5f,
            centerY = 0.5f,
            width = 0.2f,
            height = 0.2f
        )
        
        assertFalse(zone.containsPoint(0.1f, 0.1f))
        assertFalse(zone.containsPoint(0.9f, 0.9f))
    }
    
    @Test
    fun `containsPoint returns true for point inside ellipse`() {
        val zone = SolutionZone(
            centerX = 0.5f,
            centerY = 0.5f,
            width = 0.4f,
            height = 0.4f,
            shape = ZoneShape.ELLIPSE
        )
        
        assertTrue(zone.containsPoint(0.5f, 0.5f))
    }
    
    @Test
    fun `containsPoint returns false for corner of ellipse bounding box`() {
        val zone = SolutionZone(
            centerX = 0.5f,
            centerY = 0.5f,
            width = 0.4f,
            height = 0.4f,
            shape = ZoneShape.ELLIPSE
        )
        
        // Corner of bounding box is outside ellipse
        assertFalse(zone.containsPoint(0.3f, 0.3f))
    }
    
    @Test
    fun `containsTouch delegates to containsPoint`() {
        val zone = SolutionZone(
            centerX = 0.5f,
            centerY = 0.5f,
            width = 0.2f,
            height = 0.2f
        )
        val touchInside = TouchPoint(x = 0.5f, y = 0.5f)
        val touchOutside = TouchPoint(x = 0.1f, y = 0.1f)
        
        assertTrue(zone.containsTouch(touchInside))
        assertFalse(zone.containsTouch(touchOutside))
    }
    
    @Test
    fun `getCoveredCells returns correct cells for rectangle`() {
        val zone = SolutionZone(
            centerX = 0.5f,
            centerY = 0.5f,
            width = 0.4f,
            height = 0.4f
        )
        
        val cells = zone.getCoveredCells(10, 10)
        
        assertTrue(cells.isNotEmpty())
        assertTrue(cells.contains(Pair(5, 5)))
    }
    
    @Test
    fun `getCoveredCells returns fewer cells for ellipse than rectangle`() {
        val rectangleZone = SolutionZone(
            centerX = 0.5f,
            centerY = 0.5f,
            width = 0.4f,
            height = 0.4f,
            shape = ZoneShape.RECTANGLE
        )
        val ellipseZone = SolutionZone(
            centerX = 0.5f,
            centerY = 0.5f,
            width = 0.4f,
            height = 0.4f,
            shape = ZoneShape.ELLIPSE
        )
        
        val rectCells = rectangleZone.getCoveredCells(20, 20)
        val ellipseCells = ellipseZone.getCoveredCells(20, 20)
        
        assertTrue(ellipseCells.size < rectCells.size)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception for centerX below 0`() {
        SolutionZone(centerX = -0.1f, centerY = 0.5f, width = 0.1f, height = 0.1f)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception for centerX above 1`() {
        SolutionZone(centerX = 1.1f, centerY = 0.5f, width = 0.1f, height = 0.1f)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception for zero width`() {
        SolutionZone(centerX = 0.5f, centerY = 0.5f, width = 0f, height = 0.1f)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception for width exceeding 1`() {
        SolutionZone(centerX = 0.5f, centerY = 0.5f, width = 1.5f, height = 0.1f)
    }
}
