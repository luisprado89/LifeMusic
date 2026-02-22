package com.luis.lifemusic.data.remote.spotify.api

import com.luis.lifemusic.data.remote.spotify.model.SpotifySearchResponse
import com.luis.lifemusic.data.remote.spotify.model.TrackDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * ============================================================
 * SPOTIFY API SERVICE (Retrofit)
 * ============================================================
 *
 * 🎯 RESPONSABILIDAD:
 * - Definir los endpoints HTTP que usamos de Spotify Web API.
 *
 * ✅ Endpoints usados en la app:
 * 1) Search (para descubrir canciones en Home)
 * 2) Track by Id (para detalle de canción)
 *
 * 📌 Nota importante:
 * - En algunos entornos Spotify rechaza límites altos (ej: 50) en /search.
 * - Solución robusta: usar limit pequeño y paginar con offset.
 */
interface SpotifyApiService {

    /**
     * Busca canciones según una query.
     *
     * @param query  Texto de búsqueda (ej: artist:"Queen" OR artist:"Adele")
     * @param type   Tipo de búsqueda (por defecto "track")
     * @param market Mercado para resultados (por defecto "ES")
     * @param limit  Cantidad de resultados por página (recomendado 10)
     * @param offset Desde qué posición continuar (paginación)
     */
    @GET("search")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("market") market: String = "ES",
        @Query("limit") limit: Int,
        @Query("offset") offset: Int = 0
    ): SpotifySearchResponse

    /**
     * Obtiene el detalle completo de una canción por su ID.
     */
    @GET("tracks/{id}")
    suspend fun getTrackById(
        @Path("id") id: String,
        @Query("market") market: String = "ES"
    ): TrackDto
}