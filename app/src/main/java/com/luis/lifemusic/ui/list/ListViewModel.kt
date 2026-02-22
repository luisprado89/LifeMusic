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
 */
class ListViewModel(
    private val sessionRepository: SessionRepository,
    private val favoritesRepository: FavoritesRepository,
    private val spotifyRepository: SpotifyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState(isLoading = true))
    val uiState: StateFlow<ListUiState> = _uiState

    private val localCatalogById: Map<String, LocalSeedSong> =
        localSeedSongs.associateBy { it.spotifyId }

    /**
     * Cache simple en memoria para no pedir 20 veces la misma canción a Spotify.
     */
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
                        errorMessage = null,
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
                        errorMessage = null,
                        hasActiveSession = true
                    )
                }

                try {
                    val songs = hydrateFavoriteIdsToSongs(favoriteIds)

                    // ✅ Detectar cuáles IDs quedaron sin “hidratar”
                    val resolvedIds = songs.map { it.spotifyId }.toSet()

                    // missing = IDs que NO están en local y NO se resolvieron desde Spotify/cache
                    val missing = favoriteIds.filter { id ->
                        localCatalogById[id] == null && !resolvedIds.contains(id)
                    }

                    _uiState.update {
                        it.copy(
                            favoriteSongs = songs,
                            isLoading = false,
                            errorMessage = null,
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
     * - Primero local/cache (instantáneo)
     * - Luego Spotify para las faltantes
     */
    private suspend fun hydrateFavoriteIdsToSongs(favoriteIds: List<String>): List<LocalSeedSong> {
        if (favoriteIds.isEmpty()) return emptyList()

        val result = ArrayList<LocalSeedSong>(favoriteIds.size)

        val missingIds = favoriteIds.filter { id ->
            val local = localCatalogById[id]
            val cached = remoteCacheById[id]
            when {
                local != null -> {
                    result.add(local)
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

            fetched.forEach { song ->
                remoteCacheById[song.spotifyId] = song
            }

            // reconstruimos respetando el orden original de favoriteIds
            val byId = (result + fetched).associateBy { it.spotifyId }
            return favoriteIds.mapNotNull { byId[it] }
        }

        return result
    }

    /**
     * Fetch paralelo de detalles por ID.
     * Si no hay conexión, getTrackDetail devolverá null y el item quedará como "missing".
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