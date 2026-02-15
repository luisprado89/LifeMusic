package com.luis.lifemusic.data.repository

import com.luis.lifemusic.data.local.dao.UserDao
import com.luis.lifemusic.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Implementación real del repositorio de usuarios usando Room (UserDao).
 *
 * ✅ Reglas acordadas:
 * - Login SOLO con email (único).
 * - Email se normaliza (trim + lowercase).
 * - El "username visual" NO se guarda: se deriva del email (antes del @) en la UI.
 *
 * ⚠️ Nota (proyecto real):
 * - No guardar contraseñas en texto plano (usar hashing + salt).
 */
class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    /**
     * Observa un usuario por id (útil para Profile).
     *
     * Flow permite que la UI se actualice automáticamente
     * si el usuario cambia en la base de datos.
     */
    override fun observeUser(userId: Long): Flow<UserEntity?> =
        userDao.observeById(userId)

    /**
     * Registro:
     * - Normaliza email (trim + lowercase).
     * - Normaliza displayName (trim).
     * - Crea UserEntity inicial.
     * - Devuelve id autogenerado por Room.
     *
     * ✅ Email es único: si ya existe, Room lanzará excepción (constraint).
     */
    override suspend fun register(
        displayName: String,
        email: String,
        password: String,
        birthDate: Long,
        securityQuestion: String,
        securityAnswer: String
    ): Long {
        val normalizedEmail = email.trim().lowercase()
        val normalizedName = displayName.trim()

        val user = UserEntity(
            email = normalizedEmail,
            password = password,
            displayName = normalizedName,
            birthDate = birthDate,
            securityQuestion = securityQuestion.trim(),
            securityAnswer = securityAnswer.trim(),
            photoUri = null
        )

        return userDao.insert(user)
    }

    /**
     * Login:
     * - Busca por email (normalizado).
     * - Compara la contraseña.
     * - Si coincide, devuelve id; si no, null.
     */
    override suspend fun login(email: String, password: String): Long? {
        val user = userDao.findByEmail(email.trim().lowercase())
        return if (user != null && user.password == password) user.id else null
    }

    /**
     * Recuperación:
     * - Obtiene la pregunta de seguridad asociada a un email.
     */
    override suspend fun getSecurityQuestion(email: String): String? {
        val user = userDao.findByEmail(email.trim().lowercase())
        return user?.securityQuestion
    }

    /**
     * Recuperación / reset de contraseña:
     * - Busca usuario por email.
     * - Verifica respuesta de seguridad (trim + lowercase).
     * - Si coincide, actualiza password y devuelve true.
     * - Si falla, devuelve false sin modificar.
     */
    override suspend fun resetPassword(
        email: String,
        securityAnswer: String,
        newPassword: String
    ): Boolean {
        val user = userDao.findByEmail(email.trim().lowercase()) ?: return false

        val expectedAnswer = user.securityAnswer.trim().lowercase()
        val providedAnswer = securityAnswer.trim().lowercase()
        if (expectedAnswer != providedAnswer) return false

        userDao.update(user.copy(password = newPassword))
        return true
    }

    /**
     * Actualiza datos editables del perfil:
     * - displayName (trim)
     * - email (trim + lowercase) -> sigue siendo único
     *
     * ✅ Si cambias el email a uno ya existente, Room lanzará excepción (constraint).
     */
    override suspend fun updateProfile(userId: Long, displayName: String, email: String) {
        val current = userDao.getById(userId) ?: return

        val normalizedEmail = email.trim().lowercase()
        val normalizedName = displayName.trim()

        userDao.update(
            current.copy(
                displayName = normalizedName,
                email = normalizedEmail
            )
        )
    }

    /**
     * Guarda la foto de perfil como URI (String) o null.
     */
    override suspend fun updatePhoto(userId: Long, photoUri: String?) {
        val current = userDao.getById(userId) ?: return
        userDao.update(current.copy(photoUri = photoUri))
    }
}
