package com.negativespace.puzzler.data.repository

import com.negativespace.puzzler.data.local.SettingsDataStore
import com.negativespace.puzzler.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for user settings.
 * Acts as a single source of truth for settings data.
 */
@Singleton
class SettingsRepository @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) {
    val settings: Flow<UserSettings> = settingsDataStore.settingsFlow
    
    suspend fun updateSettings(settings: UserSettings) {
        settingsDataStore.updateSettings(settings)
    }
    
    suspend fun setReducedPrecision(enabled: Boolean) {
        settingsDataStore.updateReducedPrecision(enabled)
    }
    
    suspend fun setAlternativeInput(enabled: Boolean) {
        settingsDataStore.updateAlternativeInput(enabled)
    }
    
    suspend fun setHighContrast(enabled: Boolean) {
        settingsDataStore.updateHighContrast(enabled)
    }
    
    suspend fun setHapticFeedback(enabled: Boolean) {
        settingsDataStore.updateHapticFeedback(enabled)
    }
    
    suspend fun setShowAds(enabled: Boolean) {
        settingsDataStore.updateShowAds(enabled)
    }
    
    suspend fun markTutorialCompleted() {
        settingsDataStore.markTutorialCompleted()
    }
    
    suspend fun updateLevelProgress(levelId: Int) {
        settingsDataStore.updateLevelProgress(levelId)
    }
    
    suspend fun saveHighScore(levelId: Int, score: Int) {
        settingsDataStore.saveHighScore(levelId, score)
    }
    
    suspend fun addPlayTime(milliseconds: Long) {
        settingsDataStore.addPlayTime(milliseconds)
    }
}
