package com.luis.lifemusic.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.localsed.LocalSeedSong
import com.luis.lifemusic.data.repository.FavoritesRepository
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.data.repository.SpotifyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.TimeoutCancellationException

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val sessionRepository: SessionRepository,
    private val favoritesRepository: FavoritesRepository,
    private val spotifyRepository: SpotifyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    // Cache para evitar recargas innecesarias
    private var cachedSongs: List<LocalSeedSong>? = null
    private var lastFetchTime: Long = 0
    private val CACHE_DURATION = 5 * 60 * 1000 // 5 minutos

    init {
        observeHomeContent()
    }

    private fun observeHomeContent() {
        viewModelScope.launch {
            sessionRepository.sessionUserId.flatMapLatest { userId ->
                if (userId == null) {
                    flowOf(null to emptySet<String>())
                } else {
                    favoritesRepository.observeFavoriteSongIds(userId).map { userId to it.toSet() }
                }
            }.collect { (userId, favoriteSongIds) ->
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        hasActiveSession = userId != null
                    )
                }

                if (userId != null) {
                    loadSectionsWithTimeout(favoriteSongIds)
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recommendedSongs = emptyList(),
                            newReleaseSongs = emptyList(),
                            popularSongs = emptyList(),
                            errorMessage = "Inicia sesión para descubrir música"
                        )
                    }
                }
            }
        }
    }

    private suspend fun loadSectionsWithTimeout(favoriteIds: Set<String>) {
        // Timeout de 10 segundos para toda la operación
        val result = withTimeoutOrNull(10000L) {
            try {
                calculateSections(favoriteIds)
            } catch (e: Exception) {
                null
            }
        }

        if (result == null) {
            // Si hay timeout, mostramos solo canciones locales
            showLocalOnlySections(favoriteIds)
        }
    }

    private suspend fun calculateSections(favoriteIds: Set<String>) {
        try {
            // Usar caché si es válido
            val allSongs = if (shouldRefreshCache()) {
                spotifyRepository.getDiscoverySongs().also {
                    cachedSongs = it
                    lastFetchTime = System.currentTimeMillis()
                }
            } else {
                cachedSongs ?: spotifyRepository.getDiscoverySongs()
            }

            // Si no hay canciones de la API, usar solo locales
            if (allSongs.isEmpty()) {
                showLocalOnlySections(favoriteIds)
                return
            }

            // Separar locales y de API para estadísticas
            val localSongs = allSongs.filter { it.imageRes != 0 }
            val apiSongs = allSongs.filter { it.imageRes == 0 }

            // Log para debugging
            android.util.Log.d("HomeViewModel",
                "Locales: ${localSongs.size}, API: ${apiSongs.size}, Total: ${allSongs.size}")

            // Filtrar favoritos
            val discoverableSongs = allSongs.filter { it.spotifyId !in favoriteIds }

            // Crear secciones con variedad
            val recommended = createVariedSection(discoverableSongs, 12)
            val newReleases = discoverableSongs
                .sortedByDescending { it.releaseDate }
                .take(12)
            val popular = discoverableSongs
                .sortedByDescending { it.popularity }
                .take(12)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    recommendedSongs = recommended,
                    newReleaseSongs = newReleases,
                    popularSongs = popular,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("HomeViewModel", "Error en calculateSections", e)
            showLocalOnlySections(favoriteIds)
        }
    }

    private fun createVariedSection(songs: List<LocalSeedSong>, limit: Int): List<LocalSeedSong> {
        // Mezclar para tener variedad de artistas y géneros
        return songs
            .shuffled()
            .distinctBy { it.artistIds.firstOrNull() } // Evitar muchos del mismo artista
            .take(limit)
            .ifEmpty { songs.take(limit) }
    }

    private suspend fun showLocalOnlySections(favoriteIds: Set<String>) {
        // Usar SOLO el catálogo local cuando la API falla
        val localOnly = com.luis.lifemusic.data.localsed.localSeedSongs
            .filter { it.spotifyId !in favoriteIds }

        _uiState.update {
            it.copy(
                isLoading = false,
                recommendedSongs = localOnly.shuffled().take(12),
                newReleaseSongs = localOnly.sortedByDescending { it.releaseDate }.take(12),
                popularSongs = localOnly.sortedByDescending { it.popularity }.take(12),
                errorMessage = "Usando catálogo local (sin conexión a API)"
            )
        }
    }

    private fun shouldRefreshCache(): Boolean {
        return cachedSongs == null ||
                System.currentTimeMillis() - lastFetchTime > CACHE_DURATION
    }

    fun addFavorite(songSpotifyId: String) {
        viewModelScope.launch {
            val userId = sessionRepository.sessionUserId.first()
            if (userId != null) {
                favoritesRepository.addFavorite(userId, songSpotifyId)
            }
        }
    }

    fun refreshContent() {
        viewModelScope.launch {
            cachedSongs = null // Forzar refresco
            val userId = sessionRepository.sessionUserId.first()
            if (userId != null) {
                val favoriteIds = favoritesRepository.observeFavoriteSongIds(userId).first().toSet()
                loadSectionsWithTimeout(favoriteIds)
            }
        }
    }
}