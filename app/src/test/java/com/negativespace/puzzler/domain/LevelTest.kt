package com.negativespace.puzzler.domain.model

import org.junit.Assert.*
import org.junit.Test

class LevelTest {
    
    @Test
    fun `tutorial level is created correctly`() {
        val tutorial = Level.tutorial()
        
        assertEquals(0, tutorial.id)
        assertEquals("Tutorial", tutorial.name)
        assertEquals(Difficulty.EASY, tutorial.difficulty)
        assertNotNull(tutorial.solutionZone)
        assertTrue(tutorial.hints.isNotEmpty())
    }
    
    @Test
    fun `generateLevels returns non-empty list`() {
        val levels = Level.generateLevels()
        
        assertTrue(levels.isNotEmpty())
    }
    
    @Test
    fun `generateLevels includes tutorial`() {
        val levels = Level.generateLevels()
        
        assertTrue(levels.any { it.id == 0 && it.name == "Tutorial" })
    }
    
    @Test
    fun `levels have unique ids`() {
        val levels = Level.generateLevels()
        val ids = levels.map { it.id }
        
        assertEquals(ids.size, ids.toSet().size)
    }
    
    @Test
    fun `levels have increasing difficulty progression`() {
        val levels = Level.generateLevels()
        val difficulties = levels.map { it.difficulty }
        
        // First level should be easy
        assertEquals(Difficulty.EASY, difficulties.first())
        
        // Should have expert levels
        assertTrue(difficulties.contains(Difficulty.EXPERT))
    }
    
    @Test
    fun `all levels have valid solution zones`() {
        val levels = Level.generateLevels()
        
        levels.forEach { level ->
            val zone = level.solutionZone
            assertTrue(zone.centerX in 0f..1f)
            assertTrue(zone.centerY in 0f..1f)
            assertTrue(zone.width > 0f && zone.width <= 1f)
            assertTrue(zone.height > 0f && zone.height <= 1f)
        }
    }
    
    @Test
    fun `all levels have reasonable minCoverageRequired`() {
        val levels = Level.generateLevels()
        
        levels.forEach { level ->
            assertTrue(level.minCoverageRequired in 0.1f..0.9f)
        }
    }
    
    @Test
    fun `some levels have time limits`() {
        val levels = Level.generateLevels()
        
        assertTrue(levels.any { it.timeLimit != null })
    }
    
    @Test
    fun `level with time limit has reasonable duration`() {
        val levels = Level.generateLevels()
        val timedLevels = levels.filter { it.timeLimit != null }
        
        timedLevels.forEach { level ->
            assertTrue(level.timeLimit!! >= 30000L) // At least 30 seconds
        }
    }
}
