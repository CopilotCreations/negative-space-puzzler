package com.negativespace.puzzler.domain.model

import org.junit.Assert.*
import org.junit.Test

class UserSettingsTest {
    
    @Test
    fun `default settings have expected values`() {
        val settings = UserSettings()
        
        assertFalse(settings.reducedPrecisionMode)
        assertFalse(settings.alternativeInputMode)
        assertFalse(settings.highContrastMode)
        assertTrue(settings.hapticFeedbackEnabled)
        assertTrue(settings.showAds)
        assertTrue(settings.soundEnabled)
        assertFalse(settings.tutorialCompleted)
        assertEquals(0, settings.currentLevel)
        assertTrue(settings.highScores.isEmpty())
        assertEquals(0L, settings.totalPlayTime)
    }
    
    @Test
    fun `withReducedPrecision creates new settings`() {
        val original = UserSettings()
        val updated = original.withReducedPrecision(true)
        
        assertFalse(original.reducedPrecisionMode)
        assertTrue(updated.reducedPrecisionMode)
    }
    
    @Test
    fun `withAlternativeInput creates new settings`() {
        val original = UserSettings()
        val updated = original.withAlternativeInput(true)
        
        assertFalse(original.alternativeInputMode)
        assertTrue(updated.alternativeInputMode)
    }
    
    @Test
    fun `withHighContrast creates new settings`() {
        val original = UserSettings()
        val updated = original.withHighContrast(true)
        
        assertFalse(original.highContrastMode)
        assertTrue(updated.highContrastMode)
    }
    
    @Test
    fun `withHapticFeedback creates new settings`() {
        val original = UserSettings()
        val updated = original.withHapticFeedback(false)
        
        assertTrue(original.hapticFeedbackEnabled)
        assertFalse(updated.hapticFeedbackEnabled)
    }
    
    @Test
    fun `withAds creates new settings`() {
        val original = UserSettings()
        val updated = original.withAds(false)
        
        assertTrue(original.showAds)
        assertFalse(updated.showAds)
    }
    
    @Test
    fun `withTutorialCompleted creates new settings`() {
        val original = UserSettings()
        val updated = original.withTutorialCompleted()
        
        assertFalse(original.tutorialCompleted)
        assertTrue(updated.tutorialCompleted)
    }
    
    @Test
    fun `withLevelProgress updates to max level`() {
        val original = UserSettings(currentLevel = 3)
        
        val updated1 = original.withLevelProgress(5)
        assertEquals(5, updated1.currentLevel)
        
        val updated2 = updated1.withLevelProgress(2)
        assertEquals(5, updated2.currentLevel) // Shouldn't decrease
    }
    
    @Test
    fun `withHighScore updates only if higher`() {
        val original = UserSettings(highScores = mapOf(1 to 500))
        
        val updated1 = original.withHighScore(1, 800)
        assertEquals(800, updated1.highScores[1])
        
        val updated2 = updated1.withHighScore(1, 300)
        assertEquals(800, updated2.highScores[1]) // Shouldn't decrease
    }
    
    @Test
    fun `withHighScore adds new level score`() {
        val original = UserSettings(highScores = mapOf(1 to 500))
        
        val updated = original.withHighScore(2, 600)
        
        assertEquals(500, updated.highScores[1])
        assertEquals(600, updated.highScores[2])
    }
    
    @Test
    fun `addPlayTime accumulates`() {
        val original = UserSettings(totalPlayTime = 1000L)
        val updated = original.addPlayTime(500L)
        
        assertEquals(1500L, updated.totalPlayTime)
    }
    
    @Test
    fun `touchRadiusMultiplier is 1 by default`() {
        val settings = UserSettings()
        
        assertEquals(1.0f, settings.touchRadiusMultiplier, 0.001f)
    }
    
    @Test
    fun `touchRadiusMultiplier is 2 with reduced precision`() {
        val settings = UserSettings(reducedPrecisionMode = true)
        
        assertEquals(2.0f, settings.touchRadiusMultiplier, 0.001f)
    }
    
    @Test
    fun `effectiveGridSize is default by default`() {
        val settings = UserSettings()
        
        assertEquals(Heatmap.DEFAULT_GRID_SIZE, settings.effectiveGridSize)
    }
    
    @Test
    fun `effectiveGridSize is reduced with reduced precision`() {
        val settings = UserSettings(reducedPrecisionMode = true)
        
        assertEquals(10, settings.effectiveGridSize)
    }
}
