package com.luis.lifemusic.ui.detail

import com.luis.lifemusic.data.localsed.LocalSeedSong

/**
 * Estado de la UI para la pantalla de detalle de una canción.
 */
data class DetailUiState(

    /**
     * La canción cuyos detalles se están mostrando.
     * Es `null` durante la carga inicial o si ocurre un error.
     */
    val song: LocalSeedSong? = null,

    /**
     * Indica si la canción actual es favorita para el usuario logueado.
     * Permite al botón de favorito saber si debe mostrar "Añadir" o "Quitar".
     */
    val isFavorite: Boolean = false,

    /**
     * Indica si se están cargando los datos.
     */
    val isLoading: Boolean = false,

    /**
     * Mensaje de error para mostrar en la UI.
     */
    val errorMessage: String? = null,

    /**
     * Flag para saber si hay una sesión activa.
     */
    val hasActiveSession: Boolean = true
)
