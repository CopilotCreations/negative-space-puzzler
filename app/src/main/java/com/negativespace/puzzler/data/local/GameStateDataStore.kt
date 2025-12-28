package com.negativespace.puzzler.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.negativespace.puzzler.domain.model.GameState
import com.negativespace.puzzler.domain.model.Heatmap
import com.negativespace.puzzler.domain.model.TouchPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.gameDataStore: DataStore<Preferences> by preferencesDataStore(name = "game_state")

/**
 * Local data source for game state persistence.
 * All game data stays on device - nothing is transmitted.
 */
@Singleton
class GameStateDataStore @Inject constructor(
    private val context: Context
) {
    private val json = Json { 
        ignoreUnknownKeys = true 
        encodeDefaults = true
    }
    
    companion object {
        private val CURRENT_LEVEL_ID_KEY = intPreferencesKey("current_level_id")
        private val HEATMAP_KEY = stringPreferencesKey("heatmap")
        private val TOUCH_POINTS_KEY = stringPreferencesKey("touch_points")
        private val START_TIME_KEY = longPreferencesKey("start_time")
        private val GAME_STATUS_KEY = stringPreferencesKey("game_status")
        private val SOLUTION_REVEALED_KEY = booleanPreferencesKey("solution_revealed")
        private val SCORE_KEY = intPreferencesKey("score")
    }
    
    val gameStateFlow: Flow<GameState?> = context.gameDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val levelId = preferences[CURRENT_LEVEL_ID_KEY]
            if (levelId == null) {
                null
            } else {
                try {
                    GameState(
                        currentLevelId = levelId,
                        heatmap = parseHeatmap(preferences[HEATMAP_KEY]),
                        touchPoints = parseTouchPoints(preferences[TOUCH_POINTS_KEY]),
                        startTime = preferences[START_TIME_KEY] ?: System.currentTimeMillis(),
                        status = parseGameStatus(preferences[GAME_STATUS_KEY]),
                        solutionRevealed = preferences[SOLUTION_REVEALED_KEY] ?: false,
                        score = preferences[SCORE_KEY] ?: 0
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }
    
    suspend fun saveGameState(state: GameState) {
        context.gameDataStore.edit { preferences ->
            preferences[CURRENT_LEVEL_ID_KEY] = state.currentLevelId
            preferences[HEATMAP_KEY] = json.encodeToString(state.heatmap)
            preferences[TOUCH_POINTS_KEY] = json.encodeToString(state.touchPoints)
            preferences[START_TIME_KEY] = state.startTime
            preferences[GAME_STATUS_KEY] = state.status.name
            preferences[SOLUTION_REVEALED_KEY] = state.solutionRevealed
            preferences[SCORE_KEY] = state.score
        }
    }
    
    suspend fun clearGameState() {
        context.gameDataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    private fun parseHeatmap(jsonString: String?): Heatmap {
        if (jsonString.isNullOrEmpty()) return Heatmap.empty()
        return try {
            json.decodeFromString<Heatmap>(jsonString)
        } catch (e: Exception) {
            Heatmap.empty()
        }
    }
    
    private fun parseTouchPoints(jsonString: String?): List<TouchPoint> {
        if (jsonString.isNullOrEmpty()) return emptyList()
        return try {
            json.decodeFromString<List<TouchPoint>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    private fun parseGameStatus(statusString: String?): com.negativespace.puzzler.domain.model.GameStatus {
        if (statusString.isNullOrEmpty()) return com.negativespace.puzzler.domain.model.GameStatus.PLAYING
        return try {
            com.negativespace.puzzler.domain.model.GameStatus.valueOf(statusString)
        } catch (e: Exception) {
            com.negativespace.puzzler.domain.model.GameStatus.PLAYING
        }
    }
}
