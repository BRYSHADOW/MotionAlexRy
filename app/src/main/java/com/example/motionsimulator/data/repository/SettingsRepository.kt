package com.example.motionsimulator.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.motionsimulator.data.model.AppSettings
import com.example.motionsimulator.data.model.EasingType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Singleton DataStore – phải khai báo ở mức top-level để tránh tạo nhiều instance.
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "motion_simulator_settings"
)

/**
 * Repository đọc/ghi cài đặt ứng dụng thông qua Jetpack DataStore Preferences.
 * Tất cả hàm suspend phải gọi từ coroutine (ví dụ viewModelScope.launch).
 */
class SettingsRepository(private val context: Context) {

    // ── Preference Keys ──────────────────────────────────────────────────────
    companion object {
        private val KEY_DARK_MODE      = booleanPreferencesKey("dark_mode")
        private val KEY_THEME_INDEX    = intPreferencesKey("theme_index")
        private val KEY_ANIM_SPEED     = floatPreferencesKey("animation_speed")
        private val KEY_DELAY_TIME     = floatPreferencesKey("delay_time")
        private val KEY_ENABLE_LOOP    = booleanPreferencesKey("enable_loop")
        private val KEY_VIBRATION      = booleanPreferencesKey("vibration")
        private val KEY_SOUND_EFFECT   = booleanPreferencesKey("sound_effect")
        private val KEY_EASING_TYPE    = stringPreferencesKey("easing_type")
    }

    // ── Đọc dữ liệu (Flow tự cập nhật khi có thay đổi) ──────────────────────

    val settingsFlow: Flow<AppSettings> = context.dataStore.data
        .catch { exception ->
            // Nếu file bị lỗi, emit preferences rỗng thay vì crash
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { prefs ->
            AppSettings(
                isDarkMode    = prefs[KEY_DARK_MODE]   ?: false,
                themeIndex    = prefs[KEY_THEME_INDEX]  ?: 0,
                animationSpeed= prefs[KEY_ANIM_SPEED]   ?: 1.0f,
                delayTime     = prefs[KEY_DELAY_TIME]   ?: 1.0f,
                enableLoop    = prefs[KEY_ENABLE_LOOP]  ?: false,
                vibration     = prefs[KEY_VIBRATION]    ?: true,
                soundEffect   = prefs[KEY_SOUND_EFFECT] ?: false,
                easingType    = runCatching {
                    EasingType.valueOf(prefs[KEY_EASING_TYPE] ?: EasingType.LINEAR.name)
                }.getOrDefault(EasingType.LINEAR)
            )
        }

    // ── Ghi dữ liệu ──────────────────────────────────────────────────────────

    suspend fun setDarkMode(v: Boolean)       = context.dataStore.edit { it[KEY_DARK_MODE]    = v }
    suspend fun setThemeIndex(v: Int)         = context.dataStore.edit { it[KEY_THEME_INDEX]  = v }
    suspend fun setAnimationSpeed(v: Float)   = context.dataStore.edit { it[KEY_ANIM_SPEED]   = v }
    suspend fun setDelayTime(v: Float)        = context.dataStore.edit { it[KEY_DELAY_TIME]   = v }
    suspend fun setEnableLoop(v: Boolean)     = context.dataStore.edit { it[KEY_ENABLE_LOOP]  = v }
    suspend fun setVibration(v: Boolean)      = context.dataStore.edit { it[KEY_VIBRATION]    = v }
    suspend fun setSoundEffect(v: Boolean)    = context.dataStore.edit { it[KEY_SOUND_EFFECT] = v }
    suspend fun setEasingType(v: EasingType)  = context.dataStore.edit { it[KEY_EASING_TYPE]  = v.name }
}
