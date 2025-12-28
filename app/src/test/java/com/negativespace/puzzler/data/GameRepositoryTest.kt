package com.negativespace.puzzler.data

import com.negativespace.puzzler.data.repository.GameRepository
import com.negativespace.puzzler.domain.model.Level
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameRepositoryTest {
    
    @Test
    fun `getLevels returns non-empty list`() {
        val dataStore = mockk<com.negativespace.puzzler.data.local.GameStateDataStore>(relaxed = true)
        val repository = GameRepository(dataStore)
        
        val levels = repository.getLevels()
        
        assertTrue(levels.isNotEmpty())
    }
    
    @Test
    fun `getLevel returns correct level`() {
        val dataStore = mockk<com.negativespace.puzzler.data.local.GameStateDataStore>(relaxed = true)
        val repository = GameRepository(dataStore)
        
        val level = repository.getLevel(0)
        
        assertNotNull(level)
        assertEquals(0, level?.id)
    }
    
    @Test
    fun `getLevel returns null for invalid id`() {
        val dataStore = mockk<com.negativespace.puzzler.data.local.GameStateDataStore>(relaxed = true)
        val repository = GameRepository(dataStore)
        
        val level = repository.getLevel(999)
        
        assertNull(level)
    }
    
    @Test
    fun `getNextLevel returns next level`() {
        val dataStore = mockk<com.negativespace.puzzler.data.local.GameStateDataStore>(relaxed = true)
        val repository = GameRepository(dataStore)
        
        val nextLevel = repository.getNextLevel(0)
        
        assertNotNull(nextLevel)
        assertEquals(1, nextLevel?.id)
    }
    
    @Test
    fun `getNextLevel returns null for last level`() {
        val dataStore = mockk<com.negativespace.puzzler.data.local.GameStateDataStore>(relaxed = true)
        val repository = GameRepository(dataStore)
        
        val levels = repository.getLevels()
        val lastLevel = levels.last()
        val nextLevel = repository.getNextLevel(lastLevel.id)
        
        assertNull(nextLevel)
    }
    
    @Test
    fun `getTotalLevelCount returns correct count`() {
        val dataStore = mockk<com.negativespace.puzzler.data.local.GameStateDataStore>(relaxed = true)
        val repository = GameRepository(dataStore)
        
        val count = repository.getTotalLevelCount()
        
        assertTrue(count > 0)
        assertEquals(repository.getLevels().size, count)
    }
}
