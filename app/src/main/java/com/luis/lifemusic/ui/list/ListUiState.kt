package com.luis.lifemusic.ui.list

import com.luis.lifemusic.data.Song

/**
 * ListUiState
 *
 * Representa el estado inmutable de la pantalla List.
 *
 * ✅ Principios:
 * - Es un data class inmutable.
 * - Solo contiene datos necesarios para pintar la UI.
 * - No contiene lógica de negocio.
 *
 * Es emitido por ListViewModel mediante StateFlow
 * y observado desde ListRoute.
 */
data class ListUiState(

    /**
     * Canciones que se mostrarán en la lista principal.
     */
    val songs: List<Song> = emptyList(),

    /**
     * Indica si se están cargando datos.
     * Permite mostrar un CircularProgressIndicator.
     */
    val isLoading: Boolean = false,

    /**
     * Mensaje de error opcional.
     * Si no es null, la UI mostrará feedback + botón de reintento.
     */
    val errorMessage: String? = null,

    /**
     * Flag de sesión para proteger la pantalla.
     *
     * true  -> sesión válida, se permite mostrar la lista.
     * false -> sesión inválida, ListRoute debe avisar al NavHost
     *          para redirigir a Login.
     */
    val hasActiveSession: Boolean = true
)
