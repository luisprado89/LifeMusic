package com.luis.lifemusic.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.localsed.LocalSeedSong
import com.luis.lifemusic.data.localsed.localSeedSongs
import com.luis.lifemusic.data.repository.FavoritesRepository
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.data.repository.SpotifyRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

/**
 * ============================================================
 * LIST VIEWMODEL (Mis Favoritos)
 * ============================================================
 *
 * 🎯 RESPONSABILIDAD:
 * - Leer favoritos (IDs) desde Room.
 * - Convertir esos IDs en canciones completas (LocalSeedSong):
 *   - Si existe en catálogo local → se usa al instante.
 *   - Si NO existe → se pide a Spotify por ID.
 *
 * ✅ OFFLINE:
 * - Si no hay conexión, los IDs remotos NO desaparecen:
 *   mostramos un aviso con "missingRemoteCount".
 * 🎯 MEJORA:
 * - Prioriza catálogo local (tiene popularity real)
 * - Para canciones de Spotify, asigna popularity coherente
 */
class ListViewModel(
    private val sessionRepository: SessionRepository,
    private val favoritesRepository: FavoritesRepository,
    private val spotifyRepository: SpotifyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState(isLoading = true))
    val uiState: StateFlow<ListUiState> = _uiState

    // Catálogo local indexado por ID (¡con popularity real!)
    private val localCatalogById: Map<String, LocalSeedSong> =
        localSeedSongs.associateBy { it.spotifyId }

    // Cache remota
    private val remoteCacheById: MutableMap<String, LocalSeedSong> = mutableMapOf()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            val userId = sessionRepository.sessionUserId.first()
            if (userId == null) {
                _uiState.update {
                    it.copy(
                        favoriteSongs = emptyList(),
                        isLoading = false,
                        hasActiveSession = false,
                        missingRemoteCount = 0,
                        missingRemoteIds = emptyList()
                    )
                }
                return@launch
            }

            favoritesRepository.observeFavoriteSongIds(userId).collect { favoriteIds ->
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        hasActiveSession = true
                    )
                }

                try {
                    val songs = hydrateFavoriteIdsToSongs(favoriteIds)

                    val resolvedIds = songs.map { it.spotifyId }.toSet()
                    val missing = favoriteIds.filter { id ->
                        localCatalogById[id] == null && !resolvedIds.contains(id)
                    }

                    _uiState.update {
                        it.copy(
                            favoriteSongs = songs,
                            isLoading = false,
                            missingRemoteCount = missing.size,
                            missingRemoteIds = missing
                        )
                    }
                } catch (_: Exception) {
                    _uiState.update {
                        it.copy(
                            favoriteSongs = emptyList(),
                            isLoading = false,
                            errorMessage = "No se pudieron cargar tus favoritos.",
                            missingRemoteCount = 0,
                            missingRemoteIds = emptyList()
                        )
                    }
                }
            }
        }
    }

    /**
     * Convierte IDs favoritas a canciones completas.
     *
     * ✅ MEJORADO:
     * - Las canciones locales conservan su popularity real
     * - Las canciones de Spotify reciben popularity coherente
     */
    private suspend fun hydrateFavoriteIdsToSongs(favoriteIds: List<String>): List<LocalSeedSong> {
        if (favoriteIds.isEmpty()) return emptyList()

        val result = ArrayList<LocalSeedSong>(favoriteIds.size)

        val missingIds = favoriteIds.filter { id ->
            val local = localCatalogById[id]      // 🔥 TIENE POPULARITY REAL
            val cached = remoteCacheById[id]
            when {
                local != null -> {
                    result.add(local)              // ✅ Usamos local con popularity
                    false
                }
                cached != null -> {
                    result.add(cached)
                    false
                }
                else -> true
            }
        }

        if (missingIds.isNotEmpty()) {
            val fetched = viewModelScope.launchFetchMissing(missingIds)

            // ✅ Para los fetched, aseguramos que tengan popularity coherente
            val fetchedWithPopularity = fetched.map { remoteSong ->
                if (remoteSong.popularity == 0) {
                    // Asignamos un valor basado en el ID (consistente)
                    remoteSong.copy(
                        popularity = (remoteSong.spotifyId.hashCode().absoluteValue % 70) + 30
                    )
                } else remoteSong
            }

            remoteCacheById.putAll(fetchedWithPopularity.associateBy { it.spotifyId })

            // Reconstruimos respetando el orden original
            val byId = (result + fetchedWithPopularity).associateBy { it.spotifyId }
            return favoriteIds.mapNotNull { byId[it] }
        }

        return result
    }

    /**
     * Fetch paralelo de detalles por ID.
     */
    private suspend fun kotlinx.coroutines.CoroutineScope.launchFetchMissing(
        ids: List<String>
    ): List<LocalSeedSong> {
        val jobs = ids.distinct().map { id ->
            async {
                spotifyRepository.getTrackDetail(id)
            }
        }
        return jobs.mapNotNull { it.await() }
    }

    /**
     * Alterna favorito desde ListPage.
     */
    fun toggleFavorite(songSpotifyId: String) {
        viewModelScope.launch {
            val userId = sessionRepository.sessionUserId.first() ?: return@launch

            val isFav = favoritesRepository.isFavorite(userId, songSpotifyId)
            if (isFav) {
                favoritesRepository.removeFavorite(userId, songSpotifyId)
            } else {
                favoritesRepository.addFavorite(userId, songSpotifyId)
            }
        }
    }
}