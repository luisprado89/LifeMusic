package com.luis.lifemusic.data.repository

import com.luis.lifemusic.data.local.dao.FavoriteDao
import com.luis.lifemusic.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class FavoritesRepositoryImpl(
    private val favoriteDao: FavoriteDao
) : FavoritesRepository {

    override fun observeFavoriteSongIds(userId: Long): Flow<List<String>> =
        favoriteDao.observeFavoriteSongIds(userId)

    override suspend fun addFavorites(userId: Long, songSpotifyIds: List<String>) {
        val favoriteEntities = songSpotifyIds.map { spotifyId ->
            FavoriteEntity(userId = userId, songSpotifyId = spotifyId)
        }
        favoriteDao.addFavorites(favoriteEntities)
    }

    override suspend fun addFavorite(userId: Long, songSpotifyId: String) {
        val favoriteEntity = FavoriteEntity(userId = userId, songSpotifyId = songSpotifyId)
        favoriteDao.addFavorite(favoriteEntity)
    }

    override suspend fun removeFavorite(userId: Long, songSpotifyId: String) {
        favoriteDao.removeFavorite(userId, songSpotifyId)
    }

    override suspend fun isFavorite(userId: Long, songSpotifyId: String): Boolean =
        favoriteDao.isFavorite(userId, songSpotifyId)
}
