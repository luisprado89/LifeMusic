package com.luis.lifemusic.data.repository

import android.util.Log
import com.luis.lifemusic.data.localsed.LocalSeedSong
import com.luis.lifemusic.data.localsed.localSeedSongs
import com.luis.lifemusic.data.remote.spotify.api.SpotifyApiService
import com.luis.lifemusic.data.remote.spotify.model.TrackDto
import retrofit2.HttpException
import kotlin.math.absoluteValue

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
 * ✅ CORRECCIÓN IMPORTANTE:
 * - El endpoint "tracks/{id}" NO devuelve popularity con Client Credentials.
 * - Por eso generamos un valor coherente en el mapper.
 */
class SpotifyRepository(
    private val apiService: SpotifyApiService
) {

    companion object {
        private const val TAG = "SpotifyRepository"
        private const val MARKET_ES = "ES"

        // Límite fijo de 10 para evitar error "Invalid limit"
        private val LIMITS = listOf(10)

        // Paginación: offset 0,10,20,30,40
        private val OFFSETS = listOf(0, 10, 20, 30, 40)

        private const val FALLBACK_QUERY = "music"
    }

    data class DiscoveryResult(
        val songs: List<LocalSeedSong>,
        val usedOfflineFallback: Boolean
    )

    /**
     * Devuelve catálogo combinado (local + remoto).
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
            val response = apiService.getTrackById(trackId, market = MARKET_ES)

            // Log para depuración
            Log.d(TAG, "getTrackDetail: ${response.name} - popularity=${response.popularity}")

            response.toLocalSeedSong()
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

/**
 * ============================================================
 * MAPPER CORREGIDO
 * ============================================================
 *
 * 🔥 IMPORTANTE:
 * - El endpoint "tracks/{id}" NO devuelve popularity con Client Credentials.
 * - Por eso generamos un valor coherente basado en el ID.
 */
private fun TrackDto.toLocalSeedSong(): LocalSeedSong {
    // Si popularity es 0 (por Client Credentials), generamos un valor coherente
    val finalPopularity = if (popularity > 0) {
        popularity
    } else {
        // Usamos el hash del ID para tener un valor consistente
        // Rango 30-99 para que nunca sea 0
        (id.hashCode().absoluteValue % 70) + 30
    }

    return LocalSeedSong(
        spotifyId = id,
        imageRes = 0,
        title = name,
        artists = artists.map { it.name },
        artistIds = artists.map { it.id },
        albumName = album.name,
        durationMs = durationMs,
        popularity = finalPopularity,  // ✅ Nunca será 0
        releaseDate = album.releaseDate,
        imageUrl = album.images.firstOrNull()?.url
    )
}