package com.luis.lifemusic.ui.home

import com.luis.lifemusic.data.localsed.LocalSeedSong

/**
 * Estado de la UI para la pantalla de inicio (Home).
 */
data class HomeUiState(

    /**
     * Canciones para la sección "Recomendadas para ti".
     * Se calculan en base a los artistas favoritos del usuario.
     */
    val recommendedSongs: List<LocalSeedSong> = emptyList(),

    /**
     * Canciones para la sección "Nuevos Lanzamientos".
     * Se basa en la fecha de lanzamiento de las canciones.
     */
    val newReleaseSongs: List<LocalSeedSong> = emptyList(),

    /**
     * Canciones para la sección "Más Populares".
     * Se basa en el índice de popularidad de Spotify.
     */
    val popularSongs: List<LocalSeedSong> = emptyList(),

    /**
     * Indica si se están cargando los datos de las secciones.
     */
    val isLoading: Boolean = false,

    /**
     * Mensaje de error para mostrar si falla la carga de datos.
     */
    val errorMessage: String? = null,

    /**
     * Flag para saber si hay una sesión activa.
     * Si es `false`, la Route se encargará de navegar al Login.
     */
    val hasActiveSession: Boolean = true
)