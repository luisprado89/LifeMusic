package com.luis.lifemusic.data.repository

import com.luis.lifemusic.data.local.dao.UserDao
import com.luis.lifemusic.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Implementación real con Room (UserDao).
 *
 * Nota:
 * - Aquí podrías meter validaciones de negocio (p.ej. password mínima, etc.).
 * - En un proyecto real, NO guardarías la contraseña en texto plano.
 */
class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override fun observeUser(userId: Long): Flow<UserEntity?> =
        userDao.observeById(userId)

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

    override suspend fun login(username: String, password: String): Long? {
        val user = userDao.findByUsername(username.trim())
        return if (user != null && user.password == password) user.id else null
    }

    override suspend fun updateProfile(userId: Long, displayName: String, email: String) {
        val current = userDao.getById(userId) ?: return
        userDao.update(
            current.copy(
                displayName = displayName.trim(),
                email = email.trim()
            )
        )
    }

    override suspend fun updatePhoto(userId: Long, photoUri: String?) {
        val current = userDao.getById(userId) ?: return
        userDao.update(current.copy(photoUri = photoUri))
    }

}
