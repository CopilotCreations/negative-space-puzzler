package com.negativespace.puzzler.domain.model

import kotlinx.serialization.Serializable

/**
 * User settings for accessibility and preferences.
 */
@Serializable
data class UserSettings(
    val reducedPrecisionMode: Boolean = false,
    val alternativeInputMode: Boolean = false,
    val highContrastMode: Boolean = false,
    val hapticFeedbackEnabled: Boolean = true,
    val showAds: Boolean = true,
    val soundEnabled: Boolean = true,
    val tutorialCompleted: Boolean = false,
    val currentLevel: Int = 0,
    val highScores: Map<Int, Int> = emptyMap(),
    val totalPlayTime: Long = 0L
) {
    fun withReducedPrecision(enabled: Boolean) = copy(reducedPrecisionMode = enabled)
    fun withAlternativeInput(enabled: Boolean) = copy(alternativeInputMode = enabled)
    fun withHighContrast(enabled: Boolean) = copy(highContrastMode = enabled)
    fun withHapticFeedback(enabled: Boolean) = copy(hapticFeedbackEnabled = enabled)
    fun withAds(enabled: Boolean) = copy(showAds = enabled)
    fun withSound(enabled: Boolean) = copy(soundEnabled = enabled)
    fun withTutorialCompleted() = copy(tutorialCompleted = true)
    fun withLevelProgress(levelId: Int) = copy(currentLevel = maxOf(currentLevel, levelId))
    
    fun withHighScore(levelId: Int, score: Int): UserSettings {
        val currentHighScore = highScores[levelId] ?: 0
        return if (score > currentHighScore) {
            copy(highScores = highScores + (levelId to score))
        } else this
    }
    
    fun addPlayTime(milliseconds: Long) = copy(totalPlayTime = totalPlayTime + milliseconds)
    
    /**
     * Gets the touch radius multiplier for accessibility.
     */
    val touchRadiusMultiplier: Float
        get() = if (reducedPrecisionMode) 2.0f else 1.0f
    
    /**
     * Gets the grid size based on accessibility settings.
     */
    val effectiveGridSize: Int
        get() = if (reducedPrecisionMode) 10 else Heatmap.DEFAULT_GRID_SIZE
}
