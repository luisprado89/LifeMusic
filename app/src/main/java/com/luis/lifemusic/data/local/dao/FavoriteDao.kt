package com.luis.lifemusic.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.luis.lifemusic.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    /**
     * Observa los IDs de Spotify (String) de las canciones favoritas de un usuario.
     */
    @Query("SELECT song_spotify_id FROM favorites WHERE user_id = :userId")
    fun observeFavoriteSongIds(userId: Long): Flow<List<String>>

    /**
     * Añade una única canción a favoritos.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: FavoriteEntity)
    
    /**
     * Añade una lista de canciones a favoritos. Útil para el registro.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorites(favorites: List<FavoriteEntity>)

    /**
     * Elimina una canción de favoritos usando su ID de Spotify.
     */
    @Query("DELETE FROM favorites WHERE user_id = :userId AND song_spotify_id = :songSpotifyId")
    suspend fun removeFavorite(userId: Long, songSpotifyId: String)

    /**
     * Comprueba si una canción específica (por su ID de Spotify) es favorita para un usuario.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE user_id = :userId AND song_spotify_id = :songSpotifyId)")
    suspend fun isFavorite(userId: Long, songSpotifyId: String): Boolean
}
