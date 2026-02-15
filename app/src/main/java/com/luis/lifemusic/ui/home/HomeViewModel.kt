package com.luis.lifemusic.ui.home

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
 * HomeViewModel
 *
 * ✅ Responsabilidades:
 * - Exponer HomeUiState como StateFlow (estado observable por la UI).
 * - Cargar datos para Home (por ahora desde sampleSongs).
 * - Vigilar si existe sesión activa (userId en DataStore) vía SessionRepository.
 *
 * ✅ Reglas de arquitectura:
 * - El ViewModel NO navega.
 * - La navegación se decide en NavHost.
 * - HomeRoute observa el UiState y emite eventos al NavHost (p.ej. sesión expirada).
 *
 * 🔜 Evolución prevista:
 * - Sustituir sampleSongs por una fuente real (SongsRepository con Retrofit/Room).
 */
class HomeViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    /**
     * Estado interno mutable.
     * Inicializamos con isLoading=true para que la UI pueda mostrar progreso
     * mientras cargamos las secciones.
     */
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))

    /** Estado público inmutable para ser observado desde HomeRoute. */
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        // 1) Observamos sesión (guard de pantalla)
        observeSession()

        // 2) Cargamos contenido inicial de Home
        loadHome()
    }

    /**
     * Observa el userId de DataStore para saber si Home puede mostrarse.
     *
     * Regla:
     * - userId == null  -> no hay sesión activa -> HomeRoute avisará al NavHost.
     * - userId != null  -> sesión activa -> Home puede continuar.
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
     * Carga la información de Home.
     *
     * Estado UI:
     * - Pone isLoading=true antes de cargar.
     * - Si va bien: rellena recommendedSongs y newReleaseSongs.
     * - Si falla: setea errorMessage para que HomePage muestre feedback + reintento.
     *
     * Nota:
     * - Por ahora usamos sampleSongs para mantener estabilidad
     *   mientras se completa la migración a repositorios reales.
     */
    fun loadHome() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        recommendedSongs = sampleSongs.take(3),
                        newReleaseSongs = sampleSongs.takeLast(3)
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se pudo cargar la Home"
                    )
                }
            }
        }
    }
}
