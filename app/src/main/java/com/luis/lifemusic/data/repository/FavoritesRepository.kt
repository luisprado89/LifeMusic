package com.luis.lifemusic.data.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestionar los favoritos de un usuario.
 *
 * Abstrae el origen de datos (DAO) de los ViewModels.
 * Todas las operaciones se basan en el ID de Spotify (String).
 */
interface FavoritesRepository {

    /**
     * Observa en tiempo real la lista de IDs de canciones favoritas de un usuario.
     */
    fun observeFavoriteSongIds(userId: Long): Flow<List<String>>

    /**
     * Añade una lista de canciones a los favoritos de un usuario.
     * Ideal para insertar las 6 canciones iniciales en el registro.
     */
    suspend fun addFavorites(userId: Long, songSpotifyIds: List<String>)

    /**
     * Añade una única canción a los favoritos de un usuario.
     */
    suspend fun addFavorite(userId: Long, songSpotifyId: String)

    /**
     * Elimina una canción de los favoritos de un usuario.
     */
    suspend fun removeFavorite(userId: Long, songSpotifyId: String)

    /**
     * Comprueba si una canción es favorita para un usuario.
     */
    suspend fun isFavorite(userId: Long, songSpotifyId: String): Boolean
}
