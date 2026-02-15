package com.luis.lifemusic.data.repository

import com.luis.lifemusic.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * UserRepository (contrato).
 *
 * ✅ Reglas acordadas:
 * - Login SOLO con email.
 * - Email es único en BD.
 * - username NO existe como credencial.
 * - El "username visual" se deriva del email (antes del @) en la UI.
 */
interface UserRepository {

    /**
     * Observa un usuario por id (útil para Profile).
     */
    fun observeUser(userId: Long): Flow<UserEntity?>

    /**
     * Registro:
     * - Crea usuario y devuelve el id autogenerado.
     */
    suspend fun register(
        displayName: String,
        email: String,
        password: String,
        birthDate: Long,
        securityQuestion: String,
        securityAnswer: String
    ): Long

    /**
     * Login:
     * - Valida credenciales por email y devuelve el id si es correcto, o null si falla.
     */
    suspend fun login(email: String, password: String): Long?

    /**
     * Recuperación:
     * - Obtiene la pregunta de seguridad de una cuenta por email.
     */
    suspend fun getSecurityQuestion(email: String): String?

    /**
     * Recuperación:
     * - Valida respuesta y actualiza contraseña.
     */
    suspend fun resetPassword(email: String, securityAnswer: String, newPassword: String): Boolean

    /**
     * Actualiza datos editables del perfil.
     */
    suspend fun updateProfile(userId: Long, displayName: String, email: String)

    /**
     * Guarda la foto del perfil como URI string (content://...) o null.
     */
    suspend fun updatePhoto(userId: Long, photoUri: String?)
}
