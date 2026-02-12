package com.luis.lifemusic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luis.lifemusic.data.local.dao.FavoriteDao
import com.luis.lifemusic.data.local.dao.UserDao
import com.luis.lifemusic.data.local.entities.FavoriteEntity
import com.luis.lifemusic.data.local.entities.UserEntity

/**
 * Base de datos Room.
 *
 * ✅ De momento: users + favorites
 * - Más adelante podemos añadir tablas si quieres persistir canciones o cachear la API.
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
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
}
