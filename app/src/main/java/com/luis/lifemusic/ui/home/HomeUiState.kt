package com.luis.lifemusic.ui.home

import com.luis.lifemusic.data.Song

/**
 * HomeUiState
 *
 * Representa el estado inmutable de la pantalla Home.
 *
 * ✅ Principios:
 * - Es un data class inmutable.
 * - Solo contiene datos necesarios para pintar la UI.
 * - No contiene lógica de negocio.
 *
 * Este estado es emitido por HomeViewModel mediante StateFlow
 * y observado desde HomeRoute.
 */
data class HomeUiState(

    /**
     * Canciones mostradas en la sección:
     * "Recomendadas para ti".
     */
    val recommendedSongs: List<Song> = emptyList(),

    /**
     * Canciones mostradas en la sección:
     * "Nuevos lanzamientos".
     */
    val newReleaseSongs: List<Song> = emptyList(),

    /**
     * Indica si se están cargando datos.
     * Permite mostrar un CircularProgressIndicator en la UI.
     */
    val isLoading: Boolean = false,

    /**
     * Mensaje de error opcional.
     * Si no es null, la UI mostrará feedback y botón de reintento.
     */
    val errorMessage: String? = null,

    /**
     * Flag de sesión para proteger Home.
     *
     * true  -> Hay sesión activa (userId válido en DataStore).
     * false -> No hay sesión activa → se debe redirigir a Login.
     *
     * Este campo permite implementar un "guard de sesión"
     * sin mezclar lógica de navegación en el ViewModel.
     */
    val hasActiveSession: Boolean = true
)
