package com.remotebridge.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.map

/**
 * تخزين إعدادات التطبيق الدائمة (عنوان الخادم، اسم الجهاز، الدور).
 * يستخدم DataStore Preferences بدلاً من SharedPreferences لكونه آمناً مع Coroutines.
 */
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "bridge_settings"
)

class SettingsRepository(private val context: Context) {

    private object Keys {
        val SERVER_URL = stringPreferencesKey("server_url")
        val DEVICE_NAME = stringPreferencesKey("device_name")
        val ROLE = stringPreferencesKey("role")
        val AUTO_START = booleanPreferencesKey("auto_start_host")
    }

    val serverUrl: Flow<String> = context.settingsDataStore.data.map { it[Keys.SERVER_URL] ?: "" }
    val deviceName: Flow<String> = context.settingsDataStore.data.map { it[Keys.DEVICE_NAME] ?: "" }
    val role: Flow<String> = context.settingsDataStore.data.map { it[Keys.ROLE] ?: "" }
    val autoStartHost: Flow<Boolean> = context.settingsDataStore.data.map {
        it[Keys.AUTO_START] ?: false
    }

    suspend fun setServerUrl(url: String) {
        context.settingsDataStore.edit { it[Keys.SERVER_URL] = url }
    }

    suspend fun setDeviceName(name: String) {
        context.settingsDataStore.edit { it[Keys.DEVICE_NAME] = name }
    }

    suspend fun setRole(role: String) {
        context.settingsDataStore.edit { it[Keys.ROLE] = role }
    }

    suspend fun setAutoStartHost(autoStart: Boolean) {
        context.settingsDataStore.edit { it[Keys.AUTO_START] = autoStart }
    }
}
