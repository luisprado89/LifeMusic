package com.luis.lifemusic.data.repository

import com.luis.lifemusic.data.local.dao.FavoriteDao
import com.luis.lifemusic.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

class FavoritesRepositoryImpl(
    private val favoriteDao: FavoriteDao
) : FavoritesRepository {

    override fun observeFavoriteSongIds(userId: Long): Flow<List<Int>> =
        favoriteDao.observeFavoriteSongIds(userId)

    override suspend fun addFavorite(userId: Long, songId: Int) {
        favoriteDao.addFavorite(FavoriteEntity(userId = userId, songId = songId))
    }

    override suspend fun removeFavorite(userId: Long, songId: Int) {
        favoriteDao.removeFavorite(userId, songId)
    }

    override suspend fun isFavorite(userId: Long, songId: Int): Boolean =
        favoriteDao.isFavorite(userId, songId)
}
