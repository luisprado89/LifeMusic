package com.luis.lifemusic.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.localsed.localSeedSongs
import com.luis.lifemusic.data.repository.FavoritesRepository
import com.luis.lifemusic.data.repository.SessionRepository
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
 * ViewModel para la pantalla "Mis Favoritos" (ListPage).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModel(
    private val sessionRepository: SessionRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState(isLoading = true))
    val uiState: StateFlow<ListUiState> = _uiState

    private val songsCatalog = localSeedSongs.associateBy { it.spotifyId }

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            sessionRepository.sessionUserId.flatMapLatest { userId ->
                _uiState.update { it.copy(isLoading = true, hasActiveSession = userId != null) }
                if (userId == null) {
                    flowOf(emptyList())
                } else {
                    favoritesRepository.observeFavoriteSongIds(userId)
                }
            }.map { favoriteIds ->
                favoriteIds.mapNotNull { songsCatalog[it] }
            }.collect { favoriteSongs ->
                _uiState.update {
                    it.copy(isLoading = false, favoriteSongs = favoriteSongs)
                }
            }
        }
    }

    /**
     * Elimina una canción de la lista de favoritos del usuario.
     */
    fun removeFavorite(songSpotifyId: String) {
        viewModelScope.launch {
            // Obtenemos el userId actual de forma asíncrona.
            val userId = sessionRepository.sessionUserId.first()
            if (userId != null) {
                favoritesRepository.removeFavorite(userId, songSpotifyId)
            }
        }
    }
}
