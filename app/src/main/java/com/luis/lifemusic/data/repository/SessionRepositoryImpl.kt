package com.luis.lifemusic.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.luis.lifemusic.data.datastore.SessionKeys
import com.luis.lifemusic.data.datastore.sessionDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación real usando DataStore Preferences.
 *
 * Nota didáctica:
 * - sessionUserId emite null si no hay sesión.
 * - emitirá un Long cuando haya login.
 */
class SessionRepositoryImpl(
    private val context: Context
) : SessionRepository {

    override val sessionUserId: Flow<Long?> =
        context.sessionDataStore.data.map { prefs ->
            // Si no existe la clave, devolvemos null (no hay sesión).
            prefs[SessionKeys.SESSION_USER_ID]
        }

    override suspend fun setLoggedInUserId(userId: Long) {
        context.sessionDataStore.edit { prefs ->
            prefs[SessionKeys.SESSION_USER_ID] = userId
        }
    }

    override suspend fun clearSession() {
        context.sessionDataStore.edit { prefs ->
            prefs.remove(SessionKeys.SESSION_USER_ID)
        }
    }
}
