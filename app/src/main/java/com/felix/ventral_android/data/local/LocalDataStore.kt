package com.felix.ventral_android.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit
import org.json.JSONObject
import android.util.Base64
import javax.inject.Inject


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class LocalDataStore @Inject constructor(
    private val context: Context
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    // --- SAVE METHODS ---
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences -> preferences[ACCESS_TOKEN_KEY] = token }
    }

    suspend fun saveUserId(id: String) {
        context.dataStore.edit { preferences -> preferences[USER_ID_KEY] = id }
    }

    // --- READ METHODS ---

    // 1. Get Token as a Flow (Used for observing login state in UI/ViewModels)
    fun getAuthToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }
    }

    // 2. Get User ID as a Flow (Used to fetch the profile data)
    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }

    // --- DELETE METHODS ---
    suspend fun clearData() {
        context.dataStore.edit { it.clear() }
    }
}

object JwtUtils {
    fun getUserIdFromToken(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null

            val payload = parts[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes, Charsets.UTF_8)

            val jsonObject = JSONObject(decodedString)

            jsonObject.optString("id")
        } catch (e: Exception) {
            null
        }
    }
}