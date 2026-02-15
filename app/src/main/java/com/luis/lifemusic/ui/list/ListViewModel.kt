package com.luis.lifemusic.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.data.sampleSongs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ListViewModel
 *
 * ✅ Responsabilidades:
 * - Exponer ListUiState como StateFlow (estado observable por la UI).
 * - Cargar canciones para List (por ahora desde sampleSongs).
 * - Vigilar sesión activa desde DataStore (SessionRepository).
 *
 * ✅ Reglas de arquitectura:
 * - El ViewModel NO navega.
 * - La navegación se gestiona desde NavHost (con ayuda de ListRoute).
 *
 * 🔜 Evolución prevista:
 * - Sustituir sampleSongs por SongsRepository (Retrofit/Room).
 * - Integrar favoritos reales (Room) y refresh/errores reales (API).
 */
class ListViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    /**
     * Estado interno mutable.
     * Inicializamos con isLoading=true para que la UI muestre progreso
     * mientras cargamos la lista.
     */
    private val _uiState = MutableStateFlow(ListUiState(isLoading = true))

    /** Estado público inmutable para ser observado desde ListRoute. */
    val uiState: StateFlow<ListUiState> = _uiState

    init {
        // 1) Guard de sesión
        observeSession()

        // 2) Carga inicial
        loadSongs()
    }

    /**
     * Observa sessionUserId desde DataStore para proteger la pantalla.
     *
     * Regla:
     * - userId == null -> no hay sesión -> ListRoute avisará al NavHost.
     * - userId != null -> sesión válida -> List puede mostrarse.
     */
    private fun observeSession() {
        viewModelScope.launch {
            sessionRepository.sessionUserId.collectLatest { userId ->
                _uiState.update { current ->
                    current.copy(hasActiveSession = userId != null)
                }
            }
        }
    }

    /**
     * Carga de canciones para List.
     *
     * Estado UI:
     * - isLoading=true antes de cargar.
     * - Si va bien: songs cargadas.
     * - Si falla: errorMessage para que la UI muestre feedback + retry.
     *
     * 🔜 Futuro:
     * Sustituir sampleSongs por SongsRepository (Retrofit + favoritos reales).
     */
    fun loadSongs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        songs = sampleSongs
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se pudo cargar la lista de canciones"
                    )
                }
            }
        }
    }
}
