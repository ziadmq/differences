package com.mobix.colortap.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "color_tap_prefs")

class ScoreDataStore(private val context: Context) {

    private val KEY_BEST_SCORE = intPreferencesKey("best_score")

    val bestScoreFlow: Flow<Int> =
        context.dataStore.data.map { prefs -> prefs[KEY_BEST_SCORE] ?: 0 }

    suspend fun saveBestScore(value: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_BEST_SCORE] = value
        }
    }
}