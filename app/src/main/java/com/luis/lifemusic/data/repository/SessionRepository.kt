package com.luis.lifemusic.data.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de sesión.
 *
 * ¿Por qué repositorio?
 * - El ViewModel NO debe acceder a DataStore directamente.
 * - Centraliza la lectura/escritura de sesión.
 */
interface SessionRepository {

    /** Observa el userId de la sesión actual (null si no hay sesión). */
    val sessionUserId: Flow<Long?>

    /** Guarda el userId cuando el login es correcto. */
    suspend fun setLoggedInUserId(userId: Long)

    /** Borra la sesión (logout). */
    suspend fun clearSession()
}
