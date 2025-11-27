package com.app.examen.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val LAST_COUNTRY_KEY = stringPreferencesKey("last_country_searched")

    // Leer el dato (Devuelve un Flow para ser reactivo)
    val lastCountry: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[LAST_COUNTRY_KEY] ?: ""
        }

    // Guardar el dato
    suspend fun saveLastCountry(country: String) {
        dataStore.edit { preferences ->
            preferences[LAST_COUNTRY_KEY] = country
        }
    }
}