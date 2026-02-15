package com.luis.lifemusic.data.repository

import com.luis.lifemusic.data.local.dao.UserDao
import com.luis.lifemusic.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Implementación real del repositorio de usuarios usando Room (UserDao).
 *
 * ✅ Responsabilidad:
 * - Actuar como capa intermedia entre ViewModels y la base de datos (DAO).
 * - Evitar que los ViewModels conozcan Room directamente.
 *
 * ✅ Nota importante (proyecto real):
 * - Aquí podrías meter validaciones de negocio (p.ej. password mínima, username válido, etc.).
 * - En un proyecto real NO guardarías contraseñas en texto plano:
 *   se usaría hashing + salt (por ejemplo BCrypt).
 */
class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    /**
     * Observa un usuario por id (útil para pantalla de perfil).
     *
     * Flow permite que la UI se actualice automáticamente
     * si el usuario cambia en la base de datos.
     */
    override fun observeUser(userId: Long): Flow<UserEntity?> =
        userDao.observeById(userId)

    /**
     * Registro de usuario:
     * - Normaliza campos (trim en strings).
     * - Crea el UserEntity inicial.
     * - Devuelve el id autogenerado por Room.
     */
    override suspend fun register(
        username: String,
        password: String,
        securityQuestion: String,
        securityAnswer: String
    ): Long {
        val user = UserEntity(
            username = username.trim(),
            password = password,
            securityQuestion = securityQuestion.trim(),
            securityAnswer = securityAnswer.trim(),
            displayName = username.trim(),
            email = ""
        )

        // UserDao.insert devuelve el id autogenerado
        return userDao.insert(user)
    }

    /**
     * Login:
     * - Busca el usuario por username.
     * - Compara la contraseña.
     * - Si coincide, devuelve el id; si no, devuelve null.
     *
     * Nota (proyecto real):
     * - Esto se haría con hash, no comparando texto plano.
     */
    override suspend fun login(username: String, password: String): Long? {
        val user = userDao.findByUsername(username.trim())
        return if (user != null && user.password == password) user.id else null
    }

    /**
     * Recupera la pregunta de seguridad asociada a un username.
     *
     * Uso típico:
     * - Pantalla "Recuperar contraseña": primero se pide username,
     *   luego se muestra la pregunta.
     */
    override suspend fun getSecurityQuestion(username: String): String? {
        val user = userDao.findByUsername(username.trim())
        return user?.securityQuestion
    }

    /**
     * Recuperación / reset de contraseña:
     * - Busca el usuario por username.
     * - Verifica que la respuesta de seguridad coincide.
     * - Si coincide, actualiza la contraseña y devuelve true.
     * - Si falla, devuelve false sin modificar nada.
     *
     * Detalle:
     * - Normalizamos respuestas (trim + lowercase) para evitar fallos por:
     *   espacios accidentales o diferencias de mayúsculas/minúsculas.
     */
    override suspend fun resetPassword(
        username: String,
        securityAnswer: String,
        newPassword: String
    ): Boolean {
        val user = userDao.findByUsername(username.trim()) ?: return false

        val expectedAnswer = user.securityAnswer.trim().lowercase()
        val providedAnswer = securityAnswer.trim().lowercase()

        if (expectedAnswer != providedAnswer) return false

        userDao.update(user.copy(password = newPassword))
        return true
    }

    /**
     * Actualiza datos editables del perfil.
     *
     * - Si el usuario no existe, no hace nada.
     * - Normaliza los campos (trim) antes de guardarlos.
     */
    override suspend fun updateProfile(userId: Long, displayName: String, email: String) {
        val current = userDao.getById(userId) ?: return

        userDao.update(
            current.copy(
                displayName = displayName.trim(),
                email = email.trim()
            )
        )
    }

    /**
     * Guarda la foto de perfil como URI (String).
     *
     * Ejemplos:
     * - "content://..."
     * - null si se elimina la foto
     */
    override suspend fun updatePhoto(userId: Long, photoUri: String?) {
        val current = userDao.getById(userId) ?: return
        userDao.update(current.copy(photoUri = photoUri))
    }
}
