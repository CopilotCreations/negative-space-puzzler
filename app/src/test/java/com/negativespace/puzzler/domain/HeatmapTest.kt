package com.negativespace.puzzler.domain.model

import org.junit.Assert.*
import org.junit.Test

class HeatmapTest {
    
    @Test
    fun `create empty heatmap with default size`() {
        val heatmap = Heatmap.empty()
        
        assertEquals(Heatmap.DEFAULT_GRID_SIZE, heatmap.gridWidth)
        assertEquals(Heatmap.DEFAULT_GRID_SIZE, heatmap.gridHeight)
        assertTrue(heatmap.cells.all { it == 0f })
    }
    
    @Test
    fun `create empty heatmap with custom size`() {
        val heatmap = Heatmap.empty(width = 10, height = 15)
        
        assertEquals(10, heatmap.gridWidth)
        assertEquals(15, heatmap.gridHeight)
        assertEquals(150, heatmap.cells.size)
    }
    
    @Test
    fun `getIntensity returns 0 for empty heatmap`() {
        val heatmap = Heatmap.empty()
        
        assertEquals(0f, heatmap.getIntensity(5, 5), 0.001f)
    }
    
    @Test
    fun `getIntensity returns 0 for out of bounds`() {
        val heatmap = Heatmap.empty(10, 10)
        
        assertEquals(0f, heatmap.getIntensity(-1, 5), 0.001f)
        assertEquals(0f, heatmap.getIntensity(5, -1), 0.001f)
        assertEquals(0f, heatmap.getIntensity(10, 5), 0.001f)
        assertEquals(0f, heatmap.getIntensity(5, 10), 0.001f)
    }
    
    @Test
    fun `addTouch increases intensity at touch location`() {
        val heatmap = Heatmap.empty(10, 10)
        val point = TouchPoint(x = 0.5f, y = 0.5f)
        
        val updated = heatmap.addTouch(point)
        
        assertTrue(updated.getIntensity(5, 5) > 0f)
    }
    
    @Test
    fun `addTouch creates radial gradient`() {
        val heatmap = Heatmap.empty(10, 10)
        val point = TouchPoint(x = 0.5f, y = 0.5f)
        
        val updated = heatmap.addTouch(point, radius = 2)
        
        // Center should have highest intensity
        val centerIntensity = updated.getIntensity(5, 5)
        val adjacentIntensity = updated.getIntensity(4, 5)
        
        assertTrue(centerIntensity >= adjacentIntensity)
    }
    
    @Test
    fun `multiple touches accumulate intensity`() {
        val heatmap = Heatmap.empty(10, 10)
        val point = TouchPoint(x = 0.5f, y = 0.5f)
        
        val updated1 = heatmap.addTouch(point)
        val updated2 = updated1.addTouch(point)
        
        assertTrue(updated2.getIntensity(5, 5) > updated1.getIntensity(5, 5))
    }
    
    @Test
    fun `intensity is capped at maxIntensity`() {
        val heatmap = Heatmap.empty(10, 10)
        val point = TouchPoint(x = 0.5f, y = 0.5f)
        
        var updated = heatmap
        repeat(100) {
            updated = updated.addTouch(point)
        }
        
        assertTrue(updated.getIntensity(5, 5) <= 1f)
    }
    
    @Test
    fun `getUntouchedCells returns all cells for empty heatmap`() {
        val heatmap = Heatmap.empty(5, 5)
        
        val untouched = heatmap.getUntouchedCells()
        
        assertEquals(25, untouched.size)
    }
    
    @Test
    fun `getUntouchedCells excludes touched areas`() {
        val heatmap = Heatmap.empty(10, 10)
        val point = TouchPoint(x = 0.5f, y = 0.5f)
        
        val updated = heatmap.addTouch(point)
        val untouched = updated.getUntouchedCells()
        
        assertTrue(untouched.size < 100)
    }
    
    @Test
    fun `getCoveragePercentage returns 0 for empty heatmap`() {
        val heatmap = Heatmap.empty()
        
        assertEquals(0f, heatmap.getCoveragePercentage(), 0.001f)
    }
    
    @Test
    fun `getCoveragePercentage increases with touches`() {
        val heatmap = Heatmap.empty(10, 10)
        val point = TouchPoint(x = 0.5f, y = 0.5f)
        
        val updated = heatmap.addTouch(point)
        
        assertTrue(updated.getCoveragePercentage() > 0f)
    }
    
    @Test
    fun `findLargestUntouchedRegion returns all cells for empty heatmap`() {
        val heatmap = Heatmap.empty(5, 5)
        
        val region = heatmap.findLargestUntouchedRegion()
        
        assertEquals(25, region.size)
    }
    
    @Test
    fun `findLargestUntouchedRegion finds contiguous region`() {
        val cells = MutableList(25) { 0f }
        // Create a vertical divider in the middle
        for (y in 0 until 5) {
            cells[y * 5 + 2] = 1f
        }
        val heatmap = Heatmap(gridWidth = 5, gridHeight = 5, cells = cells)
        
        val region = heatmap.findLargestUntouchedRegion()
        
        // Should find one of the two sides (both have 10 cells)
        assertEquals(10, region.size)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception for invalid grid width`() {
        Heatmap(gridWidth = 0, gridHeight = 5, cells = emptyList())
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception for invalid grid height`() {
        Heatmap(gridWidth = 5, gridHeight = 0, cells = emptyList())
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `throw exception for mismatched cells size`() {
        Heatmap(gridWidth = 5, gridHeight = 5, cells = List(10) { 0f })
    }
}
