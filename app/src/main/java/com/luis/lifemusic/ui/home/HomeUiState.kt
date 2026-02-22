package com.luis.lifemusic.ui.home

import com.luis.lifemusic.data.localsed.LocalSeedSong

/**
 * ============================================================
 * HOME UI STATE
 * ============================================================
 *
 * 🎯 RESPONSABILIDAD:
 * - Representar TODO el estado que necesita la pantalla Home.
 * - Es inmutable (data class).
 * - Solo se modifica desde el ViewModel.
 *
 * 📌 Contiene:
 * - Las 3 listas de canciones.
 * - Estados globales (loading / error).
 * - Mensajes informativos.
 * - Estado de sesión.
 *
 * 👉 La UI solo observa este estado.
 */
data class HomeUiState(

    /**
     * Canciones para la sección:
     * "Recomendadas para ti"
     */
    val recommendedSongs: List<LocalSeedSong> = emptyList(),

    /**
     * Canciones para:
     * "Nuevos Lanzamientos"
     */
    val newReleaseSongs: List<LocalSeedSong> = emptyList(),

    /**
     * Canciones para:
     * "Más Populares"
     */
    val popularSongs: List<LocalSeedSong> = emptyList(),

    /**
     * Indica si la pantalla está cargando datos.
     */
    val isLoading: Boolean = false,

    /**
     * Mensaje de error general.
     * Si no es null, la UI muestra el bloque de error.
     */
    val errorMessage: String? = null,

    /**
     * Mensaje que indica que se está usando fallback offline.
     * Ejemplo:
     * "No se pudo conectar con Spotify. Mostrando catálogo local."
     */
    val offlineNoticeMessage: String? = null,

    /**
     * Mensaje informativo para la sección de recomendadas.
     * Ejemplo:
     * "Basado en tu catálogo local"
     */
    val recommendedInfoMessage: String? = null,

    /**
     * Indica si existe una sesión activa.
     * Si es false → HomeRoute redirige al Login.
     */
    val hasActiveSession: Boolean = true
)