package com.tofiq.blood.data.local

import android.content.Context
import android.content.SharedPreferences
import com.tofiq.blood.data.model.AuthToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class for handling app preferences using SharedPreferences
 * Follows SSOT (Single Source of Truth) principle
 */
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "donor_plus_prefs"
        private const val KEY_BASE_URL = "base_url"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_PHONE_NUMBER = "phone_number"
        private const val DEFAULT_BASE_URL = "http://192.168.103.177:8080"
    }

    /**
     * Get the configured base URL
     */
    fun getBaseUrl(): String {
        return sharedPreferences.getString(KEY_BASE_URL, DEFAULT_BASE_URL) ?: DEFAULT_BASE_URL
    }

    /**
     * Save the base URL
     */
    fun saveBaseUrl(baseUrl: String) {
        sharedPreferences.edit().putString(KEY_BASE_URL, baseUrl).apply()
    }

    /**
     * Save authentication token
     */
    fun saveAuthToken(authToken: AuthToken) {
        sharedPreferences.edit().apply {
            putString(KEY_AUTH_TOKEN, authToken.token)
            putString(KEY_REFRESH_TOKEN, authToken.refreshToken)
            putString(KEY_USER_ID, authToken.userId)
            putString(KEY_PHONE_NUMBER, authToken.phoneNumber)
            apply()
        }
    }

    /**
     * Get authentication token
     */
    fun getAuthToken(): AuthToken? {
        val token = sharedPreferences.getString(KEY_AUTH_TOKEN, null) ?: return null
        return AuthToken(
            token = token,
            refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, null),
            userId = sharedPreferences.getString(KEY_USER_ID, null),
            phoneNumber = sharedPreferences.getString(KEY_PHONE_NUMBER, null)
        )
    }

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null) != null
    }

    /**
     * Clear authentication data (logout)
     */
    fun clearAuthData() {
        sharedPreferences.edit().apply {
            remove(KEY_AUTH_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_PHONE_NUMBER)
            apply()
        }
    }

    /**
     * Clear all preferences
     */
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}

