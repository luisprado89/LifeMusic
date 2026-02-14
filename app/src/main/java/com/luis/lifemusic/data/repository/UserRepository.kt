package com.luis.lifemusic.data.repository

import com.luis.lifemusic.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * UserRepository (contrato).
 *
 * ✅ Por qué existe:
 * - El ViewModel NO debe hablar con UserDao directamente.
 * - Aquí se define lo que la app puede hacer con usuarios (registro/login/perfil).
 */
interface UserRepository {

    /** Observa un usuario por id (para perfil). */
    fun observeUser(userId: Long): Flow<UserEntity?>

    /** Registro: crea usuario y devuelve el id autogenerado. */
    suspend fun register(
        username: String,
        password: String,
        securityQuestion: String,
        securityAnswer: String
    ): Long

    /** Login: valida credenciales y devuelve el id si es correcto, o null si falla. */
    suspend fun login(username: String, password: String): Long?

    /** Actualiza datos editables del perfil. */
    suspend fun updateProfile(userId: Long, displayName: String, email: String)

    /** Guarda la foto del perfil como URI string (content://...). */
    suspend fun updatePhoto(userId: Long, photoUri: String?)
}
