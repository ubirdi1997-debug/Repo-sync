package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.remote.UserProfileDto

class UserSessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("usafe_session_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_TIER = "user_tier"
        private const val KEY_LAST_SYNC_TS = "last_sync_timestamp"
        private const val KEY_DARK_MODE = "dark_mode_enabled"
        private const val KEY_PRESET_ID = "preset_id"
        private const val KEY_REDUCED_MOTION = "reduced_motion"
        private const val KEY_HIGH_CONTRAST = "high_contrast"
        private const val KEY_SELECTED_PROVIDER = "selected_inference_provider"
        private const val KEY_GIT_TOKEN = "git_personal_access_token"
        private const val KEY_AGENT_PERMISSION = "agent_permission_mode"
    }

    fun saveProviderConfig(providerId: String, key: String, model: String) {
        prefs.edit()
            .putString("byok_key_$providerId", key)
            .putString("byok_model_$providerId", model)
            .apply()
    }

    fun getProviderKey(providerId: String): String? = prefs.getString("byok_key_$providerId", null)

    fun getProviderModel(providerId: String, defaultModel: String): String =
        prefs.getString("byok_model_$providerId", defaultModel) ?: defaultModel

    fun setSelectedProviderId(id: String) {
        prefs.edit().putString(KEY_SELECTED_PROVIDER, id).apply()
    }

    fun getSelectedProviderId(): String = prefs.getString(KEY_SELECTED_PROVIDER, "gemini") ?: "gemini"

    fun setGitToken(token: String) {
        prefs.edit().putString(KEY_GIT_TOKEN, token).apply()
    }

    fun getGitToken(): String = prefs.getString(KEY_GIT_TOKEN, "ghp_usafe_developer_sync_key") ?: "ghp_usafe_developer_sync_key"

    fun setAgentPermission(isAutoPilot: Boolean) {
        prefs.edit().putBoolean(KEY_AGENT_PERMISSION, isAutoPilot).apply()
    }

    fun isAutoPilot(): Boolean = prefs.getBoolean(KEY_AGENT_PERMISSION, true)

    fun saveUserSession(token: String, user: UserProfileDto) {
        prefs.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .putString(KEY_USER_ID, user.id)
            .putString(KEY_USER_EMAIL, user.email)
            .putString(KEY_USER_NAME, user.name)
            .putString(KEY_USER_TIER, user.tier)
            .apply()
    }

    fun getAuthToken(): String? = prefs.getString(KEY_AUTH_TOKEN, null)

    fun getUser(): UserProfileDto? {
        val email = prefs.getString(KEY_USER_EMAIL, null) ?: return null
        return UserProfileDto(
            id = prefs.getString(KEY_USER_ID, "usr_99") ?: "usr_99",
            email = email,
            name = prefs.getString(KEY_USER_NAME, "Vibe Architect"),
            tier = prefs.getString(KEY_USER_TIER, "Pro Cloud Tier")
        )
    }

    fun clearSession() {
        prefs.edit()
            .remove(KEY_AUTH_TOKEN)
            .remove(KEY_USER_ID)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_USER_NAME)
            .remove(KEY_USER_TIER)
            .apply()
    }

    fun saveLastSync(timestamp: Long) {
        prefs.edit().putLong(KEY_LAST_SYNC_TS, timestamp).apply()
    }

    fun getLastSync(): Long = prefs.getLong(KEY_LAST_SYNC_TS, 0L)

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    fun isDarkMode(defaultVal: Boolean = true): Boolean = prefs.getBoolean(KEY_DARK_MODE, defaultVal)

    fun setPresetId(presetId: String) {
        prefs.edit().putString(KEY_PRESET_ID, presetId).apply()
    }

    fun getPresetId(): String? = prefs.getString(KEY_PRESET_ID, null)

    fun setReducedMotion(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_REDUCED_MOTION, enabled).apply()
    }

    fun isReducedMotion(): Boolean = prefs.getBoolean(KEY_REDUCED_MOTION, false)

    fun setHighContrast(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HIGH_CONTRAST, enabled).apply()
    }

    fun isHighContrast(): Boolean = prefs.getBoolean(KEY_HIGH_CONTRAST, false)
}
