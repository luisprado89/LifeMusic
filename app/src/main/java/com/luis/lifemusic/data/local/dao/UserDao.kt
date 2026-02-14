package com.luis.lifemusic.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.luis.lifemusic.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // ✅ Para login: buscamos por username (único por índice)
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): UserEntity?

    // ✅ Para recuperar perfil por id (sesión)
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun observeById(userId: Long): Flow<UserEntity?>

    // ✅ Para updates: obtener por id en modo suspend
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getById(userId: Long): UserEntity?


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)
}
