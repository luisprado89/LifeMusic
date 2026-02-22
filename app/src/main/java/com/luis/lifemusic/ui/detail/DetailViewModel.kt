package com.luis.lifemusic.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.localsed.localSeedSongs
import com.luis.lifemusic.data.repository.FavoritesRepository
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.page.DetailDestination // Corregida la importación
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
 * ViewModel para la pantalla de Detalle de la Canción.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState(isLoading = true))
    val uiState: StateFlow<DetailUiState> = _uiState

    private val spotifyId: String = checkNotNull(savedStateHandle[DetailDestination.spotifyIdArg])

    private val songsCatalog = localSeedSongs.associateBy { it.spotifyId }

    init {
        loadSongData()
        observeFavoriteStatus()
    }

    private fun loadSongData() {
        val song = songsCatalog[spotifyId]
        if (song == null) {
            _uiState.update {
                it.copy(isLoading = false, errorMessage = "Canción no encontrada.")
            }
        } else {
            _uiState.update {
                it.copy(song = song)
            }
        }
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            sessionRepository.sessionUserId.flatMapLatest { userId ->
                if (userId == null) {
                    flowOf(false)
                } else {
                    favoritesRepository.observeFavoriteSongIds(userId)
                        .map { favoriteIds -> spotifyId in favoriteIds }
                }
            }.collect { isFavorite ->
                _uiState.update { it.copy(isLoading = false, isFavorite = isFavorite, hasActiveSession = true) }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val userId = sessionRepository.sessionUserId.first()
            if (userId != null) {
                if (uiState.value.isFavorite) {
                    favoritesRepository.removeFavorite(userId, spotifyId)
                } else {
                    favoritesRepository.addFavorite(userId, spotifyId)
                }
            }
        }
    }
}
