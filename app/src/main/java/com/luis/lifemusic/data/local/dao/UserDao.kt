package com.luis.lifemusic.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.luis.lifemusic.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * UserDao
 *
 * Capa de acceso directo a la tabla "users" en Room.
 *
 * ✅ Reglas del proyecto:
 * - Login SOLO con email.
 * - Email es único (índice unique en la entidad).
 * - El id autogenerado es la clave primaria real.
 *
 * ⚠️ El ViewModel NO debe usar este DAO directamente.
 * Debe pasar siempre por UserRepository.
 */
@Dao
interface UserDao {

    /**
     * 🔐 LOGIN:
     * Busca usuario por email (único).
     *
     * Devuelve:
     * - UserEntity si existe
     * - null si no existe
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?

    /**
     * 👤 PERFIL:
     * Observa usuario por id (usado con sesión activa).
     *
     * Flow permite que la UI se actualice automáticamente
     * si el usuario cambia en la base de datos.
     */
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun observeById(userId: Long): Flow<UserEntity?>

    /**
     * 👤 Obtener usuario por id en modo suspend (para updates).
     */
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getById(userId: Long): UserEntity?

    /**
     * ➕ Registro:
     * Inserta nuevo usuario.
     *
     * OnConflictStrategy.ABORT:
     * - Si el email ya existe (índice unique),
     *   Room lanza excepción.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    /**
     * ✏️ Actualización:
     * Actualiza todos los campos del usuario.
     */
    @Update
    suspend fun update(user: UserEntity)
}
