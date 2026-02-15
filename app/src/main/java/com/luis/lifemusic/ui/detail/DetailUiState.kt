package com.luis.lifemusic.ui.detail

import com.luis.lifemusic.data.Song

/**
 * DetailUiState
 *
 * Representa el estado inmutable de la pantalla Detail.
 *
 * ✅ Principios:
 * - Es un data class inmutable.
 * - Solo contiene datos necesarios para pintar la UI.
 * - No contiene lógica de negocio.
 *
 * Es emitido por DetailViewModel mediante StateFlow
 * y observado desde DetailRoute.
 */
data class DetailUiState(

    /**
     * Canción actual a mostrar.
     *
     * - null mientras se está cargando.
     * - null si ocurre un error.
     */
    val song: Song? = null,

    /**
     * Estado actual del favorito para el usuario logueado.
     * Permite desacoplar el toggle de favorito del modelo Song.
     */
    val isFavorite: Boolean = false,

    /**
     * Indica si se están cargando los datos del detalle.
     */
    val isLoading: Boolean = false,

    /**
     * Mensaje de error opcional.
     * Si no es null, la UI mostrará feedback y botón de reintento.
     */
    val errorMessage: String? = null,

    /**
     * Flag de sesión para proteger la pantalla.
     *
     * true  -> sesión válida.
     * false -> sesión inválida, DetailRoute debe redirigir a Login.
     */
    val hasActiveSession: Boolean = true
)
