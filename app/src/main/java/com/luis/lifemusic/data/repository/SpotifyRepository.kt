package com.luis.lifemusic.data.repository

import android.util.Log
import com.luis.lifemusic.data.localsed.LocalSeedSong
import com.luis.lifemusic.data.localsed.localSeedSongs
import com.luis.lifemusic.data.remote.spotify.api.SpotifyApiClient
import com.luis.lifemusic.data.remote.spotify.model.TrackDto
import retrofit2.HttpException

class SpotifyRepository {

    private val apiService = SpotifyApiClient.apiService

    init {
        Log.d("SpotifyRepository", "🚀 Repositorio de Spotify creado")
    }

    suspend fun getDiscoverySongs(): List<LocalSeedSong> {
        Log.d("SpotifyRepository", "📡 getDiscoverySongs() llamado")

        return try {
            // Corregido: Se añade el parámetro 'limit' que ahora es obligatorio.
            val response = apiService.searchTracks(
                query = "genre:pop",
                type = "track",
                market = "ES",
                limit = 50
            )
            
            val items = response.tracks.items
            Log.d("SpotifyRepository", "✅ API OK: ${items.size} canciones")

            val apiSongs = items.map { it.toLocalSeedSong() }
            (localSeedSongs + apiSongs).distinctBy { it.spotifyId }

        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("SpotifyRepository", "❌ HTTP ${e.code()}: $errorBody")
            localSeedSongs

        } catch (e: Exception) {
            Log.e("SpotifyRepository", "❌ Error general", e)
            localSeedSongs
        }
    }
}

private fun TrackDto.toLocalSeedSong(): LocalSeedSong {
    return LocalSeedSong(
        spotifyId = this.id,
        imageRes = 0,
        title = this.name,
        artists = this.artists.map { it.name },
        artistIds = this.artists.map { it.id },
        albumName = this.album.name,
        durationMs = this.durationMs,
        popularity = this.popularity,
        releaseDate = this.album.releaseDate,
        imageUrl = this.album.images.firstOrNull()?.url
    )
}
