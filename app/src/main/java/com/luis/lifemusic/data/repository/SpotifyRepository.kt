package com.luis.lifemusic.data.repository

import android.util.Log
import com.luis.lifemusic.data.localsed.LocalSeedSong
import com.luis.lifemusic.data.localsed.localSeedSongs
import com.luis.lifemusic.data.remote.spotify.api.SpotifyApiService
import com.luis.lifemusic.data.remote.spotify.model.TrackDto
import retrofit2.HttpException

/**
 * ============================================================
 * SPOTIFY REPOSITORY
 * ============================================================
 *
 * 🎯 OBJETIVO:
 * - Traer canciones de Spotify para mezclar con el catálogo local (offline-first).
 * - Evitar duplicados usando spotifyId REAL.
 * - Si falla Spotify o no hay resultados, devolvemos solo local (fallback).
 *
 * ✅ ESTRATEGIA ROBUSTA (tu caso real):
 * - En tu entorno Spotify devuelve 400 "Invalid limit" para 20/50.
 * - Solución: usar limit FIJO = 10 y paginar con offset (0,10,20,30...).
 *
 * ✅ IMPORTANTE:
 * - NO usamos "genre:" porque tu modelo local no trabaja con géneros.
 * - Usamos una query por PALABRAS CLAVE (keywords) basada en artistas favoritos.
 *   (Ej: "Nirvana U2 Michael Jackson")
 * - Esto es mucho más compatible que: artist:"X" OR artist:"Y" ...
 */
class SpotifyRepository(
    private val apiService: SpotifyApiService
) {

    companion object {
        private const val TAG = "SpotifyRepository"
        private const val MARKET_ES = "ES"

        // Spotify debería aceptar 1..50, pero a ti te falla con 20 y funciona con 10.
        private val LIMITS = listOf(10)

        // Paginación (offset debe ser múltiplo del limit). Con limit=10 => 0,10,20,30,40...
        private val OFFSETS = listOf(0, 10, 20, 30, 40)

        private const val FALLBACK_QUERY = "music"
    }

    data class DiscoveryResult(
        val songs: List<LocalSeedSong>,
        val usedOfflineFallback: Boolean
    )

    /**
     * Devuelve catálogo combinado (local + remoto).
     * Estrategia:
     * 1) Intentar query "estricta" (la que venga, normalmente con artist:)
     * 2) Si total=0 => intentar query "simple" (sin artist:)
     * 3) Si sigue 0 => fallback "music"
     */
    suspend fun getDiscoverySongs(query: String): DiscoveryResult {
        val strictQuery = query.trim().ifBlank { FALLBACK_QUERY }
        val simpleQuery = simplifyQuery(strictQuery)

        val queriesToTry = buildList {
            add(strictQuery)
            if (simpleQuery != strictQuery) add(simpleQuery)
            if (FALLBACK_QUERY !in this) add(FALLBACK_QUERY)
        }

        for (q in queriesToTry) {
            for (limit in LIMITS) {
                val collected = mutableListOf<LocalSeedSong>()

                try {
                    for (offset in OFFSETS) {
                        Log.d(TAG, "Search q='$q' limit=$limit offset=$offset")

                        val response = apiService.searchTracks(
                            query = q,
                            market = MARKET_ES,
                            limit = limit,
                            offset = offset
                        )

                        val items = response.tracks.items
                        val total = response.tracks.total
                        Log.d(TAG, "items=${items.size} (offset=$offset) total=$total")

                        if (items.isEmpty()) break

                        collected += items.map { it.toLocalSeedSong() }

                        // Si Spotify dice total=0, no tiene sentido seguir paginando
                        if (total == 0) break
                    }

                    val combined = (localSeedSongs + collected).distinctBy { it.spotifyId }

                    val hasRemoteUnique = collected.any { remote ->
                        localSeedSongs.none { local -> local.spotifyId == remote.spotifyId }
                    }

                    Log.d(TAG, "RESULT remote=${collected.size} combined=${combined.size} hasRemoteUnique=$hasRemoteUnique")

                    // ✅ Si conseguimos al menos 1 canción remota única: éxito real
                    if (hasRemoteUnique) {
                        return DiscoveryResult(
                            songs = combined,
                            usedOfflineFallback = false
                        )
                    }

                    // Si no trajo nada remoto, probamos la siguiente query
                } catch (e: HttpException) {
                    val body = e.response()?.errorBody()?.string().orEmpty()
                    Log.e(TAG, "HTTP ${e.code()} search q='$q' limit=$limit: $body")

                    // Si por lo que sea vuelve a dar Invalid limit, no merece seguir con otros offsets
                    val invalidLimit = e.code() == 400 && body.contains("Invalid limit", ignoreCase = true)
                    if (!invalidLimit) break
                } catch (e: Exception) {
                    Log.e(TAG, "Error search q='$q' limit=$limit", e)
                    break
                }
            }
        }

        // Fallback final
        return DiscoveryResult(
            songs = localSeedSongs,
            usedOfflineFallback = true
        )
    }

    /**
     * Track detail por ID (para Detail).
     */
    suspend fun getTrackDetail(trackId: String): LocalSeedSong? {
        return try {
            apiService.getTrackById(trackId, market = MARKET_ES).toLocalSeedSong()
        } catch (e: Exception) {
            Log.e(TAG, "No se pudo cargar detalle de trackId=$trackId", e)
            null
        }
    }

    /**
     * Convierte:
     *  artist:"Nirvana" OR artist:"U2" OR artist:"Michael Jackson"
     * en:
     *  Nirvana OR U2 OR "Michael Jackson"
     */
    private fun simplifyQuery(q: String): String {
        var out = q

        // Quitar prefijos artist:
        out = out.replace(Regex("""artist:""""), "")
        out = out.replace(Regex("""artist:""".toRegex().pattern), "")

        // Quitar artist: sin comillas
        out = out.replace(Regex("""artist:"""), "")
        out = out.replace(Regex("""artist:"""), "")
        out = out.replace(Regex("""artist:"""), "")

        out = out.replace("artist:", "")

        // Limpiar comillas dobles repetidas/espacios
        out = out.replace(Regex("""\s+"""), " ").trim()

        // Si queda vacío, fallback
        return out.ifBlank { FALLBACK_QUERY }
    }
}

private fun TrackDto.toLocalSeedSong(): LocalSeedSong {
    return LocalSeedSong(
        spotifyId = id,
        imageRes = 0,
        title = name,
        artists = artists.map { it.name },
        artistIds = artists.map { it.id },
        albumName = album.name,
        durationMs = durationMs,
        popularity = popularity,
        releaseDate = album.releaseDate,
        imageUrl = album.images.firstOrNull()?.url
    )
}