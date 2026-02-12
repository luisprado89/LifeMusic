package com.luis.lifemusic.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.luis.lifemusic.data.datastore.SessionKeys
import com.luis.lifemusic.data.datastore.sessionDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación concreta del repositorio de sesión usando DataStore.
 *
 * 🔹 Responsabilidad:
 * Gestionar únicamente la sesión activa del usuario.
 *
 * 🔹 Importante:
 * - NO guarda username ni password.
 * - Solo almacena el id autogenerado de Room.
 * - Si el valor es null → no hay sesión activa.
 */
class SessionRepositoryImpl(
    /**
     * Contexto de aplicación necesario para acceder a DataStore.
     *
     * ⚠ No se pasará este Context al ViewModel.
     * El ViewModel solo conocerá la interfaz SessionRepository.
     */
    private val appContext: Context
) : SessionRepository {

    /**
     * Flow observable que emite el userId actual.
     *
     * - null → usuario no logueado.
     * - Long → usuario con sesión activa.
     *
     * Permite que la app reaccione automáticamente
     * ante login o logout.
     */
    override val sessionUserId: Flow<Long?> =
        appContext.sessionDataStore.data.map { preferences ->
            preferences[SessionKeys.SESSION_USER_ID]
        }

    /**
     * Guarda el userId cuando el login es correcto.
     */
    override suspend fun setLoggedInUserId(userId: Long) {
        appContext.sessionDataStore.edit { preferences ->
            preferences[SessionKeys.SESSION_USER_ID] = userId
        }
    }

    /**
     * Borra la sesión activa (logout).
     */
    override suspend fun clearSession() {
        appContext.sessionDataStore.edit { preferences ->
            preferences.remove(SessionKeys.SESSION_USER_ID)
        }
    }
}
