package com.luis.lifemusic.ui.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.localsed.localSeedSongs
import com.luis.lifemusic.data.repository.FavoritesRepository
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.data.repository.SpotifyRepository
import com.luis.lifemusic.page.DetailDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ============================================================
 * DETAIL VIEWMODEL
 * ============================================================
 *
 * 🎯 RESPONSABILIDAD:
 * - Cargar canción por spotifyId (argumento de navegación).
 * - Si existe en catálogo local → mostrar al instante.
 * - Si no existe (o para refrescar) → pedirla a Spotify.
 * - Observar si está en favoritos.
 * 🎯 MEJORA:
 * - Prioriza catálogo local (tiene popularity real)
 * - Mezcla datos de Spotify manteniendo popularity local
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository,
    private val favoritesRepository: FavoritesRepository,
    private val spotifyRepository: SpotifyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState(isLoading = true))
    val uiState: StateFlow<DetailUiState> = _uiState

    private val spotifyId: String =
        checkNotNull(savedStateHandle[DetailDestination.spotifyIdArg])

    /**
     * Catálogo local indexado por spotifyId para lookup rápido.
     * ¡Estas canciones TIENEN popularity real!
     */
    private val songsCatalog = localSeedSongs.associateBy { it.spotifyId }

    init {
        loadSongData()
        observeFavoriteStatus()
    }

    /**
     * Carga la canción de manera inteligente:
     * 1️⃣ Primero local (rápido y con popularity)
     * 2️⃣ Luego Spotify para actualizar imagen/álbum (si existe)
     */
    private fun loadSongData() {
        viewModelScope.launch {
            // 1️⃣ BUSCAR EN CATÁLOGO LOCAL (si existe)
            val localSong = songsCatalog[spotifyId]

            if (localSong != null) {
                // Mostramos local inmediatamente (con popularity REAL)
                _uiState.update {
                    it.copy(
                        song = localSong,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                Log.d("DetailViewModel", "✅ Usando canción local: ${localSong.title} pop=${localSong.popularity}")
            } else {
                // Si no hay local, mostramos loading mientras pedimos a Spotify
                _uiState.update { it.copy(isLoading = true) }
            }

            // 2️⃣ INTENTAR SPOTIFY PARA ACTUALIZAR DATOS
            val remoteSong = spotifyRepository.getTrackDetail(spotifyId)

            if (remoteSong != null) {
                // Mezclar datos: mantener popularity local si existe
                val finalSong = if (localSong != null) {
                    localSong.copy(
                        // Solo actualizamos lo que Spotify SÍ da
                        imageUrl = remoteSong.imageUrl ?: localSong.imageUrl,
                        albumName = remoteSong.albumName,
                        releaseDate = remoteSong.releaseDate,
                        // ¡MANTENEMOS POPULARITY LOCAL!
                        popularity = localSong.popularity
                    )
                } else {
                    remoteSong
                }

                _uiState.update {
                    it.copy(
                        song = finalSong,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                Log.d("DetailViewModel", "✅ Actualizado con Spotify: ${finalSong.title} pop=${finalSong.popularity}")
                return@launch
            }

            // 3️⃣ Si no hay local ni remoto, mostrar error
            if (localSong == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Canción no encontrada."
                    )
                }
            }
        }
    }

    /**
     * Observa si la canción está en favoritos y si hay sesión activa.
     */
    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            sessionRepository.sessionUserId
                .flatMapLatest { userId ->
                    if (userId == null) {
                        flowOf(null)
                    } else {
                        favoritesRepository.observeFavoriteSongIds(userId)
                            .map { favoriteIds -> userId to (spotifyId in favoriteIds) }
                    }
                }
                .collect { result ->
                    if (result == null) {
                        _uiState.update {
                            it.copy(
                                isFavorite = false,
                                hasActiveSession = false
                            )
                        }
                    } else {
                        val (_, isFavorite) = result
                        _uiState.update {
                            it.copy(
                                isFavorite = isFavorite,
                                hasActiveSession = true
                            )
                        }
                    }
                }
        }
    }

    /**
     * Alterna favorito.
     */
    fun toggleFavorite() {
        viewModelScope.launch {
            val userId = sessionRepository.sessionUserId.first() ?: return@launch

            if (uiState.value.isFavorite) {
                favoritesRepository.removeFavorite(userId, spotifyId)
            } else {
                favoritesRepository.addFavorite(userId, spotifyId)
            }
        }
    }
}