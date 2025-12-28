package com.negativespace.puzzler.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.negativespace.puzzler.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

/**
 * Local data source for user settings using DataStore.
 * All data is stored locally on the device - nothing is transmitted.
 */
@Singleton
class SettingsDataStore @Inject constructor(
    private val context: Context
) {
    private val json = Json { 
        ignoreUnknownKeys = true 
        encodeDefaults = true
    }
    
    companion object {
        private val SETTINGS_KEY = stringPreferencesKey("user_settings")
        private val REDUCED_PRECISION_KEY = booleanPreferencesKey("reduced_precision")
        private val ALTERNATIVE_INPUT_KEY = booleanPreferencesKey("alternative_input")
        private val HIGH_CONTRAST_KEY = booleanPreferencesKey("high_contrast")
        private val HAPTIC_FEEDBACK_KEY = booleanPreferencesKey("haptic_feedback")
        private val SHOW_ADS_KEY = booleanPreferencesKey("show_ads")
        private val SOUND_ENABLED_KEY = booleanPreferencesKey("sound_enabled")
        private val TUTORIAL_COMPLETED_KEY = booleanPreferencesKey("tutorial_completed")
        private val CURRENT_LEVEL_KEY = intPreferencesKey("current_level")
        private val HIGH_SCORES_KEY = stringPreferencesKey("high_scores")
        private val TOTAL_PLAY_TIME_KEY = longPreferencesKey("total_play_time")
    }
    
    val settingsFlow: Flow<UserSettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserSettings(
                reducedPrecisionMode = preferences[REDUCED_PRECISION_KEY] ?: false,
                alternativeInputMode = preferences[ALTERNATIVE_INPUT_KEY] ?: false,
                highContrastMode = preferences[HIGH_CONTRAST_KEY] ?: false,
                hapticFeedbackEnabled = preferences[HAPTIC_FEEDBACK_KEY] ?: true,
                showAds = preferences[SHOW_ADS_KEY] ?: true,
                soundEnabled = preferences[SOUND_ENABLED_KEY] ?: true,
                tutorialCompleted = preferences[TUTORIAL_COMPLETED_KEY] ?: false,
                currentLevel = preferences[CURRENT_LEVEL_KEY] ?: 0,
                highScores = parseHighScores(preferences[HIGH_SCORES_KEY]),
                totalPlayTime = preferences[TOTAL_PLAY_TIME_KEY] ?: 0L
            )
        }
    
    suspend fun updateSettings(settings: UserSettings) {
        context.dataStore.edit { preferences ->
            preferences[REDUCED_PRECISION_KEY] = settings.reducedPrecisionMode
            preferences[ALTERNATIVE_INPUT_KEY] = settings.alternativeInputMode
            preferences[HIGH_CONTRAST_KEY] = settings.highContrastMode
            preferences[HAPTIC_FEEDBACK_KEY] = settings.hapticFeedbackEnabled
            preferences[SHOW_ADS_KEY] = settings.showAds
            preferences[SOUND_ENABLED_KEY] = settings.soundEnabled
            preferences[TUTORIAL_COMPLETED_KEY] = settings.tutorialCompleted
            preferences[CURRENT_LEVEL_KEY] = settings.currentLevel
            preferences[HIGH_SCORES_KEY] = json.encodeToString(settings.highScores)
            preferences[TOTAL_PLAY_TIME_KEY] = settings.totalPlayTime
        }
    }
    
    suspend fun updateReducedPrecision(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[REDUCED_PRECISION_KEY] = enabled
        }
    }
    
    suspend fun updateAlternativeInput(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ALTERNATIVE_INPUT_KEY] = enabled
        }
    }
    
    suspend fun updateHighContrast(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[HIGH_CONTRAST_KEY] = enabled
        }
    }
    
    suspend fun updateHapticFeedback(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[HAPTIC_FEEDBACK_KEY] = enabled
        }
    }
    
    suspend fun updateShowAds(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_ADS_KEY] = enabled
        }
    }
    
    suspend fun markTutorialCompleted() {
        context.dataStore.edit { preferences ->
            preferences[TUTORIAL_COMPLETED_KEY] = true
        }
    }
    
    suspend fun updateLevelProgress(levelId: Int) {
        context.dataStore.edit { preferences ->
            val current = preferences[CURRENT_LEVEL_KEY] ?: 0
            preferences[CURRENT_LEVEL_KEY] = maxOf(current, levelId)
        }
    }
    
    suspend fun saveHighScore(levelId: Int, score: Int) {
        context.dataStore.edit { preferences ->
            val currentScores = parseHighScores(preferences[HIGH_SCORES_KEY])
            val existingScore = currentScores[levelId] ?: 0
            if (score > existingScore) {
                val newScores = currentScores + (levelId to score)
                preferences[HIGH_SCORES_KEY] = json.encodeToString(newScores)
            }
        }
    }
    
    suspend fun addPlayTime(milliseconds: Long) {
        context.dataStore.edit { preferences ->
            val current = preferences[TOTAL_PLAY_TIME_KEY] ?: 0L
            preferences[TOTAL_PLAY_TIME_KEY] = current + milliseconds
        }
    }
    
    private fun parseHighScores(jsonString: String?): Map<Int, Int> {
        if (jsonString.isNullOrEmpty()) return emptyMap()
        return try {
            json.decodeFromString<Map<Int, Int>>(jsonString)
        } catch (e: Exception) {
            emptyMap()
        }
    }
}
