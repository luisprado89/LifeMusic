package com.luis.lifemusic.ui.list

import com.luis.lifemusic.data.localsed.LocalSeedSong

/**
 * Estado de la UI para la pantalla de la lista de favoritos.
 */
data class ListUiState(

    /**
     * La lista de canciones favoritas del usuario.
     * Se obtiene cruzando los IDs de favoritos de Room con el catálogo de canciones.
     */
    val favoriteSongs: List<LocalSeedSong> = emptyList(),

    /**
     * Indica si se está realizando la carga inicial o si hay una operación en curso.
     */
    val isLoading: Boolean = false,

    /**
     * Mensaje de error para mostrar en la UI si algo falla.
     */
    val errorMessage: String? = null,

    /**
     * Flag para saber si hay una sesión activa.
     * Si es `false`, la Route se encargará de navegar al Login.
     */
    val hasActiveSession: Boolean = true,

    /**
     * ✅ NUEVO:
     * Cantidad de favoritos que están guardados en Room (IDs),
     * pero no se pudieron “hidratar” a canciones completas porque
     * no existen en local y no se pudo obtener detalle desde Spotify.
     *
     * Ejemplo típico: el usuario añadió favoritos desde internet y luego perdió conexión.
     */
    val missingRemoteCount: Int = 0,

    /**
     * ✅ NUEVO:
     * Lista de IDs remotos que faltan (por si luego quieres mostrar un botón "Reintentar"
     * o implementar un placeholder por item).
     */
    val missingRemoteIds: List<String> = emptyList()
)