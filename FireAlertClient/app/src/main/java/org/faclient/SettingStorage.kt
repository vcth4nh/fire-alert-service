package org.faclient

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant


class SettingStorage(private val context: Context) {
    companion object {
        // TODO: singleton?
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        private val EMERGENCY = booleanPreferencesKey("emergency")
        private val LOCATION = stringPreferencesKey("location")
        private val TIME = longPreferencesKey("time")
    }

    val getEmergency: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[EMERGENCY] ?: false
    }

    private suspend fun setEmergency(emergency: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[EMERGENCY] = emergency
        }
    }

    suspend fun startEmergency() {
        setEmergency(true)
    }

    suspend fun stopEmergency() {
        setEmergency(false)
    }

    val getLocation: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LOCATION] ?: "No location available"
    }


    suspend fun setLocation(location: String) {
        context.dataStore.edit { preferences ->
            preferences[LOCATION] = location
        }
    }

    val getTime: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[TIME] ?: Instant.now().epochSecond
    }

    suspend fun setTime(time: Long) {
        context.dataStore.edit { preferences ->
            preferences[TIME] = time
        }
    }
}