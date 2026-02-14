package com.luis.lifemusic.data.repository

import kotlinx.coroutines.flow.Flow

/**
 * Favoritos por usuario.
 *
 * ✅ La UI/VM no habla con FavoriteDao directo.
 */
interface FavoritesRepository {

    /** Observa los songIds favoritos de un usuario. */
    fun observeFavoriteSongIds(userId: Long): Flow<List<Int>>

    /** Marca favorito. */
    suspend fun addFavorite(userId: Long, songId: Int)

    /** Quita favorito. */
    suspend fun removeFavorite(userId: Long, songId: Int)

    /** Comprueba si una canción es favorita. */
    suspend fun isFavorite(userId: Long, songId: Int): Boolean
}
