package com.immortalwrt.manager.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferencesDataSource @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        val KEY_THEME = stringPreferencesKey("theme")
        val KEY_LANGUAGE = stringPreferencesKey("language")
        val KEY_IP_ANONYMIZE = booleanPreferencesKey("diagnostic_ip_anonymize")
        val KEY_MAC_ANONYMIZE = booleanPreferencesKey("diagnostic_mac_anonymize")
        val KEY_HOSTNAME_HIDDEN = booleanPreferencesKey("diagnostic_hostname_hidden")
    }

    val theme: Flow<String> = context.dataStore.data.map { it[KEY_THEME] ?: "system" }
    val language: Flow<String> = context.dataStore.data.map { it[KEY_LANGUAGE] ?: "auto" }
    val diagnosticDefaults: Flow<Triple<Boolean, Boolean, Boolean>> = context.dataStore.data.map {
        Triple(
            it[KEY_IP_ANONYMIZE] ?: false,
            it[KEY_MAC_ANONYMIZE] ?: false,
            it[KEY_HOSTNAME_HIDDEN] ?: false
        )
    }

    suspend fun setTheme(theme: String) { context.dataStore.edit { it[KEY_THEME] = theme } }
    suspend fun setLanguage(language: String) { context.dataStore.edit { it[KEY_LANGUAGE] = language } }
    suspend fun setDiagnosticDefaults(ipAnon: Boolean, macAnon: Boolean, hostnameHidden: Boolean) {
        context.dataStore.edit {
            it[KEY_IP_ANONYMIZE] = ipAnon
            it[KEY_MAC_ANONYMIZE] = macAnon
            it[KEY_HOSTNAME_HIDDEN] = hostnameHidden
        }
    }
}
