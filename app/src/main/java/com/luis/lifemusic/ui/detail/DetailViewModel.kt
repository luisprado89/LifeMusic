package com.luis.lifemusic.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.repository.FavoritesRepository
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.data.sampleSongs
import com.luis.lifemusic.page.DetailDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * DetailViewModel
 *
 * ✅ Responsabilidades:
 * - Leer songId desde argumentos de navegación (SavedStateHandle).
 * - Cargar el detalle (de momento desde sampleSongs).
 * - Sincronizar el estado de favorito por usuario (Room) mediante FavoritesRepository.
 * - Vigilar sesión activa (DataStore) para proteger la pantalla.
 *
 * ✅ Reglas de arquitectura:
 * - El ViewModel NO navega.
 * - DetailRoute observa el estado y gestiona redirecciones (sesión expirada).
 *
 * 🔜 Evolución prevista:
 * - Sustituir sampleSongs por SongsRepository (Retrofit/Room).
 * - El favorito podría observarse como Flow para updates automáticos.
 */
class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    /**
     * Estado interno mutable.
     * Arrancamos en loading para que la UI muestre progreso inicial.
     */
    private val _uiState = MutableStateFlow(DetailUiState(isLoading = true))

    /** Estado público inmutable para ser observado desde DetailRoute. */
    val uiState: StateFlow<DetailUiState> = _uiState

    /**
     * songId viene de Navigation Compose.
     * checkNotNull: si falta este argumento, es un fallo de navegación/configuración.
     */
    private val songId: Int = checkNotNull(savedStateHandle[DetailDestination.songIdArg])

    /**
     * UserId actual de sesión; se actualiza observando DataStore.
     * Se usa para consultar/actualizar favoritos por usuario.
     */
    private var currentUserId: Long? = null

    init {
        // 1) Guard de sesión
        observeSession()

        // 2) Carga inicial del detalle
        loadDetail()
    }

    /**
     * Observa DataStore (SessionRepository) para mantener el guard de sesión de Detail.
     *
     * Regla:
     * - userId == null -> sesión inválida -> DetailRoute avisará al NavHost.
     * - userId != null -> sesión válida -> se permite operar (favoritos).
     */
    private fun observeSession() {
        viewModelScope.launch {
            sessionRepository.sessionUserId.collectLatest { userId ->
                currentUserId = userId

                _uiState.update { current ->
                    current.copy(hasActiveSession = userId != null)
                }

                // Si hay sesión y ya tenemos canción cargada, refrescamos favorito real.
                if (userId != null && _uiState.value.song != null) {
                    refreshFavoriteStatus()
                }
            }
        }
    }

    /**
     * Carga el detalle de la canción.
     *
     * Por ahora se resuelve desde sampleSongs para mantener estabilidad.
     * 🔜 Futuro: cuando exista SongsRepository, esto consultará capa de datos real.
     */
    fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val song = sampleSongs.firstOrNull { it.id == songId }
            if (song == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        song = null,
                        errorMessage = "No se encontró la canción solicitada"
                    )
                }
                return@launch
            }

            // Pintamos rápido con fallback visual, y luego consultamos favorito real en Room.
            _uiState.update {
                it.copy(
                    isLoading = false,
                    song = song,
                    isFavorite = song.isFavorite, // fallback inicial
                    errorMessage = null
                )
            }

            // Sincroniza favorito real (si hay sesión).
            refreshFavoriteStatus()
        }
    }

    /**
     * Consulta en Room si la canción está en favoritos del usuario actual.
     */
    private fun refreshFavoriteStatus() {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            val song = _uiState.value.song ?: return@launch
            val favorite = favoritesRepository.isFavorite(userId, song.id)
            _uiState.update { it.copy(isFavorite = favorite) }
        }
    }

    /**
     * Evento de UI para alternar favorito.
     *
     * Regla:
     * - Si no hay sesión activa, no se hace nada (protección).
     */
    fun onFavoriteClick() {
        val userId = currentUserId ?: return
        val song = _uiState.value.song ?: return

        viewModelScope.launch {
            val currentlyFavorite = _uiState.value.isFavorite

            if (currentlyFavorite) {
                favoritesRepository.removeFavorite(userId, song.id)
            } else {
                favoritesRepository.addFavorite(userId, song.id)
            }

            // Actualizamos UI de forma inmediata para mejor UX.
            _uiState.update { it.copy(isFavorite = !currentlyFavorite) }
        }
    }
}
