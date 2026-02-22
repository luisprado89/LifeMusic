package com.luis.lifemusic.ui.detail

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
     */
    private val songsCatalog = localSeedSongs.associateBy { it.spotifyId }

    init {
        loadSongData()
        observeFavoriteStatus()
    }

    /**
     * Carga la canción:
     * 1) Si existe en local → la pinta rápido.
     * 2) Intenta cargar desde Spotify (sirve para:
     *    - canciones que vienen de internet
     *    - refrescar datos si hay conexión)
     */
    private fun loadSongData() {
        viewModelScope.launch {
            // 1) Mostrar local rápido si existe
            val localSong = songsCatalog[spotifyId]
            if (localSong != null) {
                _uiState.update { it.copy(song = localSong, isLoading = true, errorMessage = null) }
            }

            // 2) Intentar Spotify (si falla, nos quedamos con local si existía)
            val remoteSong = spotifyRepository.getTrackDetail(spotifyId)
            if (remoteSong != null) {
                _uiState.update { it.copy(song = remoteSong, isLoading = false, errorMessage = null) }
                return@launch
            }

            // Si no hay remoto:
            // - si teníamos local, lo dejamos
            // - si no teníamos nada, mostramos error
            _uiState.update { current ->
                val hasLocal = current.song != null
                current.copy(
                    isLoading = false,
                    errorMessage = if (hasLocal) null else "Canción no encontrada."
                )
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
                        // Sin sesión → no favoritos y hasActiveSession=false
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
     * Alterna favorito:
     * - Si está en favoritos → lo quita
     * - Si no está → lo añade
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