package com.luis.lifemusic.data.remote.spotify.api

import com.luis.lifemusic.data.remote.spotify.model.SpotifySearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz de Retrofit para la API de Búsqueda de Spotify.
 */
interface SpotifyApiService {

    /**
     * Busca canciones que coincidan con una consulta.
     */
    @GET("search")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("type") type: String,
        @Query("market") market: String,
        @Query("limit") limit: Int // Se elimina el valor por defecto para ser más explícitos
    ): SpotifySearchResponse
}
