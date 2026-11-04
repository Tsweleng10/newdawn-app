package com.example.uinewdawn.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore by preferencesDataStore(name = "auth_prefs")

object TokenManager {

    private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val USER_ID_KEY = stringPreferencesKey("user_id")

    // ADDED THEME KEY
    private val THEME_KEY = stringPreferencesKey("theme_mode")

    lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // ---------- SAVE ----------
    suspend fun saveToken(token: String) {
        appContext.dataStore.edit { prefs -> prefs[TOKEN_KEY] = token }
    }

    suspend fun saveUser(id: String, fullName: String, email: String) {
        appContext.dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = id
            prefs[USER_NAME_KEY] = fullName
            prefs[USER_EMAIL_KEY] = email
        }
    }

    // ---------- THEME METHODS ----------
    suspend fun saveThemeMode(mode: String) {
        appContext.dataStore.edit { prefs -> prefs[THEME_KEY] = mode }
    }

    suspend fun getThemeMode(): String {
        return appContext.dataStore.data.first()[THEME_KEY] ?: "system"
    }

    fun getThemeModeBlocking(): String = runBlocking {
        try {
            appContext.dataStore.data.first()[THEME_KEY] ?: "system"
        } catch (e: Exception) {
            "system"
        }
    }

    // ---------- READ ----------
    suspend fun getTokenAsync(): String? = appContext.dataStore.data.first()[TOKEN_KEY]

    fun getToken(): String? = runBlocking {
        try { appContext.dataStore.data.first()[TOKEN_KEY] } catch (e: Exception) { null }
    }

    suspend fun getUserName(): String? = appContext.dataStore.data.first()[USER_NAME_KEY]
    suspend fun getUserEmail(): String? = appContext.dataStore.data.first()[USER_EMAIL_KEY]
    suspend fun getUserId(): String? = appContext.dataStore.data.first()[USER_ID_KEY]

    // ---------- CLEAR ----------
    suspend fun clear() {
        appContext.dataStore.edit { it.clear() }
    }

    fun isLoggedIn(): Boolean = getToken() != null
}