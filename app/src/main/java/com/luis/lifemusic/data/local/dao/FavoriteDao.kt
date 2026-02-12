package com.luis.lifemusic.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.luis.lifemusic.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT song_id FROM favorites WHERE user_id = :userId")
    fun observeFavoriteSongIds(userId: Long): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE user_id = :userId AND song_id = :songId")
    suspend fun removeFavorite(userId: Long, songId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE user_id = :userId AND song_id = :songId)")
    suspend fun isFavorite(userId: Long, songId: Int): Boolean
}
