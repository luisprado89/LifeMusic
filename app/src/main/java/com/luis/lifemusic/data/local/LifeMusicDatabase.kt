package com.luis.lifemusic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luis.lifemusic.data.local.dao.FavoriteDao
import com.luis.lifemusic.data.local.dao.UserDao
import com.luis.lifemusic.data.local.entities.FavoriteEntity
import com.luis.lifemusic.data.local.entities.UserEntity

/**
 * Base de datos Room de la app.
 *
 * ✅ Regla:
 * - Este archivo NO crea la base de datos (no hace Room.databaseBuilder aquí).
 * - Solo define:
 *   - entidades
 *   - versionado
 *   - getters a DAOs
 *
 * 👉 ¿Por qué?
 * - Mantiene la responsabilidad clara:
 *   - RoomDatabase = "contrato" de la BD
 *   - AppContainer = "fábrica" / creador de dependencias
 */
@Database(
    entities = [
        UserEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LifeMusicDatabase : RoomDatabase() {

    /** DAO de usuarios (registro, login, perfil) */
    abstract fun userDao(): UserDao

    /** DAO de favoritos (canciones favoritas por usuario) */
    abstract fun favoriteDao(): FavoriteDao
}
