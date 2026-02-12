package com.luis.lifemusic.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

/**
 * DataStore de sesión (clave-valor).
 *
 * ¿Por qué aquí?
 * - La sesión es un dato simple (userId actual), NO una tabla.
 * - DataStore sustituye a SharedPreferences en proyectos modernos.
 *
 * Guardaremos:
 * - session_user_id -> Long (id autogenerado en Room)
 */
private const val SESSION_DATASTORE_NAME = "session_datastore"

val Context.sessionDataStore by preferencesDataStore(name = SESSION_DATASTORE_NAME)

object SessionKeys {
    val SESSION_USER_ID = longPreferencesKey("session_user_id")
}
